// Dependencies
var ncp = require('ncp').ncp;
var fs = require('fs');

module.exports = function(recipe) {
  // NIGREDO
  // A blackening or melanosis - Take the prima materia and cook into a uniform black matter.  Get the json model into a variable, and copy seed project into target.

  return function(){
    return new Promise(function(resolve, reject){
      var location = process.cwd();
      var source = __dirname;

      // Get JSONs
      var lead = require(location + "/" + recipe.lead);

      recipe.gold = lead.name || "gold-"+recipe.circle;

      // Copy seed project
      if (!fs.existsSync(recipe.gold)){
          fs.mkdirSync(recipe.gold);
      };

      if (fs.existsSync(source+'/circles/'+recipe.circle)){
        try{
          var blueprint = require(source+'/circles/'+recipe.circle+'/blueprint.json');
        }catch(err){
          throw 'Provided circle has no blueprint '+err.message;
        }
        if (fs.existsSync(source+'/circles/'+recipe.circle+'/src')){
          ncp(source+'/circles/'+recipe.circle+'/src', location+'/'+recipe.gold, function (err) {
           if (err) {
             return console.error(err);
           };
           resolve({ location : location, source : source, lead : lead, gold : recipe.gold, circle : { name : recipe.circle, blueprint : blueprint } })
          });
        }else{
          throw 'Provided circle does not have proper src folder'
        };
      }else{
        throw 'Provided circle does not exist.';
      };


    })
  };
};