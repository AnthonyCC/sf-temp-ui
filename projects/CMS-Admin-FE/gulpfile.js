var gulp = require('gulp'),
    gulpLoadPlugins = require('gulp-load-plugins'),
    autoprefixer = require('autoprefixer'),
    cssnext = require('cssnext'),
    mqpacker = require('css-mqpacker'),
    cssnano = require('cssnano'),
    mainBowerFiles = require('main-bower-files'),
    gulpif = require('gulp-if'),
    del = require('del'),
    path = require('path'),
    browserSync = require('browser-sync'),
    proxy = require('proxy-middleware'),
    url = require('url'),
    reload = browserSync.reload,
    Karma = require('karma').Server;

var DEBUG = false;

var plugins = gulpLoadPlugins();

var build = function (subpath) {
  return !subpath ? 'build' : path.join('build', subpath);
};

function test_unit(done) {
  new Karma({
    configFile: __dirname + '/karma.conf.js',
    singleRun: true
  }, done).start();
}

gulp.task('clean:all', function() {
  return del([build('**/*')]);
});

gulp.task('clean:js', function() {
  return del([build('js/app.js')]);
});

gulp.task('clean:jsModules', function() {
  return del([build('js/modules/*.js')]);
});

gulp.task('clean:libs', function() {
  return del([build('js/libs.js'), build('components')]);
});

gulp.task('clean:css', function() {
  return del([build('css/style.css')]);
});

gulp.task('clean:externalCss', function() {
  return del([build('css/external.css')]);
});

gulp.task('clean:fonts', function() {
  return del([build('css/fonts/**/*')]);
});

gulp.task('clean:html', function() {
  return del([build('**/*.html'), '!'+build('components/**/*')]);
});

gulp.task('clean:assets', function() {
  return del([build('assets/**/*')]);
});

gulp.task('lint', function () {
  return gulp.src('src/js/**/*.js')
    .pipe(plugins.eslint())
    .pipe(plugins.eslint.format())
    .pipe(plugins.eslint.failOnError());
});

gulp.task('lint:html', function () {
  return gulp.src('src/elements/**/*.html')
    .pipe(plugins.eslint())
    .pipe(plugins.eslint.format())
    .pipe(plugins.eslint.failOnError());
});

gulp.task('js', ['clean:js'], function () {
  var res = gulp.src(['src/js/!(modules)**/*.js', 'src/js/*.js'])
    .pipe(plugins.sourcemaps.init())
    .pipe(plugins.babel())
    .pipe(gulpif(!DEBUG, plugins.uglify()))
    .pipe(plugins.concat('app.js'))
    .pipe(plugins.sourcemaps.write('.'))
    .pipe(gulp.dest(build('js')));

  return res;
});

gulp.task('jsModules', ['clean:jsModules'], plugins.folders('src/js/modules', function (module) {
  return gulp.src(path.join('src/js/modules', module, '*.js'))
    .pipe(plugins.sourcemaps.init())
    .pipe(plugins.babel())
    .pipe(gulpif(!DEBUG, plugins.uglify()))
    .pipe(plugins.concat(module + '.js'))
    .pipe(plugins.sourcemaps.write('.'))
    .pipe(gulp.dest(build('js/modules')));
  })
);

gulp.task('css', ['clean:css'], function () {
  var processors = [
    autoprefixer({browsers: ['last 2 version', 'ie 9']}),
    cssnext(),
    mqpacker(),
    cssnano()
  ];

  return gulp.src('src/css/**/*.css')
    .pipe(plugins.sourcemaps.init())
    .pipe(plugins.postcss(processors))
    .pipe(plugins.concat('style.css'))
    .pipe(plugins.sourcemaps.write('.'))
    .pipe(gulp.dest(build('css')));
});

gulp.task('fonts', ['clean:fonts'], function () {
  return gulp.src('src/css/fonts/**/*')
    .pipe(gulp.dest(build('css/fonts')));
});

gulp.task('components', ['clean:libs'], function () {
  return gulp.src(['bower_components/**/*'])
    .pipe(gulp.dest(build('components')));
});

gulp.task('libs', ['clean:libs', 'components'], function () {
  return gulp.src(mainBowerFiles('**/*.js'))
    .pipe(plugins.sourcemaps.init())
    .pipe(plugins.uglify())
    .pipe(plugins.concat('libs.js'))
    .pipe(plugins.sourcemaps.write('.'))
    .pipe(gulp.dest(build('js')));
});

gulp.task('externalCss', ['clean:externalCss'], function () {
  return gulp.src(mainBowerFiles('**/*.css'))
    .pipe(plugins.sourcemaps.init())
    .pipe(plugins.minifyCss())
    .pipe(plugins.concat('external.css'))
    .pipe(plugins.sourcemaps.write('.'))
    .pipe(gulp.dest(build('css')));
});

gulp.task('html:copy', ['clean:html'], function () {
  return gulp.src('src/**/*.html')
    .pipe(gulp.dest(build()));
});

gulp.task('html:vulcanize', ['lint:html', 'html:copy', 'libs', 'components', 'externalCss', 'css', 'js', 'jsModules'], function () {
  return gulp.src([build('**/*.html'), '!'+build('components/**/*')])
    .pipe(plugins.vulcanize({
      abspath: '',
      inlineScripts: false,
      inlineCss: false,
      stripComments: false
    }))
    .pipe(gulp.dest(build()));
});

gulp.task('html', ['clean:html', 'html:copy', 'html:vulcanize']);

gulp.task('html:vulcanize:light', ['lint:html', 'html:copy'], function () {
  return gulp.src([build('**/*.html'), '!'+build('components/**/*')])
    .pipe(plugins.vulcanize({
      abspath: '',
      inlineScripts: false,
      inlineCss: false,
      stripComments: false
    }))
    .pipe(gulp.dest(build()));
});

gulp.task('html:light', ['clean:html', 'html:copy', 'html:vulcanize:light']);

gulp.task('assets', ['clean:assets'], function () {
  return gulp.src('src/assets/**/*')
    .pipe(gulp.dest(build('assets')));
});

gulp.task('build', ['js', 'jsModules', 'libs', 'fonts', 'css', 'externalCss', 'html', 'assets']);

gulp.task('default', ['lint', 'build'], function () {
});

gulp.task('vulcanize', ['build'], function () {
  return gulp.src([build('**/*.html'), '!'+build('components/**/*')])
    .pipe(plugins.vulcanize({
      abspath: '',
      inlineScripts: true,
      inlineCss: true,
      stripComments: true
    }))
    .pipe(plugins.rename({
      extname: '.vul.html'
    }))
    .pipe(gulp.dest(build()));
});

// web component tester
require('web-component-tester').gulp.init(gulp);

gulp.task('serve', ['default'], function () {
  var mockProxyOptions = url.parse('http://localhost:3000/cmsapi'),
      proxyOptions = url.parse('http://localhost:8080/cmsadmin');

  mockProxyOptions.route = '/cmsapi';
  proxyOptions.route = '/cmsadmin';

  browserSync({
    port: 5000,
    notify: false,
    logPrefix: 'EFE',
    snippetOptions: {
      rule: {
        match: '<span id="browser-sync-binding"></span>',
        fn: function(snippet) {
          return snippet;
        }
      }
    },
    server: {
      baseDir: [build()],
      middleware: [
        proxy(mockProxyOptions),
        proxy(proxyOptions)
      ]
    }
  });

  DEBUG = true;
  gulp.watch('src/js/**/*', ['jsModules', 'js', reload]);
  gulp.watch('src/css/**/*', ['css', reload]);
  gulp.watch('src/css/fonts/**/*', ['fonts', reload]);
  gulp.watch('src/assets/**/*', ['assets', reload]);
  gulp.watch('src/**/*.html', ['html:light', reload]);
  gulp.watch('bower_components/**/*', ['libs', 'externalCss', reload]);
});
