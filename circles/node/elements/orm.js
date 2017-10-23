module.exports = function(Sequelize){

	// Connection to database
	var sequelize = new Sequelize('{{configuration.db}}', '{{configuration.dbUser}}', '{{configuration.dbPassword}}', {
	  host: '{{configuration.dbHost}}',
	  dialect: 'mysql',
	  port: {{configuration.dbPort}},
	  pool: {
	    max: 5,
	    min: 0,
	    idle: 10000
	  },
	});

	// Models definition
	var model = {};
	model.sequelize = Sequelize;

	// http://docs.sequelizejs.com/en/latest/docs/models-definition/#definition
	// http://docs.sequelizejs.com/en/latest/docs/models-definition/#data-types
	// http://docs.sequelizejs.com/en/latest/docs/associations/


	model.User = sequelize.define('user', {
		type : Sequelize.INTEGER,
		name : Sequelize.STRING,
		password : Sequelize.STRING,
		email : Sequelize.STRING
	},
	{
		freezeTableName: true,
	}
	);

	{{#composition}}	
	model.{{capitalizedId}} = sequelize.define('{{lowerId}}', {
		{{#fields}}
		{{name}} : Sequelize.{{type}}{{#unless @last}},{{/unless}}
		{{/fields}}
	},
	{
		freezeTableName: true,
	}
	);
	{{/composition}}	


	

    // Relations
    {{#composition}}
    {{#parents}}
    	model.{{../capitalizedId}}.belongsTo(model.{{capitalizedId}});
    {{/parents}}
    {{/composition}}



	// Sync 
	var reset = false;
	sequelize
	  .sync({ force: reset })
	  .then(function(err) {
	    console.log('It worked!');
	    //DEFAULTS
	    if(reset){

	    }
	  }, function (err) { 
	    console.log('An error occurred while creating the table:', err);
	  });

	return model;

}