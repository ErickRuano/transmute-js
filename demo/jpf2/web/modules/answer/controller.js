app.controller('answersController', function(appConfig, $scope, toastr, API){

  	// Set module title
  	appConfig.setTitle("answers");
    // Flag to show/hide loading animation
    $scope.loading = true;
    // Get local session
    // $scope.user = JSON.parse(localStorage.user);
    $scope.user = {};
  	$scope.categories = []; // Categories collection

    // Temporal models for CRUD
    $scope.newAnswer = {};
  	$scope.tmpAnswer = {};
  	$scope.setTmpAnswer = function(obj, idx){
  		clone = JSON.parse(JSON.stringify(obj));
  		$scope.tmpAnswer = clone;
      $scope.idx = idx;
  		return clone;
  	}; 

    // Method to create a new
    $scope.addAnswer = function(){
      var valid = true;
      // if(!$scope.newCategory.name ){ valid = false; };
      if(valid){
          $scope.addAnswerDialog.close();
        API.answer.create($scope.newAnswer).then(function(cat){
          // $scope.answers.push(cat);
          fetch();
           toastr.success("added");
        }, function(){
          
        });
      }else{
        toastr.error("Debes completar todos los campos");
      };
    };

    // Method to edit a existing 
  	$scope.editAnswer = function(){
  		var valid = true;
  		// if(!$scope.tmpCategory.name){ valid = false; };
  		if(valid){
  			$scope.editAnswerDialog.close();
        API.answer.update($scope.tmpAnswer).then(function(){
          fetch();
  			   toastr.success("UPDATED");
        }, function(){
          
        });
  		}else{
  			toastr.error("Debes completar todos los campos");
  		};
  	};

    // Method to delete a existing 
  	$scope.deleteAnswer = function(){
        API.answer.delete($scope.tmpAnswer).then(function(){
  		    toastr.success("DELETED");
          $scope.answers.splice($scope.idx, 1);
        }, function(){
          
        });
  	};

    // API call to populate main collection
    var fetch = function(){
      API.answer.getAll().then(function(answers){
        $scope.answers = answers;
        $scope.$apply();
      });
    };
    fetch();

});