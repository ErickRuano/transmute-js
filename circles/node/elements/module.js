module.exports = function(model){

  var template = {
   
   getAll: function(req, res) {

      var input = req.query;
      
      model.{{capitalizedId}}.findAll().then(function(data){
        res.send(data);
      });

    },
    getOne: function(req, res) {

      var input = req.query;
      input.id = req.params.id;
      model.{{capitalizedId}}.findById(input.id).then(function(data){
        res.send(data);
      });

    },
    create: function(req, res) {

      var input = req.body;
      
      model.{{capitalizedId}}.create(input).then(function(data){
        res.send(data);
      });

    },
    update: function(req, res) {

      var input = req.body;
      input.id = req.params.id;

      model.{{capitalizedId}}.update(input, { where: { id: input.id } }).then(function(data){
        res.send(data);
      });

    },
    delete: function(req, res) {

      var input = req.body;
      input.id = req.params.id;

      model.{{capitalizedId}}.destroy({ where: { id: input.id } }).then(function(data){
        res.send("deleted");
      });

    },
}
   
    
  return template;

};