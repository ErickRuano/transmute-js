app.controller('companiesController', function(appConfig, $scope, toastr, API){

  	// Set module title
  	appConfig.setTitle("companies");
    // Flag to show/hide loading animation
    $scope.loading = true;
    // Get local session
    // $scope.user = JSON.parse(localStorage.user);
    $scope.user = {};
  	$scope.categories = []; // Categories collection

    // Temporal models for CRUD
    $scope.newCompany = {};
  	$scope.tmpCompany = {};
  	$scope.setTmpCompany = function(obj, idx){
  		clone = JSON.parse(JSON.stringify(obj));
  		$scope.tmpCompany = clone;
      $scope.idx = idx;
  		return clone;
  	}; 

    // Method to create a new
    $scope.addCompany = function(){
      var valid = true;
      // if(!$scope.newCategory.name ){ valid = false; };
      if(valid){
          $scope.addCompanyDialog.close();
        API.company.create($scope.newCompany).then(function(cat){
          // $scope.companies.push(cat);
          fetch();
           toastr.success("added");
        }, function(){
          
        });
      }else{
        toastr.error("Debes completar todos los campos");
      };
    };

    // Method to edit a existing 
  	$scope.editCompany = function(){
  		var valid = true;
  		// if(!$scope.tmpCategory.name){ valid = false; };
  		if(valid){
  			$scope.editCompanyDialog.close();
        API.company.update($scope.tmpCompany).then(function(){
          fetch();
  			   toastr.success("UPDATED");
        }, function(){
          
        });
  		}else{
  			toastr.error("Debes completar todos los campos");
  		};
  	};

    // Method to delete a existing 
  	$scope.deleteCompany = function(){
        API.company.delete($scope.tmpCompany).then(function(){
  		    toastr.success("DELETED");
          $scope.companies.splice($scope.idx, 1);
        }, function(){
          
        });
  	};

    // API call to populate main collection
    var fetch = function(){
      API.company.getAll().then(function(companies){
        $scope.companies = companies;
        $scope.$apply();
      });
    };
    fetch();

});