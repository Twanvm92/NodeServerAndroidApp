// API version 1
var express = require('express');
var router = express.Router();
var pool = require('../database/database_connector');
var auth =  require('../authentication/authentication');
var bcrypt = require('bcrypt');

router.all( new RegExp("[^(\/login|\/register)]"), function (req, res, next) {

    console.log("VALIDATE TOKEN")

    var token = (req.header('Token')) || '';

    auth.decodeToken(token, function (err, payload) {
        if (err) {
            console.log('Error handler: ' + err.message);
            res.status((err.status || 401 )).json({error: new Error("Not authorised").message});
        } else {
            next();
        }
    });
});

router.post('/login', function(req, res) {

    var username = req.body.username || '';
    var password = req.body.password || '';

    if (username != '' && password != '') {
        var query_str = {
            sql: query_str = 'SELECT password FROM customer WHERE username=?',
            values: [username],
            timeout: 2000
        }

        pool.getConnection(function (error, connection) {
            if (error) {
                throw error
            }
            connection.query(query_str, function (error, result, fields) {
                connection.release();
                if (error) {
                    throw error
                }

                if (result.length > 0) {
                    bcrypt.compare(password, result[0].password, function (err, response) {
                        if (response === true) {
                            console.log("Password correct");
                            res.status(200).json({"token": auth.encodeToken(username), "username": username});
                        } else {
                            res.status(401).json({"error": "Invalid credentials"})
                        }
                    });
                } else {
                    res.status(401).json({"error": "Invalid credentials"})
                }
            });
        });
    }
});

router.post('/register', function(req, res) {

    var username = req.body.username || '';
    var password = req.body.password || '';

    if (username != '' && password != '') {
        var hash = bcrypt.hashSync(password, 10);
        var query_str = {
            sql: 'INSERT INTO `customer` (username, password) VALUES (?, ?)',
            values: [username, hash],
            timeout: 2000 // 2secs
        };

        pool.getConnection(function (error, connection) {
            if (error) {
                throw error
            }
            connection.query(query_str, function (error, rows, fields) {
                connection.release();
                if (error) {
                    if (error.code === 'ER_DUP_ENTRY') {
                        res.status(401).json({"Error": "Deze gebruiker bestaat al"});
                        return;
                    } else {
                        throw error
                    }
                }
                console.log("Gebruiker aangemaakt in database");
                console.log("Password opgeslagen als hash in database");

                // Generate JWT
                res.status(200).json({"token": auth.encodeToken(username), "username": username});
            });
        });
    };
});

module.exports = router;