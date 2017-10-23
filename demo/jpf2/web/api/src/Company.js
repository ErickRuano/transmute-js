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