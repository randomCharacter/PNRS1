var mongoose = require('mongoose'),
    Schema   = mongoose.Schema;

var MessageSchema = new Schema({

    sender: {
        type: String,
        required: true
    },

    receiver: {
        type: String,
        required: true
    },

    data: {
        type: String
    },

    date: {
        type: Date
    }
});

module.exports = MessageSchema;
