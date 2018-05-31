var mongoose = require('mongoose'),
    bcrypt = require('bcrypt'),
    crypto = require('crypto'),
    profileSchema = require('./schema.js'),
    APIError = require('../APIError');

profileSchema.statics.validateSecret = function (secret, hash) {
    return bcrypt.compareSync(secret, hash);
};

profileSchema.statics.create = function(data, callback) {
    model.findOne({username: data.username}, function(err, found) {
        if (found) {
            return callback(APIError.ALREADY_EXISTS);
        }

        var newProfile = new model({
            username: data.username,
            password: bcrypt.hashSync(data.password, bcrypt.genSaltSync(8), null),
            email: data.email
        });

        newProfile.save(function(err) {
          if (err) return callback(err);
          return callback(null);
        });
    });
};

profileSchema.statics.login = function(username, password, callback) {
    model.findOne({username: username}, function(err, found) {
        if (!found) {
            return callback(null, APIError.NOT_REGISTERED);
        }

        if (!model.validateSecret(password, found.password)) {
            return callback(null, APIError.INVALID_PASS);
        }

        var session = username + '-' + crypto.randomBytes(20).toString("hex");
        found.update({session: session}, function(err) {
            if (err) return callback(err);
            return callback(session, null);
        });
    });
};

profileSchema.statics.isLogged = function(session, callback) {
    model.findOne({session: session}, function(err, found) {
        if (!found) {
            return callback(null, APIError.NOT_LOGGED_IN);
        }

        return callback(found, null);
    });
};

profileSchema.statics.getUsers = function(callback) {
    var query = model.find({}).select({ "username": 1, "_id": 0});

    query.exec(function (err, someValue) {
        if (err) return callback(null, err);
        return callback(someValue, null);
    });

};

profileSchema.statics.logout = function(username, callback) {
    model.findOne({username: username}, function(err, found) {
        if (!found) {
            return callback(APIError.NOT_LOGGED_IN);
        }

        found.session = undefined;
        found.save(function(err){
            if (err) {
                return callback(err);
            }
            return callback(null);
        });
    });
};

profileSchema.statics.checkReceiver = function(username, callback) {
    model.findOne({username: username}, function(err, found) {
        if (!found) {
            return callback(false);
        }

        return callback(true);
    });
};


var model = mongoose.model('profile', profileSchema);

module.exports = model;
