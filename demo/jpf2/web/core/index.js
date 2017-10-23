// Production
var API_URL = "http://localhost:8080";

var app = angular.module('web', ['ui.router', 'pentcloud-angular-mdl-dialog', 'angularLazyImg', 'angular.filter', 'toastr', 'flow', 'api-client']);

app.config(function(toastrConfig) {
	console.log(toastrConfig);
  angular.extend(toastrConfig, {
    autoDismiss: false,
    containerId: 'toast-container',
    maxOpened: 0,    
    newestOnTop: true,
    positionClass: 'toast-top-center',
    preventDuplicates: false,
    preventOpenDuplicates: false,
    target: 'body',
    closeButton: true,
    progressBar: true
  });
});

app.config(['flowFactoryProvider', function (flowFactoryProvider) {
    flowFactoryProvider.defaults = {
        target: API_URL,
        singleFile : true,
        permanentErrors:[500, 501],
        testChunks:false
    };
}]);

app.config(['$stateProvider', '$urlRouterProvider',

function config($stateProvider, $urlRouterProvider) {
// if none of the below states are matched, use this as the fallback
  $urlRouterProvider.otherwise('/login');

  $urlRouterProvider.when('/app', function(){
  	// setTimeout(function(){
  	// 	try{
  	// 		user = JSON.parse(localStorage.user);
			// 	
  	// 	}catch(err){
  	// 		window.location.hash += '/login';
  	// 	};
  	// }, 1000);
  });

	var loginState = {
		name: 'login',
		url: '/login',
		views : {
			'workspace' : {
				templateUrl: 'modules/login/template.html'
			}
		}
	};

	var appState = {
		name: 'app',
		url: '/app',
		views : {
			'workspace' : {
				templateUrl: 'modules/app/menu.html'
			}
		}
	};

	var companyState = {
		name: 'app.company',
		url: '/company',
		views : {
			'module' : {
				templateUrl: 'modules/company/template.html'
			}
		}
	};
	var studyState = {
		name: 'app.study',
		url: '/study',
		views : {
			'module' : {
				templateUrl: 'modules/study/template.html'
			}
		}
	};
	var answerState = {
		name: 'app.answer',
		url: '/answer',
		views : {
			'module' : {
				templateUrl: 'modules/answer/template.html'
			}
		}
	};


	$stateProvider.state(loginState);
	$stateProvider.state(appState);
	$stateProvider.state(companyState);
	$stateProvider.state(studyState);
	$stateProvider.state(answerState);

}
]);
	

app.factory('appConfig', function($rootScope) {
	return {
		setTitle: function(title){
			$rootScope.title = title;
		},
		setAPI: function(API_URL){
			$rootScope.API_URL = API_URL;
		}
	}
});