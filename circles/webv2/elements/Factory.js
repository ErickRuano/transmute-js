app.factory('{{lowerName}}', function(apiConfig, pentcloudAngularRequest){
	return {
		getAll : function(input){
			return pentcloudAngularRequest.get(apiConfig.host+'/{{pluralId}}');
		},
		getOne : function(input){
			return pentcloudAngularRequest.get(apiConfig.host+'/{{pluralId}}/'+input.id);
		},
		create : function(input){
			return pentcloudAngularRequest.post(apiConfig.host+'/{{pluralId}}', input);
		},
		update : function(input){
			return pentcloudAngularRequest.put(apiConfig.host+'/{{pluralId}}/'+input.id, input);
		},
		delete : function(input){
			return pentcloudAngularRequest.delete(apiConfig.host+'/{{pluralId}}/'+input.id, input);
		}
	}
});