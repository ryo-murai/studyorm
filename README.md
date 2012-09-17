studyorm
========
just comparing some Object-Relational-Mapping Tools and Libraries in Java.

## libraries used
- Java Persistence API
- Spring DATA JPA
- QueryDsl(JPA)
- QueryDsl(SQL)
- Spring DATA JDBC Extension

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

## source codes using ORM
- to be described

## micro benchmarking
- using [junit-benchmarks](http://labs.carrotsearch.com/junit-benchmarks.html)
- HSQLDB in-memory database
- 2,000 records of Customer and 4,000 records of Order.
- Visualized results can be referred in this [link](https://raw.github.com/gist/bd9473d242493c574771/5f336d76ad96dcceb51d7fb965546ccbdda1f88f/BenchmarkTest.html)
### execution environment
- Windows 7 64bit
- Celeron Dual-Core SU2300 1.2GHz/2Cores
- RAM 4GB
- JDK 7

## TODO
- Add "Spring Data JDBC"
- Use file database to do benchmarking with more large amount of data records
- compare more complex operations