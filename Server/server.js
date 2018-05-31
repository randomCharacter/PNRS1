var express = require('express'),
    mongoose = require('mongoose'),
    bodyParser = require('body-parser'),
    service = require('./service');

var app = express();
mongoose.connect("mongodb://localhost:27017/pnrs");

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({
    extended: true
}));

var router = express.Router();

app.use('/', router);

router.post('/register', service.register);
router.post('/login', service.login);
router.get('/contacts', service.getContacts);
router.get('/message/:id', service.getMessages);
router.post('/message', service.sendMessage);
router.post('/logout', service.logout);



app.listen(80);
console.log('Listening on port 80');
