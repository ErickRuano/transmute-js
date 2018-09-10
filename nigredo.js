// Dependencies
var ncp = require('ncp').ncp;
var fs = require('fs');
var exec = require('child_process').exec;

module.exports = function(recipe) {
  // NIGREDO
  // A blackening or melanosis - Take the prima materia and cook into a uniform black matter.  Get the json model into a variable, and copy seed project into target.

  // Circles
  var circles = {
    web : "https://git.pentcloud.com/pentcloud/transmute-circle-web-ngjs"
  };

  return function(){
    return new Promise(function(resolve, reject){
      var location = process.cwd();
      var source = ".";

      // Get JSONs
      var lead = require(location + "/" + recipe.lead);

      recipe.gold = lead.name || "gold-"+recipe.circle;

      // Copy seed project
      var copySeedProject = function(){

        if (!fs.existsSync(recipe.gold)){
            fs.mkdirSync(recipe.gold);
        };

        if (fs.existsSync(source+'/circles/'+recipe.circle)){
          try{
            // var blueprint = require(source+'/circles/'+recipe.circle+'/blueprint.json');
            var blueprint = JSON.parse(fs.readFileSync(source+'/circles/'+recipe.circle+'/blueprint.json', 'utf8'));
          }catch(err){
            throw 'Provided circle has no blueprint '+err.message;
          }

          if (fs.existsSync(source+'/circles/'+recipe.circle+'/src')){
            ncp(source+'/circles/'+recipe.circle+'/src', location+'/'+recipe.gold, function (err) {
             if (err) {
               return console.error(err);
             };
             // Delete circles folder
            // NIGREDO ENDS
            resolve({ location : location, source : source, lead : lead, gold : recipe.gold, circle : { name : recipe.circle, blueprint : blueprint } });
            });
          }else{
            throw 'Provided circle does not have proper src folder: '+source+'/circles/'+recipe.circle+'/src'
          };
        }else{
          throw 'Provided circle does not exist: '+source+'/circles/'+recipe.circle;
        };
      };

      // check if circle provided is not a remote repository
      if(!recipe.circle.includes("http")){
        // its not a remote repository, search on transmute circles registry
        if(circles[recipe.circle]){
          // registry found, use value as provided circle
          recipe.repository = circles[recipe.circle];
        }else{
          throw 'Provided circle does not exist';
        };
      }else{
        // It is a remote repository
        recipe.repository = recipe.circle;
      };

      // Get safe circle name 
      recipe.circle = recipe.repository.split('/')[recipe.repository.split('/').length-1];

      if (!fs.existsSync('circles')){
            fs.mkdirSync('circles');
        };        
        exec('git clone '+recipe.repository+' circles/'+recipe.circle, function(error, stdout, stderr) {
          console.log(error, stdout, stderr);
          copySeedProject();
        });

      



    })
  };
};