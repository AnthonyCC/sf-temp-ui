*Project setup*

* java 6
* maven 3

*How to run project locally*
<pre><code>mvn clean tomcat7:run -Dspring.profiles.active=developer</code></pre>

*How to generate API documentation*
<pre><code>mvn clean test</code></pre>

*The generated index.html can be found*
target/miredot/index.html

*How to generate schema create/drop SQL scripts under target/generated-schema folder*
<pre><code>mvn clean test -Pschema -Dspring.profiles.active=schema</code></pre>

*How to run all unit test cases*
<pre><code>mvn clean test -Punittest</code></pre>

*How to run all rest test cases*
<pre><code>mvn clean test -Presttest</code></pre>

*How to run all test cases*
<pre><code>mvn clean test</code></pre>
