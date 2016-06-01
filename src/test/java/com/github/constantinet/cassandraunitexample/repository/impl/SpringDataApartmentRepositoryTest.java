package com.github.constantinet.cassandraunitexample.repository.impl;

import com.github.constantinet.cassandraunitexample.entity.Apartment;
import com.github.constantinet.cassandraunitexample.entity.ApartmentLocation;
import com.github.constantinet.cassandraunitexample.repository.ApartmentRepository;
import org.cassandraunit.spring.CassandraDataSet;
import org.cassandraunit.spring.CassandraUnitDependencyInjectionTestExecutionListener;
import org.cassandraunit.spring.EmbeddedCassandra;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({CassandraUnitDependencyInjectionTestExecutionListener.class,
        DependencyInjectionTestExecutionListener.class})
@ContextConfiguration("classpath:cassandra-test-context.xml")
@CassandraDataSet(value = {"cql/apartments.cql"}, keyspace = "example")
@EmbeddedCassandra(timeout = 20000)
public class SpringDataApartmentRepositoryTest {

    @Autowired
    private ApartmentRepository springDataApartmentRepository;

    @Autowired
    private CassandraOperations cassandraOperations;

    @Test
    public void testFindByOwner_whenExistingOwnerPassed_returnsAllRecordsByOwner() throws Exception {
        // when
        final Iterable<Apartment> result = springDataApartmentRepository.findByOwner("John Smith");

        // then
        assertThat(result, containsInAnyOrder(
                allOf(
                        hasProperty("apartmentLocation", is(new ApartmentLocation("Salem", "Pine Lane", 10, 100))),
                        hasProperty("owner", is("John Smith")),
                        hasProperty("features", contains("Balcony", "Luxury bathroom"))
                ),
                allOf(
                        hasProperty("apartmentLocation", is(new ApartmentLocation("Salem", "Lake Avenue", 2, 20))),
                        hasProperty("owner", is("John Smith")),
                        hasProperty("features", contains("Attic", "Balcony", "Swimming pool"))
                )
        ));
    }

    @Test
    public void testDeleteAll_deletesAllRecords() throws Exception {
        // when
        springDataApartmentRepository.deleteAll();

        // then
        final List<Apartment> result = cassandraOperations.select("SELECT * FROM apartments", Apartment.class);
        assertThat(result, empty());
    }
}