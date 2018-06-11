var gulp = require('gulp'),
gp_concat = require('gulp-concat'),
gp_rename = require('gulp-rename'),
gp_minify = require('gulp-minify');
gp_selfExecute = require('gulp-self-execute');
runSequence = require('run-sequence').use(gulp);
jsdoc = require('gulp-jsdoc3');

gulp.task('merge', function(){
    return gulp.src(["../src/index.js", "../src/api.js", "../src/config.js", {{#composition}}"../src/{{capitalizedName}}.js",{{/composition}} "../src/Login.js"])
        .pipe(gp_concat('apiclient.js'))
        .pipe(gulp.dest('../dist'))
});

gulp.task('doc', function(){
    var config = require('./config.json');
    return gulp.src(["../dist/apiclient.js"])
        .pipe(jsdoc(config))
});

gulp.task('wrap', function(){
    return gulp.src(["../dist/apiclient.js"])
        .pipe(gp_selfExecute({
            'window': 'window'
        }))
        .pipe(gulp.dest('../dist'))
        .pipe(gp_minify({
            mangle : false
        }))
        .pipe(gulp.dest('../dist'))
});

gulp.task('build', function(){
    runSequence('merge', 'doc', function(){
        console.log('Wraping in self executing anonymous function');
        setTimeout(function(){
            runSequence('wrap', function(){
                console.log('done!');
            });
        }, 2000);
    });
})

gulp.task('default', ['build'   ], function(){});
