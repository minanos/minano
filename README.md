# minano

Welcome to the home of minano. Here you will find all source code for the product.

## Building 

Build all code and run all tests including integration tests:

    mvn clean install

Build all code skipping integration tests:

    mvn -DskipITs clean install

Build all code skipping all tests:

    mvn -DskipTests clean install

Build all including javadoc and distribution (used for CI build):

    mvn -P all clean install
## Running
	mvn -o -DskipTests tomcat7:run -f runtime/pom.xml

## Signin with below account:
E-Mail: admin@minano.com
Password: admin888

## Validation
	curl -X GET "http://localhost:8080/api/users"
	open http://localhost:8080/?lang=zh_CN

## License

This software is licensed under AGPL 3.0 license. Also the distribution includes
3rd party software components. The vast majority of these libraries are licensed under Apache 2.0. 