app.factory('apiConfig', function() {
    return {
        host : "{{configuration.remote}}",
        apiv : 'v1'
    };
});
