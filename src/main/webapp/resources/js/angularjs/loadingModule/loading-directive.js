angular.module('loadingModule')
    .directive('loading', function($compile){
      

      
      return {
          restrict: 'E', //Default for 1.3+

          replace : true,
 //template: '<div class="alert alert-info" data-ng-show="loading">LOADING</div><table class="table"><thead><tr><th>Title</th><th>Date</th></tr></thead><tbody><tr data-ng-repeat="line in data"><td class="white">{{line.inspectionId}}</td><td class="white">{{line.inspectionDate | date:"dd/MM/yyyy"}}</td></tr></tbody></table>'
          templateUrl: '/turfclubPrograms/resources/js/angularjs/loadingModule/loading-view.html',
 
          
          
      };
    });