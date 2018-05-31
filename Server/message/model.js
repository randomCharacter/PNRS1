var mongoose = require('mongoose'),
    messageSchema = require('./schema.js'),
    APIError = require('../APIError');

var sort_by_date = function (date1, date2) {
    if (date1 > date2) return 1;
    if (date1 < date2) return -1;
    return 0;
};

messageSchema.statics.saveMessage = function(sender, receiver, data, callback) {

    var newMessage = new model({
        sender: sender,
        receiver: receiver,
        data: data,
        date: new Date()
    });

    newMessage.save(function(err) {
      if (err) return callback(err);
      return callback(null);
    });
};

messageSchema.statics.getMessages = function(me, other, callback) {
    model.find({
        $or : [
            {$and: [
                    {receiver: me},
                    {sender: other}
                    ]},
            {$and: [
                    {receiver: other},
                    {sender: me}
                    ]}
        ]
    }, function(err, found) {
        if (found.length === 0) {
            return callback([], null);
        }

        var response = [];

        found.forEach(function (object) {
            response.push({
                sender: object.sender,
                data: object.data
            })
        });

        response.sort(sort_by_date);

        return callback(response, null);
    });
};

var model = mongoose.model('message', messageSchema);

module.exports = model;
