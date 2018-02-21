app.controller('loginController', function($scope, $rootScope, $state, API, appConfig, $http, pentcloudAngularRequest){
	// Set module title
	appConfig.setTitle('Login');
	// Set api url
	// appConfig.setAPI('{{configuration.host}}');

	// Model for user data
	$scope.user = {
		username : "",
		password : ""
	};

	// Method to attempt login
	$scope.login = function(){
		// API auth call
		// $http.post('{{configuration.remote}}/login', $scope.user).then(function(res){
		// 		localStorage.user = JSON.stringify(res.data.user);
		// 		pentcloudAngularRequest.db.set('session', res.data);
				$state.go('app');		
		// }, function(){
		// 	$scope.error = true;
		// });
	};

	if(pentcloudAngularRequest.db.get('session')){
		$state.go('app');
	};
});