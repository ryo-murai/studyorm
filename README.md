studyorm
========
just comparing some Object-Relational-Mapping Tools and Libraries in Java.

## libraries used
- [Java Persistence API 2.0](http://jcp.org/en/jsr/detail?id=317)
- [Spring DATA JPA](http://www.springsource.org/spring-data/jpa)
- [QueryDsl(JPA)](http://www.querydsl.com/)
- [QueryDsl(SQL)](http://www.querydsl.com/)
- [Spring JDBC](http://static.springsource.org/spring/docs/current/spring-framework-reference/html/jdbc.html)
- [Spring DATA JDBC Extension](http://www.springsource.org/spring-data/jdbc-extensions)

## database schema
- see [create-tables.sql](https://github.com/ryo-murai/studyorm/blob/master/src/main/resources/sql/create-tables.sql)

## data manipulation operations
- insert
- delete
- batch delete
- update
- batch update
- query
- joined query

## example source codes using ORMs
- [Java Persistence API 2.0](https://github.com/ryo-murai/studyorm/blob/master/src/main/java/studyorm/jpa/JpaOperations.java)
- [Spring DATA JPA](https://github.com/ryo-murai/studyorm/blob/master/src/main/java/studyorm/jpa/springdatajpa/SpringDataJpaOperations.java)
- [QueryDsl(JPA)](https://github.com/ryo-murai/studyorm/blob/master/src/main/java/studyorm/jpa/querydsl/QueryDslJpaOperations.java)
- [QueryDsl(SQL)](https://github.com/ryo-murai/studyorm/blob/master/src/main/java/studyorm/querydslsql/QueryDslSqlOperations.java)
- [Spring JDBC](https://github.com/ryo-murai/studyorm/blob/master/src/main/java/studyorm/springjdbc/SpringJdbcOperations.java)
- [Spring DATA JDBC Extension](https://github.com/ryo-murai/studyorm/blob/master/src/main/java/studyorm/querydslsql/springdataext/QueryDslTemplateOperations.java)

## micro benchmarking
- recorded and visualized using [junit-benchmarks](http://labs.carrotsearch.com/junit-benchmarks.html)
- see this [gist](https://gist.github.com/bd9473d242493c574771)

## TODO
- compare more data manipulation operations like large objects, etc...
