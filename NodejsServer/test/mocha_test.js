var chai = require('chai');
var chaiHttp = require('chai-http');
var server = require('../index.js');
var should = chai.should();

chai.use(chaiHttp);

describe('Testing user information', function() {
    var user = {
        username: "test",
        password: "test"
    }

    // logging in with test account
    it('Test POST /login', function(done) {
        chai.request(server)
            .post('/login')
            .send(user)
            .end(function(err, res) {
                res.should.have.status(200);
                res.should.be.an('object');
                done();
            });
    });

    // username already exists
    it('Test POST /register', function(done) {
        chai.request(server)
            .post('/register')
            .send(user)
            .end(function(err, res) {
                res.should.have.status(401);
                res.should.be.an('object');
                done();
            });
    });
});

