// { location : location, source : source, lead : lead, gold : recipe.gold, circle : { name : , blueprint :  } }
module.exports = function(massa_confusa) {
  // ALBEDO
  // A whitening or leucosis - Take the generated massa confusa and create a white, purificated mass.  Check and attempt to clean any errors in model or circle.
  
  return new Promise(function(resolve, reject){
    // Check model
    var checkBlueprint = function(blueprint){
    	return new Promise(function(resolve, reject){
    		try{
    			if(!blueprint.requires){
		    		console.log('No requirements specified');
			    }else{
			    	for(i = 0; i < !blueprint.requires.length; i++){
			    		console.log(blueprint.requires[i]);
			    	};
			    }
    		}catch(err){

    		}
    	});
    };

    var diversify = function(composite){ // Diversifies properties and formats, i.e. id splits into id, capitalizedId, upperId, lowerId.  Also, fields acquire properties like isString
    	return new Promise(function(resolve, reject){
    		try{
		  //   	// Name
		    	composite.name ? composite.capitalizedName = composite.name.charAt(0).toUpperCase() + composite.name.toLowerCase().slice(1) : false;
		    	composite.name ? composite.lowerName = composite.name.toLowerCase() : false;
		    	composite.name ? composite.upperName = composite.name.toUpperCase() : false;
		  //   	// Id
		    	composite.id ? composite.capitalizedId = composite.id.charAt(0).toUpperCase() + composite.id.toLowerCase().slice(1) : false;
		    	composite.id ? composite.lowerId = composite.id.toLowerCase() : false;
		    	composite.id ? composite.upperId = composite.id.toUpperCase() : false;
		  //   	// Type
		    	composite.type ? composite.capitalizedType = composite.type.charAt(0).toUpperCase() + composite.type.toLowerCase().slice(1) : false;
		    	composite.type ? composite.lowerType = composite.type.toLowerCase() : false;
		    	composite.type ? composite.upperType = composite.type.toUpperCase() : false;
		  //   	// Plural
		    	composite.pluralId ? composite.capitalizedPluralId = composite.pluralId.charAt(0).toUpperCase() + composite.pluralId.toLowerCase().slice(1) : false;
		    	composite.pluralId ? composite.lowerPluralId = composite.pluralId.toLowerCase() : false;
		    	composite.pluralId ? composite.upperPluralId = composite.pluralId.toUpperCase() : false;
		  //   	Type Booleans
		    	switch (composite.lowerType){
		    		case 'string':
		    			composite.isString = true;
		    			composite.isInt = false;
		    			break;
		    		case 'integer':
		    			composite.isInt = true;
		    			composite.isString = false;
		                break;
		    	};
		    	resolve();
    		}catch(err){
    			throw(err);
    			reject(err);
    		}
    	});
    };
    
    var processComposite = function(iteration){
	    var composite = massa_confusa.lead.composition[iteration];

	    diversify(composite).then(function(){
	    	var processField = function(composite, fieldIteration){
	    		return new Promise(function(resolve, reject){
		    		var field = composite.fields[fieldIteration];
		    		diversify(field).then(function(){
		    			if((composite.fields.length - 1) > fieldIteration){
		    				processField(composite, fieldIteration + 1).then(function(){
		    					if((massa_confusa.lead.composition.length - 1 ) > iteration){
						    		processComposite(iteration + 1);
						    	}else{
						    		processBond(0);
						    	};
		    				})
		    			}else{
		    				resolve();
		    			}
		    		});
	    		});
	    	};

	    	if(composite.fields && composite.fields.length){
	    		processField(composite, 0).then(function(){
			    	if((massa_confusa.lead.composition.length - 1 ) > iteration){
			    		processComposite(iteration + 1);
			    	}else{
			    		processBond(0);
			    	};
	    		});
	    	}else{
			    processBond(0);
	    	};

	    });
    };

    checkBlueprint(massa_confusa.circle.blueprint).then(function(){
    	processComposite(0);
    });

    // Relationships
    var bind = function(bond){
    	return new Promise(function(resolve, reject){

	    	var firstPass = function(){

	    		for(i = 0; i < massa_confusa.lead.composition.length; i++){
	    			var composite = massa_confusa.lead.composition[i];
		    		switch (bond.relationship){
			    		case '-':
			    			// Poner parent y child
			    			// Child
			    			if(composite.id == bond.a){
				    			if(!composite.children){
				    				composite.children = [];
				    			};
			    				composite.children.push(bond.b)
			    			};
			    			// Parent
			    			if(composite.id == bond.b){
				    			if(!composite.parents){
				    				composite.parents = [];
				    			};
				    			composite.parents.push(bond.a);
			    			};
			    			break;
			    		case '=':
			    			// Poner peer
			                break;
			    	};
		    		if(!((massa_confusa.lead.composition.length - 1) > i )){
		    			secondPass();
		    			// console.log(massa_confusa.lead.composition);
		    			// resolve();
		    		};
	    		};
	    	};

	    	var secondPass = function(){
	    		var expandBind = function(composite, collection, composition){
	    			return new Promise(function(resolve, reject){
	    				for(i = 0; i < composite[collection].length; i ++){
	    					composite[collection][i] = composition[composite[collection][i]];
	    					if((composition[collection].length - 1) == i){
	    						resolve();
	    					}
	    				}
	    			});
	    		};

	    		var searchable = {};
	    		for(i = 0; i < massa_confusa.lead.composition.length; i++){
	    			searchable[massa_confusa.lead.composition[i].id] = massa_confusa.lead.composition[i];
	    			if((massa_confusa.lead.composition.length - 1) == i){ 
	    					for(ii = 0; ii < massa_confusa.lead.composition.length; ii++){
    							var composite = massa_confusa.lead.composition[ii];
    							if(composite.parents){
    								for(iii = 0; iii < composite.parents.length; iii ++){
    									if(typeof composite.parents[iii] != 'object'){
	    									var tmp = searchable[composite.parents[iii]];
	    									composite.parents[iii] = tmp;
	    								};
    									if((composite.parents.length - 1) == iii){
    										if(composite.children){
	    										for(iiii = 0; iiii < composite.children.length; iiii ++){
			    									if(typeof composite.children[iiii] != 'object'){
				    									var tmp = searchable[composite.children[iiii]];
				    									composite.children[iiii] = tmp;
			    									}
			    									if((composite.children.length - 1) == iiii){
			    										resolve();
			    									}
			    								}
    										}else{
    											resolve();
    										}
    									}
    								}
    							}else if(composite.children){
    								for(iii = 0; iii < composite.children.length; iii ++){
    									if(typeof composite.children[iii] != 'object'){
	    									var tmp = searchable[composite.children[iii]];
	    									composite.children[iii] = tmp;
    									}
    									if((composite.children.length - 1) == iii){
    										resolve();
    									}
    								}
    							}else{
    								resolve();
    							};
	    					}
	    				resolve()
	    			};

	    		};

	    	};

	    	firstPass();

    	});
    };

    var processBond = function(i){
    	// Get bond
    	var plainBond = massa_confusa.lead.bonds[i];
    	var bond = {};

    	// Check relationship type
    	if(plainBond.indexOf("-") != -1){
    		bond.relationship = "-";
    	};
    	if(plainBond.indexOf("=") != -1){
    		bond.relationship = "=";
    	};

    	// Separate composites
    	try{
	    	bond.a = plainBond.split(bond.relationship)[0];
	    	bond.b = plainBond.split(bond.relationship)[1];
    	}catch(err){
    		throw "Bond not declared properly";
    	};

    	// Execute
    	bind(bond).then(function(){
	    	if((massa_confusa.lead.bonds.length - 1) > i ){
	    		i++;
	    		processBond(i);
	    	}else{
	    		resolve(massa_confusa);	
	    	}
    	});

    };

    // Check circle

  });

};