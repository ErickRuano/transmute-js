var db = require('../config/mysql.js')();
var httpStatus = require('../config/httpStatusCodes')();
var jwt = require('jwt-simple');
var encode = require('md5');
 
var auth = {
 
  login: function(req, res) {

    console.log(req.body);
 
    var username = req.body.username || '';
    var password = req.body.password || '';
    
    if (username == '' || password == '') {
      res.status(httpStatus._401.status);
      res.json({
        "status": httpStatus._401.status,
        "message": httpStatus._401.message
      });
      return;
    }
 
    // Fire a query to DB and check if the credentials are valid
    auth.validate(username, password, res); 
  },

  validate: function(username, password, res) {
    console.log('hola')
    var query = ("select * from user where email = ? and password = ?;");
    var params = [username, password];

    db.execute(query, params, function(data){
      console.log(data)

      if(data.length != 1){
        res.status(401);
        res.json({
          "status": 401,
          "message": "Invalid credentials"
        });
        return;
      }else{
        // If authentication is success, we will generate a token
        // and dispatch it to the client
        var dbUserObj = {
          id : data[0].id,
          email : data[0].email,
          type : data[0].type,
          regionId : data[0].regionId
        };

        if(data[0].type == 1){
          var budgetQuery = "select (REG.budget - IFNULL(SUM(ORM.value),0)) budget from cbc.OrderMaterial ORM JOIN cbc.`order` ORD ON ORM.orderId = ORD.id JOIN agency AGE ON AGE.id = ORD.agencyId JOIN region REG ON REG.id = AGE.regionId WHERE AGE.regionId = ? AND YEAR(ORD.createdAt) = YEAR(CURRENT_DATE()) AND MONTH(ORD.createdAt) = MONTH(CURRENT_DATE());"
          var params = [data[0].regionId];

          db.execute(budgetQuery, params, function(budgetData){
            if(budgetData.length != 0){
              dbUserObj.budget = { current : budgetData[0].budget, total : budgetData[0].total };
            };
            res.json(genToken(dbUserObj));
            return;
          });
        }else{
          res.json(genToken(dbUserObj));
          return;

        };


      }

    });

  },
 
  validateUser: function(username, callback) {
    var query = ("SELECT * FROM user WHERE email = ?");
    var params = [username];

    db.execute(query, params, function(data){
      callback(data[0]);
    });
  },

  
}
 
// private method
function genToken(user) {

  var expires = expiresIn(1); // days

  var token = jwt.encode({
    exp: expires,
    user : user.id,
    email : user.email,
    type : user.type,
    regionId : user.regionId
  }, require('../config/secret')());
 
  return {
    token: token,
    expires: expires,
    user: user
  };

}
 
function expiresIn(numDays) {
  var dateObj = new Date();
  return dateObj.setDate(dateObj.getDate() + numDays);
}
 
module.exports = auth;