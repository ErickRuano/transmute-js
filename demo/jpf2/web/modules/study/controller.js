app.controller('studiesController', function(appConfig, $scope, toastr, API){

  	// Set module title
  	appConfig.setTitle("studies");
    // Flag to show/hide loading animation
    $scope.loading = true;
    // Get local session
    // $scope.user = JSON.parse(localStorage.user);
    $scope.user = {};
  	$scope.categories = []; // Categories collection

    // Temporal models for CRUD
    $scope.newStudy = {};
  	$scope.tmpStudy = {};
  	$scope.setTmpStudy = function(obj, idx){
  		clone = JSON.parse(JSON.stringify(obj));
  		$scope.tmpStudy = clone;
      $scope.idx = idx;
  		return clone;
  	}; 

    // Method to create a new
    $scope.addStudy = function(){
      var valid = true;
      // if(!$scope.newCategory.name ){ valid = false; };
      if(valid){
          $scope.addStudyDialog.close();
        API.study.create($scope.newStudy).then(function(cat){
          // $scope.studies.push(cat);
          fetch();
           toastr.success("added");
        }, function(){
          
        });
      }else{
        toastr.error("Debes completar todos los campos");
      };
    };

    // Method to edit a existing 
  	$scope.editStudy = function(){
  		var valid = true;
  		// if(!$scope.tmpCategory.name){ valid = false; };
  		if(valid){
  			$scope.editStudyDialog.close();
        API.study.update($scope.tmpStudy).then(function(){
          fetch();
  			   toastr.success("UPDATED");
        }, function(){
          
        });
  		}else{
  			toastr.error("Debes completar todos los campos");
  		};
  	};

    // Method to delete a existing 
  	$scope.deleteStudy = function(){
        API.study.delete($scope.tmpStudy).then(function(){
  		    toastr.success("DELETED");
          $scope.studies.splice($scope.idx, 1);
        }, function(){
          
        });
  	};

    // API call to populate main collection
    var fetch = function(){
      API.study.getAll().then(function(studies){
        $scope.studies = studies;
        $scope.$apply();
      });
    };
    fetch();

});