module.exports = function(model){
	var express = require('express');
	var router = express.Router();

	var db = require('../config/mysql.js')();

	var auth = require('./auth.js');

	var user = require('./v1/user')(model, db);
	{{#composition}}
	var {{id}} = require('./v1/{{id}}/{{id}}')(model);
	{{/composition}}

	/*
	 * Routes that can be accessed by anyone
	 */

		router.post('/login', auth.login);

		{{#composition}}
		// User
		router.get('/api/v1/{{pluralId}}', {{id}}.getAll);
		router.post('/api/v1/{{pluralId}}', {{id}}.create);
		router.get('/api/v1/{{pluralId}}/:id', {{id}}.getOne);
		router.put('/api/v1/{{pluralId}}/:id', {{id}}.update);
		router.delete('/api/v1/{{pluralId}}/:id', {{id}}.delete);		
		{{/composition}}



	return router;		
};