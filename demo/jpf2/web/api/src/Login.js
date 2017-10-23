app.factory('login', function(apiConfig, pentcloudAngularRequest){
	return {
		login : function(input){
			return pentcloudAngularRequest.post(apiConfig.host+'/login', input);
		},
	}
});
