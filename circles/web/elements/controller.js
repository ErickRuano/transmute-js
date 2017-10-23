app.controller('{{pluralId}}Controller', function(appConfig, $scope, toastr, API){

  	// Set module title
  	appConfig.setTitle("{{pluralId}}");
    // Flag to show/hide loading animation
    $scope.loading = true;
    // Get local session
    // $scope.user = JSON.parse(localStorage.user);
    $scope.user = {};
  	$scope.categories = []; // Categories collection

    // Temporal models for CRUD
    $scope.new{{capitalizedId}} = {};
  	$scope.tmp{{capitalizedId}} = {};
  	$scope.setTmp{{capitalizedId}} = function(obj, idx){
  		clone = JSON.parse(JSON.stringify(obj));
  		$scope.tmp{{capitalizedId}} = clone;
      $scope.idx = idx;
  		return clone;
  	}; 

    // Method to create a new
    $scope.add{{capitalizedId}} = function(){
      var valid = true;
      // if(!$scope.newCategory.name ){ valid = false; };
      if(valid){
          $scope.add{{capitalizedId}}Dialog.close();
        API.{{lowerId}}.create($scope.new{{capitalizedId}}).then(function(cat){
          // $scope.{{pluralId}}.push(cat);
          fetch();
           toastr.success("added");
        }, function(){
          
        });
      }else{
        toastr.error("Debes completar todos los campos");
      };
    };

    // Method to edit a existing 
  	$scope.edit{{capitalizedId}} = function(){
  		var valid = true;
  		// if(!$scope.tmpCategory.name){ valid = false; };
  		if(valid){
  			$scope.edit{{capitalizedId}}Dialog.close();
        API.{{lowerId}}.update($scope.tmp{{capitalizedId}}).then(function(){
          fetch();
  			   toastr.success("UPDATED");
        }, function(){
          
        });
  		}else{
  			toastr.error("Debes completar todos los campos");
  		};
  	};

    // Method to delete a existing 
  	$scope.delete{{capitalizedId}} = function(){
        API.{{lowerId}}.delete($scope.tmp{{capitalizedId}}).then(function(){
  		    toastr.success("DELETED");
          $scope.{{pluralId}}.splice($scope.idx, 1);
        }, function(){
          
        });
  	};

    // API call to populate main collection
    var fetch = function(){
      API.{{lowerId}}.getAll().then(function({{pluralId}}){
        $scope.{{pluralId}} = {{pluralId}};
        $scope.$apply();
      });
    };
    fetch();

});