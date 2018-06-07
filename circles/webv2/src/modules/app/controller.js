app.controller('AppController', function($timeout, appConfig, $scope, pentcloudAngularRequest, $state, $rootScope){
	
	// Set api url
	// appConfig.setAPI(API_URL);
	// Get local session
	try{
		$rootScope.user = JSON.parse(localStorage.user);
	}catch(err){
		pentcloudAngularRequest.db.remove('session');
		delete localStorage.user;
		$state.go('login');
	}
	

	// If session exists inject token to request class middlewares
	if(pentcloudAngularRequest.db.get('session')){
		pentcloudAngularRequest.addMiddleware(function(xhr){
			xhr.setRequestHeader("X-Access-Token", pentcloudAngularRequest.db.get('session').token);
			xhr.setRequestHeader("content-type", 'application/json');
		});
	}else{
		$state.go('login');
	}

	$scope.logout = function(){
		pentcloudAngularRequest.db.remove('session');
		delete localStorage.user;
		$state.go('login');
	}
});
