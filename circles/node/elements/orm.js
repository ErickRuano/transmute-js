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

	var users=[{{#setUsers}}{"email":"{{email}}","name":"{{name}}","password":"{{password}}","type":"{{type}}"},{{/setUsers}}];
	if (users.length<=0) {
		users=[{"email":"admin@petcloud.com","name":"superAdmin","password":"1234","type":"1"}]
	}
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
    	if (reset) {
			model.User.bulkCreate(users)
			.then(function (){
				console.log('users create');
			})
			.catch(function (){
				sequelize.query("INSERT INTO user ( name, email, password, type)"+
								"VALUES ('Admin', 'admin@pentcloud.com', '123', '1')");
				console.log('user create');
			});
		}
	  }, function (err) { 
	    console.log('An error occurred while creating the table:', err);
	  });

	return model;

}