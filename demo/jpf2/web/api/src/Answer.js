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