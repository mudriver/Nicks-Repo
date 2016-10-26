angular.module("employee-history-app", [])
.filter('subName', function() {

    return function(stringValue) {
        return stringValue.substring(0,3);
    };
});