// API version 1
var express = require('express');
var router = express.Router();
var pool = require('../database/database_connector');
var auth =  require('../authentication/authentication');
var bcrypt = require('bcrypt');
var dateTimeHandler = require('../dateTimeHandler');

// A token needs to be provided by the user except for certain requests
router.all( new RegExp(/^(?!.*\/films\/\d+$|\/films$|\/login$|\/register$).*$/m), function (request, response, next) {
    console.log("Validating token...")
    var token = (request.header('Token')) || '';

    auth.decodeToken(token, function (error, payload) {
        if (error) {
            console.log('Error: ' + error.message);
            response.status(401).json({error: "Not authorised"});
        } else {
            console.log("User has been granted access");
            next();
        }
    });
});

// Logging in with an existing user account
router.post('/login', function(request, response) {

    var username = request.body.username || '';
    var password = request.body.password || '';

    if (username != '' && password != '') {
        var query_str = {
            sql: query_str = 'SELECT customer_id, password FROM customer WHERE username=?',
            values: [username],
            timeout: 5000
        }

        pool.getConnection(function (error, connection) {
            if (error) {
                return response.status(500).json(error);

            }
            connection.query(query_str, function (error, result, fields) {
                connection.release();
                if (error) {
                    return response.status(400).json(error);
                }

                if (result.length > 0) {
                    bcrypt.compare(password, result[0].password, function (error, res) {
                        if (res === true) {
                            console.log("User entered correct password");
                            response.status(200).json({token: auth.encodeToken(username), username: username,
                                customer_id: result[0].customer_id});
                        } else {
                            console.log("User entered incorrect password");
                            response.status(401).json({error: "Invalid credentials"});
                        }
                    });
                } else {
                    console.log("User entered an username that does not exist");
                    response.status(401).json({error: "Invalid credentials"});
                }
            });
        });
    }
});

// Registering a new user account
router.post('/register', function(request, response) {
    var username = request.body.username || '';
    var password = request.body.password || '';

    if (username != '' && password != '') {
        var hash = bcrypt.hashSync(password, 10);
        var query_str = {
            sql: 'INSERT INTO `customer` (username, password) VALUES (?, ?)',
            values: [username, hash],
            timeout: 5000
        };

        pool.getConnection(function (error, connection) {
            if (error) {
                return response.status(500).json(error);
            }
            connection.query(query_str, function (error, rows, fields) {
                connection.release();
                if (error) {
                    if (error.code === 'ER_DUP_ENTRY') {

                        console.log("Username already exists in database");
                        return response.status(401).json({error: "This username already exists"});
                    } else {
                        return response.status(400).json(error);
                    }
                }
                console.log("New user account has been made");
                // Generate JWT
                response.status(200).json({"token": auth.encodeToken(username), "username": username});
            });
        });
    };
});

// Getting a certain amount of films starting from an index number
router.get('/films', function(request, response, next) {
    // amount of movies the user is requesting
    var count = request.query.count;
    // starting index number
    var offset = request.query.offset;

    if (!(parseInt(count) > 0) || !(parseInt(offset) >= 0)){
        return response.status(400).json({'error' : 'Illegal parameters'});
    }

        var query_str = {
            // sql: query_str = 'SELECT * FROM film LIMIT ? OFFSET ?;',
            // values: [count, offset],
            sql: query_str = 'SELECT * FROM film',
            values: [],
            timeout: 5000
        }
        query_str.sql += ' LIMIT ' + count + ' OFFSET ' + offset;

        pool.getConnection(function (error, connection) {
            if (error) {
                return response.status(500).json(error);
            }
            connection.query(query_str, function (error, rows, fields) {
                connection.release();
                if (error) {
                    return response.status(400).json(error);
                }
                response.status(200).json(rows);
            });
        });
});

// Getting a film by ID
router.get('/films/:filmid', function(request, response, next) {
    var filmID = request.params.filmid;

    if (filmID > 0) {
        var query_str = {
            sql: query_str = 'SELECT * FROM view_rental WHERE film_id = "' + filmID + '";',
            values: [],
            timeout: 5000
        }
        pool.getConnection(function (error, connection) {
            if (error) {
                return response.status(500).json(error);
            }
            connection.query(query_str, function (error, rows, fields) {
                connection.release();
                if (error) {
                    response.status(400).json(error);
                } else {
                    response.status(200).json(rows);
                }

            });
        });
    } else {
        response.status(400).json({'error' : 'Illegal parameters'});
    }
});

// Getting films being rented by userID
router.get('/rentals/:userid', function(request, response) {
    var userID = request.params.userid;
    var query_str = {
        sql: query_str = 'SELECT * FROM view_rental WHERE customer_id = "' + userID + '" AND return_date IS NULL;',
        values: [],
        timeout: 5000
    }

    pool.getConnection(function (error, connection) {
        if (error) {
            return response.status(500).json(error);
        }
        connection.query(query_str, function (error, rows, fields) {
            connection.release();
            if (error) {
                return response.status(400).json(error);
            }
            response.status(200).json(rows);
        });
    });
});

// Renting a film
router.post('/rentals/:userid/:inventoryid', function(request, response) {
    var userID = request.params.userid;
    var inventoryID = request.params.inventoryid;

    var query_str = {
        sql: 'INSERT INTO `rental` (inventory_id, customer_id ) VALUES ( "' + inventoryID + '", "' + userID + '");',
        values : [ ],
        timeout : 5000
    };

    pool.getConnection(function (error, connection) {
        if (error) {
            return response.status(500).json(error);
        }
        connection.query(query_str, function (error, rows, fields) {
            connection.release();
            if (error) {
                console.log(error.toString());
                return response.status(400).json(error);

            }
            console.log("Rental added to database")
            response.status(200).json({message: "Rental added to database"});
        });
    });
});

// Returning a rental
router.put('/rentals/:userid/:inventoryid', function(request, response) {
    var userID = request.params.userid;
    var inventoryID = request.params.inventoryid;

    var dateTime = dateTimeHandler.getDateTime();
    console.log(dateTime);

    var query_str = {
        sql: 'UPDATE `rental` set return_date = "' + dateTime + '" WHERE inventory_id = "' + inventoryID + '" AND  customer_id = "' + userID + '";',
        values : [ ],
        timeout : 5000
    };

    pool.getConnection(function (error, connection) {
        if (error) {
            return response.status(500).json(error);
        }
        connection.query(query_str, function (error, rows, fields) {
            connection.release();
            if (error) {
                console.log(error.toString());
                return response.status(400).json(error);
            }
            if(rows.affectedRows > 0) {
                console.log("Rental succesfully returned at " + dateTime);
                response.status(200).json({message: "Rental succesfully returned at " + dateTime});
            } else {
                console.log("Rental was not found")
                response.status(400).json({error: "Rental was not found"});
            }
        });
    });
});

// Deleting a rental from the database
router.delete('/rentals/:userid/:inventoryid', function(request, response) {
    var userID = request.params.userid;
    var inventoryID = request.params.inventoryid;

    var query_str = {
        sql: 'DELETE FROM `rental` WHERE inventory_id = "' + inventoryID + '" AND customer_id = "' + userID + '";',
        values : [ ],
        timeout : 5000
    };

    pool.getConnection(function (error, connection) {
        if (error) {
            return response.status(500).json(error);
        }
        connection.query(query_str, function (error, rows, fields) {
            connection.release();
            if (error) {
                return response.status(400).json(error);
            }

            if(rows.affectedRows > 0) {
                console.log("Rental succesfully deleted");
                response.status(200).json({message: "Rental succesfully deleted"});
            } else {
                console.log("Rental was not found")
                response.status(400).json({error: "Rental was not found"});
            }
        });
    });
});

module.exports = router;