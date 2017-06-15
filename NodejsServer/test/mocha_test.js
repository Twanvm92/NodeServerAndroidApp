var chai = require('chai');
var chaiHttp = require('chai-http');
var server = require('../index.js');
var should = chai.should();
var pool = require('../database/database_connector');

chai.use(chaiHttp);

describe('Testing user information', function() {
    // change mocka timeout settings
    this.timeout(15000);

    const validUser = {
        username: "test",
        password: "test"
    };

    const invalidUser = {
        username: "oewfwefjwerhh",
        password: "DoesntMatterwfweergre"
    };

    const unregisteredUser = {
        username: "anewUser",
        password: "randompassword"
    };

    const userID = 1;
    var token;
    var inventory_id = 3;

    // this will run before all tests
    before(function(done) {
        // make sure to delete test data in database before running new tests
        var query_str_del_us = {
            sql: query_str_del_us = 'DELETE FROM customer WHERE username=?',
            values: [unregisteredUser.username],
            timeout: 5000
        }

        pool.getConnection(function (error, connection) {
            if (error) {
                console.log("Before: Connection to database failed");
                throw error
            }
            connection.query(query_str_del_us, function (error, result, fields) {
                connection.release();
                if (error) {
                    console.log("Before: Deleting user failed");
                    throw error
                }
                return;
            });
        });

        // make sure to delete test rental from database
        var query_str_del_ren = {
            sql: query_str_del_ren = 'DELETE FROM rental WHERE inventory_id=?',
            values: [inventory_id],
            timeout: 5000
        }

        pool.getConnection(function (error, connection) {
            if (error) {
                console.log("Before: Connection to database failed");
                throw error
            }
            connection.query(query_str_del_ren, function (error, result, fields) {
                connection.release();
                if (error) {
                    console.log("Before: Deleting rental failed");
                    throw error
                }
                return;
            });
        });

        // get a valid JWT token
        chai.request(server)
            .post('/api/v1/login')
            .send(validUser)
            .end(function(err, res) {
                res.body.should.be.an('object');
                res.body.should.have.property('token');
                token = res.body.token;
                console.log('token: ' + token);

            });
        done();
    });

    /*---------------- Log in test ----------------*/

    // succes
    it('Test succes POST /api/v1/login', function(done) {
        chai.request(server)
            .post('/api/v1/login')
            .send(validUser)
            .end(function(err, res) {
                res.should.have.status(200);
                res.should.be.json;
                res.body.should.have.property('token')
                res.body.should.have.property('username')
                res.body.should.have.property('customer_id');
                done();
            });
    });

    // fail
    it('Test fail POST /api/v1/login', function(done) {
        chai.request(server)
            .post('/api/v1/login')
            .send(invalidUser)
            .end(function(err, res) {
                res.should.be.json;
                res.should.have.status(401);
                res.body.should.have.property('error').equal('Invalid credentials');
                done();
            });
    });

    /*---------------- Register test ----------------*/

    // succes
    it('Test succes POST /api/v1/register', function(done) {
        chai.request(server)
            .post('/api/v1/register')
            .send(unregisteredUser)
            .end(function(err, res) {
                res.should.be.json;
                res.should.have.status(200);
                res.body.should.have.property('token');
                res.body.should.have.property('username');
                done();
            });
    });

    // fail
    it('Test fail POST /api/v1/register', function(done) {
        chai.request(server)
            .post('/api/v1/register')
            .send(validUser)
            .end(function(err, res) {
                res.should.be.json;
                res.should.have.status(401);
                res.body.should.have.property('error').equal('This username already exists');
                done();
            });
    });

    /*---------------- get movies test ----------------*/

    // succes
    it('Test succes GET /api/v1/films', function(done) {
        chai.request(server)
            .get('/api/v1/films?count=20&offset=5')
            .end(function(err, res) {
                res.should.be.json
                res.should.have.status(200);
                res.body.should.be.an.instanceof(Array);
                res.body.should.have.lengthOf(20);
                done();
            });
    });

    // fail
    it('Test fail GET /api/v1/films', function(done) {
        chai.request(server)
            .get('/api/v1/films?count=0&offset=-5')
            .end(function(err, res) {
                res.should.be.json
                //res.should.have.status(400);
                //res.body.should.have.property('error').equal('Illegal parameters');
                done();
            });
    });

    /*---------------- get film by movieID test ----------------*/

    // succes
    it('Test succes GET /api/v1/films/:filmid', function(done) {
        chai.request(server)
            .get('/api/v1/films/50')
            .end(function(err, res) {
                res.should.be.json
                res.should.have.status(200);
                res.body.should.be.an.instanceof(Array);
                done();
            });
    });

    // fail
    it('Test fail GET /api/v1/films/:filmid', function(done) {
        chai.request(server)
            .get('/api/v1/films/erd')
            .end(function(err, res) {
                res.should.be.json
                res.should.have.status(400);
                res.body.should.have.property('error').equal('Illegal parameters');
                done();
            });
    });

    /*---------------- get rentals by userID test ----------------*/

    // succes
    it('Test succes GET /api/v1/rentals/:userid', function(done) {
        chai.request(server)
            .get('/api/v1/rentals/' + userID)
            .set('Token', token)
            .end(function(err, res) {
                console.log('token: ' + token);
                res.should.be.json
                res.should.have.status(200);
                res.body.should.be.an.instanceof(Array);
                done();
            });
    });

    // fail on authorization
    it('Test fail GET /api/v1/rentals/:userid', function(done) {
        chai.request(server)
            .get('/api/v1/rentals/:userid')
            .end(function(err, res) {
                res.should.be.json
                res.should.have.status(401);
                res.body.should.have.property('error').equal('Not authorised');
                done();
            });
    });

    /*---------------- renting movie test ----------------*/

    // succes
    it('Test succes POST /api/v1/rentals/:userid/:inventoryid', function(done) {
        chai.request(server)
            .post('/api/v1/rentals/1/3')
            .set('Token', token)
            .end(function(err, res) {
                console.log('token: ' + token);
                res.should.be.json
                res.should.have.status(200);
                res.body.should.be.an('object');
                res.body.should.have.property('message');
                done();
            });
    });

    // fail
    it('Test fail POST /api/v1/rentals/:userid/:inventoryid', function(done) {
        chai.request(server)
            .post('/api/v1/rentals/1/3')
            .set('Token', token)
            .end(function(err, res) {
                res.should.be.json
                res.should.have.status(400);
                res.body.should.have.property('code');
                done();
            });
    });

    /*---------------- returning rental test ----------------*/

    // succes
    it('Test succes PUT /api/v1/rentals/:userid/:inventoryid', function(done) {
        chai.request(server)
            .put('/api/v1/rentals/1/3')
            .set('Token', token)
            .end(function(err, res) {
                res.should.be.json
                res.should.have.status(200);
                res.body.should.be.an('object');
                res.body.should.have.property('message');
                done();
            });
    });

    // fail
    it('Test fail PUT /api/v1/rentals/:userid/:inventoryid', function(done) {
        chai.request(server)
            .put('/api/v1/rentals/1/5000')
            .set('Token', token)
            .end(function(err, res) {
                res.should.be.json
                res.should.have.status(400);
                res.body.should.have.property('error').equal('Rental was not found');
                done();
            });
    });

    /*---------------- deleting rental test ----------------*/

    // succes
    it('Test succes DELETE /api/v1/rentals/:userid/:inventoryid', function(done) {
        chai.request(server)
            .delete('/api/v1/rentals/1/3')
            .set('Token', token)
            .end(function(err, res) {
                res.should.be.json
                res.should.have.status(200);
                res.body.should.be.an('object');
                res.body.should.have.property('message');
                done();
            });
    });

    // fail
    it('Test fail DELETE /api/v1/rentals/:userid/:inventoryid', function(done) {
        chai.request(server)
            .delete('/api/v1/rentals/1/5000')
            .set('Token', token)
            .end(function(err, res) {
                res.should.be.json
                res.should.have.status(400);
                res.body.should.have.property('error').equal('Rental was not found');
                done();
            });
    });
});

