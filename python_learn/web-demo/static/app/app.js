angular.module("myApp", ["ngTable", "ngResource"]);

(function() {
  "use strict";

  angular.module("myApp").controller("demoController", function ($scope, $http, NgTableParams) {
    var self = this;
    
    $http.get("data1.txt")
    .success(function(response) {
        self.tableParams = new NgTableParams({
            sorting: { title: "asc" }
        }, {
          dataset: response
        });
    });
  });
})();

/*
(function() {
  "use strict";

  angular.module("myApp").run(setRunPhaseDefaults);
  setRunPhaseDefaults.$inject = ["ngTableDefaults"];

  function setRunPhaseDefaults(ngTableDefaults) {
    ngTableDefaults.params.count = 10;
    ngTableDefaults.settings.counts = [];
  }
})();*/