(function(window, undefined) {

var app = angular.module("api-client", ["pentcloud-angular-js-request"]);
app.factory('API', ['company', 'study', 'answer',  'login', function(company, study, answer,  login) {
  return {
		company : company,
		study : study,
		answer : answer,
		login : login
	};
}]);

app.factory('apiConfig', function() {
    return {
        host : "http://localhost:8080",
        apiv : 'v1'
    };
});

app.factory('company', function(apiConfig, pentcloudAngularRequest){
	return {
		getAll : function(input){
			return pentcloudAngularRequest.get(apiConfig.host+'/companies');
		},
		getOne : function(input){
			return pentcloudAngularRequest.get(apiConfig.host+'/companies/'+input.id);
		},
		create : function(input){
			return pentcloudAngularRequest.post(apiConfig.host+'/companies', input);
		},
		update : function(input){
			return pentcloudAngularRequest.put(apiConfig.host+'/companies/'+input.id, input);
		},
		delete : function(input){
			return pentcloudAngularRequest.delete(apiConfig.host+'/companies/'+input.id, input);
		}
	}
});
app.factory('study', function(apiConfig, pentcloudAngularRequest){
	return {
		getAll : function(input){
			return pentcloudAngularRequest.get(apiConfig.host+'/studies');
		},
		getOne : function(input){
			return pentcloudAngularRequest.get(apiConfig.host+'/studies/'+input.id);
		},
		create : function(input){
			return pentcloudAngularRequest.post(apiConfig.host+'/studies', input);
		},
		update : function(input){
			return pentcloudAngularRequest.put(apiConfig.host+'/studies/'+input.id, input);
		},
		delete : function(input){
			return pentcloudAngularRequest.delete(apiConfig.host+'/studies/'+input.id, input);
		}
	}
});
app.factory('answer', function(apiConfig, pentcloudAngularRequest){
	return {
		getAll : function(input){
			return pentcloudAngularRequest.get(apiConfig.host+'/answers');
		},
		getOne : function(input){
			return pentcloudAngularRequest.get(apiConfig.host+'/answers/'+input.id);
		},
		create : function(input){
			return pentcloudAngularRequest.post(apiConfig.host+'/answers', input);
		},
		update : function(input){
			return pentcloudAngularRequest.put(apiConfig.host+'/answers/'+input.id, input);
		},
		delete : function(input){
			return pentcloudAngularRequest.delete(apiConfig.host+'/answers/'+input.id, input);
		}
	}
});
app.factory('login', function(apiConfig, pentcloudAngularRequest){
	return {
		login : function(input){
			return pentcloudAngularRequest.post(apiConfig.host+'/login', input);
		},
	}
});

}(window));