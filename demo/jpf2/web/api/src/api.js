app.factory('API', ['company', 'study', 'answer',  'login', function(company, study, answer,  login) {
  return {
		company : company,
		study : study,
		answer : answer,
		login : login
	};
}]);
