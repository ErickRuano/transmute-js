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
        res.send(data);
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