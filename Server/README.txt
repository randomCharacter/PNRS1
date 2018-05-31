POST /register
body: {
  username: "milica",
  password: "Demo123",
  email: "milica.matic@rt-rk.com"
}

response: {
  message: "OK",
  code: 200
}

ERROR:
{
  message: "Wrong body parameters",
  code: 400
}
{
  message: "User already exists",
  code: 409
}

#####################################################

POST /login
body: {
  username: "milica",
  password: "Demo123",
}

response: {
  message: "User already exists",
  code: 409
}

response headers: {
  sessionid: username-123456abcdef
  ...
}

ERROR:
{
  message: "Wrong body parameters",
  code: 400
}
{
  message: "User is not registered",
  code: 404
}
{
  message: "Invalid password",
  code: 404
}

#####################################################

GET /contacts
headers: {
  sessionid: username-123456abcdef
}

response: [
  {
    username: "user1"
  },
  {
    username: "user2"
  },
  {
    username: "user3"
  },
  {
    username: "user4"
  }
]

ERROR: {
  message: "User is not logged in",
  code: 404
}

#####################################################

POST /message
body: {
  receiver: username
  data: message
}
headers: {
  sessionid: username-123456abcdef
}

response: {
  message: "OK",
  code: 200
}

ERROR:
{
  message: "Wrong body parameters",
  code: 400
}
{
  message: "User is not logged in",
  code: 404
}
{
    "message": "Receiver does not exist",
    "code": 404
}

#####################################################

GET /message/:id
  NOTE: id is username of contact who are you chatting with
headers: {
  sessionid: username-123456abcdef
}

response: {
  [
    {
      sender: "user1",
      data: "message 1"
    },
    {
      sender: "user2",
      data: "message 2"
    },
    {
      sender: "user1",
      data: "message 3"
    },
    {
      sender: "user2",
      data: "message 4"
    }
  ]
}
  NOTE: messages are sorted by date of sending

ERROR:
{
  message: "User is not logged in",
  code: 404
}
{
    "message": "Receiver does not exist",
    "code": 404
}

#####################################################

POST /logout
headers: {
  sessionid: username-123456abcdef
}

response: {
  message: "OK",
  code: 200
}

ERROR:
{
  message: "User is not logged in",
  code: 404
}
