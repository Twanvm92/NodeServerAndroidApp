var express = require('express');
var bodyParser = require('body-parser');
var config = require('./config/config');

var app = express();
app.use(bodyParser.urlencoded({ 'extended':'true'}))
app.use(bodyParser.json());
app.use(bodyParser.json({ type: 'application/vnd.api+json' }));


app.all('*', function(request, response, next){
    console.log(request.method + " " + request.url);
    next();
})

app.use(require('./routes/api_v1'));


app.set('PORT', config.webPort);

app.get('/info', function(request, response) {
    response.send('Rent a film with this app :D');
})

app.get('/about', function(request, response) {
    response.send('Programmeren tentamen 1.4 - Twan & Maikel');
})

app.all('*', function(request, response) {
    response.status(404);
    response.send('404 - Not found');
})

var port = process.env.PORT || app.get('PORT');

app.listen(port, function() {
    console.log('Server app is listening on port ' + port);
})

module.exports = app;
