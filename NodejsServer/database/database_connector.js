var mysql = require('mysql');
var config = require('../config/config');
var configLocalhost = require('../config/configLocalhost');

var pool = mysql.createPool({
    multipleStatements:true,
    connectionLimit : 25,
    host : configLocalhost.dbHost,
    user : configLocalhost.dbUsername,
    password : configLocalhost.dbPassword,
    database : configLocalhost.dbDatabase
});

module.exports = pool;