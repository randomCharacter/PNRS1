var profile = require('./profile/model'),
    message = require('./message/model'),
    APIError = require('./APIError');

exports.register = function (req, res, next) {
        if (req.body.username && req.body.password && req.body.email) {
            profile.create(req.body, function (err) {
                if (err) {
                    res.status(409).send(err);
                } else {
                    res.status(200).send(APIError.OK);
                }
            });
        } else {
            res.status(400).send(APIError.NO_BODY);
        }
};

exports.login = function (req, res, next) {
    if (req.body.username && req.body.password) {
        profile.login(req.body.username, req.body.password, function (session, err) {
            if (err) {
                res.status(404).send(err);
            } else {
                res.setHeader("sessionid", session);
                res.status(200).send(APIError.OK);
            }
        });
    } else {
        res.status(400).send(APIError.NO_BODY);
    }
};

exports.sendMessage = function (req, res, next) {
    if (req.headers['sessionid']) {
        profile.isLogged(req.headers['sessionid'], function(found, err) {
            if (!found) {
                res.status(400).send(err);
            }
            if (req.body.receiver && req.body.data) {
                profile.checkReceiver(req.body.receiver, function(exists) {
                    if (!exists) {
                        res.status(404).send(APIError.RECEIVER_NOT_FOUND);
                    } else {
                        message.saveMessage(found.username, req.body.receiver, req.body.data, function (err) {
                            if (err) {
                                res.status(500).send(err);
                            } else {
                                res.status(200).send(APIError.OK);
                            }
                        })
                    }
                })
            } else {
                res.status(400).send(APIError.NO_BODY);
            }
        });
    } else {
        res.status(404).send(APIError.NOT_LOGGED_IN);
    }
};

exports.getMessages = function (req, res, next) {
    if (req.headers['sessionid']) {
        profile.isLogged(req.headers['sessionid'], function(found, err) {
            if (!found) {
                res.status(400).send(err);
            } else {
                profile.checkReceiver(req.params.id, function(exists) {
                    if (!exists) {
                        res.status(404).send(APIError.RECEIVER_NOT_FOUND);
                    } else {
                        message.getMessages(found.username, req.params.id, function (response, err) {
                            if (err) {
                                res.status(400).send(err);
                            } else {
                                res.status(200).send(response);
                            }
                        })
                    }
                })
            }
        });
    } else {
        res.status(404).send(APIError.NOT_LOGGED_IN);
    }
};

exports.getContacts = function (req, res, next) {
    if (req.headers['sessionid']) {
        profile.isLogged(req.headers['sessionid'], function(found, err) {
            if (!found) {
                res.status(400).send(err);
            } else {
                profile.getUsers(function (response, err) {
                    if (err) {
                        res.status(400).send(err);
                    } else {
                        res.status(200).send(response);
                    }
                })
            }
        });
    } else {
        res.status(404).send(APIError.NOT_LOGGED_IN);
    }
};

exports.logout = function (req, res, next) {
    if (req.headers['sessionid']) {
        profile.isLogged(req.headers['sessionid'], function(found, err) {
            if (!found) {
                res.status(404).send(err);
            } else {
                profile.logout(found.username, function (err) {
                    if (err) {
                        res.status(400).send(err);
                    } else {
                        res.status(200).send(APIError.OK);
                    }
                })
            }
        });
    } else {
        res.status(404).send(APIError.NOT_LOGGED_IN);
    }
};
