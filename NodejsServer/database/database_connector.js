var mysql = require('mysql');
var config = require('../config');

var pool = mysql.createPool({
    multipleStatements:true,
    connectionLimit : 25,
    host : config.dbHost,
    user : config.dbUser,
    password : config.dbPassword,
    database : config.dbDatabase
});

module.exports = pool;