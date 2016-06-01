# Introduction
Welcome to the example project that features testing with awesome [Cassandra Unit](https://github.com/jsevellec/cassandra-unit) tool.

The goal is to demonstrate some testing techniques using Cassandra Unit and a set of complementary frameworks.
Hopefully, this example may be helpful for developers who look forward to test a layer of code between their application and Cassandra database.

# Features
  - [DataStax Java Driver](https://github.com/datastax/java-driver) powered repository tested using pure Cassandra Unit
  - [Spring Data Cassandra](https://github.com/spring-projects/spring-data-cassandra) powered repository tested using Spring Test and Spring facilities of Cassandra Unit
  - Spring Data Cassandra annotated entity that features a composite key (both partition and clustering parts consist of multiple fields), a secondary index and a collection field
  - [Project Lombok](https://github.com/rzwitserloot/lombok) to avoid boilerplate code
  - predefined data loaded from CQL dataset
  - Spring CassandraTemplate or DataStax Java Driver to get data after create/update/delete operations in tests
  - [JUnit 4](https://github.com/junit-team/junit4) as a test framework and [Hamcrest](https://github.com/hamcrest/JavaHamcrest) matchers for assertions

# How to run
Simply run `mvn test` from the project root (requires Maven 3).