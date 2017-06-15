var settings = require('../config');
const moment = require('moment');
const jwt = require('jwt-simple');

// Encoding token using username
function encodeToken(username) {
    const playload = {
        exp: moment().add(1, 'minutes').unix(),
        iat: moment().unix(),
        sub: username
    };
    return jwt.encode(playload, settings.secretkey);
}

// Decoding token to username
function decodeToken(token, cb) {

    try {
        const payload = jwt.decode(token, settings.secretkey);

        // Checking if token is expired
        const now = moment().unix();
        if (now > payload.exp) {
            console.log('Token has expired.');
        }

        cb(null, payload);

    } catch(err) {
        cb(err, null);
    }
}

module.exports = {
    encodeToken,
    decodeToken
};