app.controller('{{pluralId}}Controller', function(appConfig, $scope, toastr, API, $filter){
    // initialization components
    $('.dropdown-button').dropdown();
    $('.modal').modal();
    $('ul.tabs').tabs();
    $('.collapsible').collapsible();
  	// Set module title
  	appConfig.setTitle("{{pluralId}}");
    // Flag to show/hide loading animation
    $scope.loading = true;
    // Get local session
    // $scope.user = JSON.parse(localStorage.user);
    $scope.user = {};
  	$scope.categories = []; // Categories collection
    //pagination begin
    $scope.currentPage = 0;
    $scope.pageSize = 5;
    $scope.pages = [];
    $scope.showData = function () {
      if ($scope.searchtest!=="") {
        $scope.pageSize=$scope.{{pluralId}}.length;
      }else{
        $scope.pageSize=5;
      }
      $scope.configPages()
    }
    $scope.setPageSize = function (value) {
      $scope.pageSize=value;
      $scope.configPages()
    }
    $scope.configPages = function() {
      $scope.pages.length = 0;
      var ini = $scope.currentPage - 4;
      var fin = $scope.currentPage + 5;
      if (ini < 1) {
        ini = 1;
        if (Math.ceil($scope.{{pluralId}}.length / $scope.pageSize) > 10){
          fin = 10;
        }
        else
          fin = Math.ceil($scope.{{pluralId}}.length / $scope.pageSize);
      } else {
        if (ini >= Math.ceil($scope.{{pluralId}}.length / $scope.pageSize) - 10) {
          ini = Math.ceil($scope.{{pluralId}}.length / $scope.pageSize) - 10;
          fin = Math.ceil($scope.{{pluralId}}.length / $scope.pageSize);
        }
      }
      if (ini < 1){ ini = 1; }
      for (var i = ini; i <= fin; i++) {
        $scope.pages.push({
          no: i
        });
      }

      if ($scope.currentPage >= $scope.pages.length){
        $scope.currentPage = $scope.pages.length - 1;
      }
    };

    $scope.setPage = function(index) {
      $scope.currentPage = index - 1;
    };
    //pagination end
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
          $('#add{{capitalizedId}}Dialog').modal('close');
        API.{{lowerId}}.create($scope.new{{capitalizedId}}).then(function(cat){
          // $scope.{{pluralId}}.push(cat);
          fetch();
           toastr.success("{{capitalizedId}} agregado correctamente");
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
        API.{{lowerId}}.update($scope.tmp{{capitalizedId}}).then(function(){
          fetch();
           toastr.success("{{capitalizedId}} actualizado");
        }, function(){
          
        });
        $('#edit{{capitalizedId}}Dialog').modal('close');
  		}else{
  			toastr.error("Debes completar todos los campos");
  		};
  	};

    // Method to delete a existing 
  	$scope.delete{{capitalizedId}} = function(){
        API.{{lowerId}}.delete($scope.tmp{{capitalizedId}}).then(function(){
  		    toastr.success("{{capitalizedId}} eliminado");
          $scope.{{pluralId}}.splice($scope.idx, 1);
          $('#delete{{capitalizedId}}Dialog').modal('close');
        }, function(){
          
        });
  	};
    
    //open and close Dialogs;
    $scope.openDialog = function (dialog) {
      $('#'+dialog).modal('open');
    }
    $scope.closeDialog = function (dialog) {
      $('#'+dialog).modal('close');
    }

    // API call to populate main collection
    var fetch = function(){
      API.{{lowerId}}.getAll().then(function({{pluralId}}){
        $scope.{{pluralId}} = {{pluralId}};
        $scope.configPages()
        $scope.$apply();
        if ($scope.{{pluralId}}.status==='601') {
          window.location.hash += '/login';
        }
      });
    };
    fetch();

});