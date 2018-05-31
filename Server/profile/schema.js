var mongoose = require('mongoose'),
    Schema   = mongoose.Schema;

var ProfileSchema = new Schema({

    username: {
        type: String,
        required: true,
        unique: true
    },

    password: {
        type: String,
        required: true
    },

    session: {
        type: String
    },

    email: {
        type: String,
        required: true
    }
});

module.exports = ProfileSchema;
