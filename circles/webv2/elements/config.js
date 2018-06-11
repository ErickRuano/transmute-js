app.factory('apiConfig', function() {
    return {
        host : "http://{{configuration.remote}}:{{configuration.port}}/api/v1",
        apiv : 'v1'
    };
});
