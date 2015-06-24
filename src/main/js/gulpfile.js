var gulp = require('gulp');
var clean = require('gulp-clean');
var concat = require('gulp-concat');
var uglify = require('gulp-uglify');
var usemin = require('gulp-usemin');
var htmlmin = require('gulp-html-minifier');
var rev = require('gulp-rev');
var karma = require('karma').server;

var pathToDistDir = '../../../build/webapp/static';

/********************** PACKAGE */
gulp.task('package', ['html', 'index.html']);

gulp.task('clean', function() {
    return gulp.src(pathToDistDir + '/*', {read: false})
        .pipe(clean({force: true}));
});

gulp.task('html', function () {
    return gulp.src(['app/**/*.html', '!app/index.html'])
        .pipe(htmlmin({collapseWhitespace: false, removeComments: true}))
        .pipe(gulp.dest(pathToDistDir));
});

gulp.task('index.html', function () {
    return gulp.src('app/index.html')
        .pipe(usemin({
            js: [uglify(), rev()],
            html: [htmlmin({collapseWhitespace: false, removeComments: true})]
        }))
        .pipe(gulp.dest(pathToDistDir));
});

/********************** TEST */
gulp.task('test', function (done) {
    karma.start({
        configFile: __dirname + '/karma.conf.js'
    }, done);
});

gulp.task('test-once', function (done) {
    karma.start({
        configFile: __dirname + '/karma.conf.js',
        singleRun: true
    }, done);
});