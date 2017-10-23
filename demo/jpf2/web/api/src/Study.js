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