// Dependencies
var fs = require('fs');
var mkdirp = require('mkdirp');
var Handlebars = require('handlebars');

// { location : location, source : source, lead : lead, gold : recipe.gold, circle : { name : , blueprint :  } }
module.exports = function(silver) {
  // Citrinitas
  // A yellowing or xanthosis - Transmute silver into gold.  Expose every template to model, execute directives and copy to target.
  var count = 0;
  return new Promise(function(resolve, reject){

    var bond = function(silver, stroke){
    	// 🝰
    	// Get file contents
    	var element = fs.readFileSync(silver.source+'/circles/'+silver.circle.name+'/elements/'+stroke, "utf8");

    	// Parse and replace directives
    	var template = Handlebars.compile(element);

    	var result = template(silver.lead);

    	// Copy to target
		mkdirp(silver.gold+'/'+silver.circle.blueprint.strokes[stroke].destination, function (err) {
		    if (err) console.error(err)
		    else fs.writeFile(silver.gold+'/'+silver.circle.blueprint.strokes[stroke].destination+'/'+stroke, result, function(){});
            count++;
            if(Object.keys(silver.circle.blueprint.strokes).length == count){
                resolve(silver);
            };
		});
    };

    var create = function(silver, stroke, replaceName){
    	// 🜹
    	// For each composite
    	var createFile = function(destination, stroke, result){
    		if(!fs.existsSync(destination)){
				mkdirp(destination, function (err) {
				    if(err){
                     console.error(err)
                    }else{
                        fs.writeFile(destination+'/'+stroke, result, function(){});
                        count++;
                        if(Object.keys(silver.circle.blueprint.strokes).length == count){
                            resolve(silver);
                        };
                    }
                        
				});
	    	}else{
	    		fs.writeFile(destination+'/'+stroke, result, function(){
                    count++;
                    if(Object.keys(silver.circle.blueprint.strokes).length == count){
                        resolve(silver);
                    }
                });
	    	}
    	};

        


        for(i = 0 ; i < silver.lead.composition.length; i++){

            var next = function(strokeName){
                // Get file contents
                var element = fs.readFileSync(silver.source+'/circles/'+silver.circle.name+'/elements/'+stroke, "utf8");

                // Parse and replace directives
                var template = Handlebars.compile(element);

                var result = template(silver.lead.composition[i]);

                var destination = silver.gold+'/'+silver.circle.blueprint.strokes[stroke].destination+'/'+silver.lead.composition[i].id;
                if(silver.circle.blueprint.strokes[stroke].isFile){
                    var destination = silver.gold+'/'+silver.circle.blueprint.strokes[stroke].destination;
                }
                // Copy to target
                createFile(destination, strokeName, result);
            }

            var strokeName = stroke;
            if(replaceName && silver.circle.blueprint.strokes[stroke].substitutes && silver.circle.blueprint.strokes[stroke].with){
                for(substitute in silver.circle.blueprint.strokes[stroke].substitutes){
                    strokeName = strokeName.replace(silver.circle.blueprint.strokes[stroke].substitutes[substitute], silver.lead.composition[i][silver.circle.blueprint.strokes[stroke].with]);
                    if(substitute == (silver.circle.blueprint.strokes[stroke].substitutes.length - 1)){
                        next(strokeName);
                    }
                };
            }else{
                next(stroke);
            }

        }
    

        

        
    }

    for(stroke in silver.circle.blueprint.strokes){
    	switch (silver.circle.blueprint.strokes[stroke].composition){
    		case '🜹':
    			create(silver, stroke);
    			break;
    		case '🝰':
                bond(silver, stroke);
                break;
            case '🜹🝰':
                create(silver, stroke, true);
                break;
    		default:
    			bond(silver, stroke);
    	};
    }
  });
  

};