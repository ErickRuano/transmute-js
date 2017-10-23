module.exports = function(model, db){

  var template = {
   
   getAll: function(req, res) {

      var input = req.query;
      model.User.findAll({ include : [{ model : model.Region, attributes : ["name", "budget"] }] }).then(function(data){
        res.send(data);
      });

    },
    getOne: function(req, res) {

      var input = req.query;
      input.id = req.params.id;
      model.User.findById(input.id).then(function(data){
        if(data.dataValues.type == 1){
          var budgetQuery = "select (REG.budget - IFNULL(SUM(ORM.value),0)) budget from cbc.OrderMaterial ORM JOIN cbc.`order` ORD ON ORM.orderId = ORD.id JOIN agency AGE ON AGE.id = ORD.agencyId JOIN region REG ON REG.id = AGE.regionId WHERE AGE.regionId = ? AND YEAR(ORD.createdAt) = YEAR(CURRENT_DATE()) AND MONTH(ORD.createdAt) = MONTH(CURRENT_DATE());"
          var params = [data.dataValues.regionId];
          db.execute(budgetQuery, params, function(budgetData){
            data.dataValues.budget = { current : budgetData[0].budget, total : budgetData[0].total };
            res.send(data);
          });
        }else{
          res.send(data);
        }


      });

    },
    create: function(req, res) {

      var input = req.body;
      
      model.User.create(input).then(function(data){
        res.send(data);
      });

    },
    update: function(req, res) {

      var input = req.body;
      input.id = req.params.id;

      model.User.update(input, { where: { id: input.id } }).then(function(data){
        res.send(data);
      });

    },
    delete: function(req, res) {

      var input = req.body;
      input.id = req.params.id;

      model.User.destroy({ where: { id: input.id } }).then(function(data){
        res.send("deleted");
      });

    },
}
   
    
  return template;

};