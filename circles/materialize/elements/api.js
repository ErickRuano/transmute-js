app.factory('API', [{{#composition}}'{{lowerName}}', {{/composition}} 'login', function({{#composition}}{{lowerName}}, {{/composition}} login) {
  return {
  	{{#composition}}
		{{lowerName}} : {{lowerName}},
  	{{/composition}}
		login : login
	};
}]);
