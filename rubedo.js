var exec = require('child_process').exec;
var Handlebars = require('handlebars');

module.exports = function(gold) {
  // RUBEDO
  // A reddening, purpling or iosis - You have succeeded, now do any cleanup or preparations you need.  Compile, fetch dependencies or execute any other custom shell commands.

  

  return new Promise(function(resolve, reject){
      var i = 0;

      var execute = function(i){
        if(gold.circle.blueprint.commands && gold.circle.blueprint.commands[i]){
          var cmd = gold.circle.blueprint.commands[i];

          var parser = Handlebars.compile(cmd);
          var cmd = parser(gold);

          console.log('Executing:  '+cmd);
          exec(cmd, function(error, stdout, stderr) {
            i++;
            execute(i);
          });
        };
      };

      execute(i);

        
  });

};