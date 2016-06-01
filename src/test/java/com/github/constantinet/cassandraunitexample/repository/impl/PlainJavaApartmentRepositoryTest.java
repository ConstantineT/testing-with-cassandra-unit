package com.github.constantinet.cassandraunitexample.repository.impl;

import com.datastax.driver.core.Row;
import com.github.constantinet.cassandraunitexample.entity.Apartment;
import com.github.constantinet.cassandraunitexample.entity.ApartmentLocation;
import com.github.constantinet.cassandraunitexample.repository.ApartmentRepository;
import org.cassandraunit.CassandraCQLUnit;
import org.cassandraunit.dataset.cql.ClassPathCQLDataSet;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class PlainJavaApartmentRepositoryTest {

    private static final String FIND_BY_PK_QUERY_TEMPLATE = "SELECT * FROM apartments " +
            "WHERE city = '%s' AND street = '%s' AND house_number = %d AND apartment_number = %d";
    private static final String GIVEN_PK_CITY = "Salem";
    private static final String GIVEN_PK_STREET = "Pine Lane";
    private static final int GIVEN_PK_HOUSE_NUMBER = 10;
    private static final int GIVEN_PK_APARTMENT_NUMBER = 101;

    @Rule
    // default cluster configuration from cu-cassandra.yaml, i.e. 127.0.0.1:9142
    public final CassandraCQLUnit cassandraCQLUnit = new CassandraCQLUnit(
            new ClassPathCQLDataSet("cql/apartments.cql", "example"), 20000);

    @Rule
    public ErrorCollector errorCollector = new ErrorCollector();

    private ApartmentRepository plainJavaApartmentRepository;

    @Before
    public void setUp() {
        plainJavaApartmentRepository = new PlainJavaApartmentRepository(cassandraCQLUnit.getSession());
    }

    @Test
    public void testFindAll_returnsAllRecords() throws Exception {
        // when
        final Iterable<Apartment> result = plainJavaApartmentRepository.findAll();

        // then
        assertThat(result, containsInAnyOrder(
                allOf(
                        hasProperty("apartmentLocation", is(new ApartmentLocation("Salem", "Pine Lane", 10, 100))),
                        hasProperty("owner", is("John Smith")),
                        hasProperty("features", contains("Balcony", "Luxury bathroom"))
                ),
                allOf(
                        hasProperty("apartmentLocation", is(new ApartmentLocation("Salem", "Pine Lane", 10, 101))),
                        hasProperty("owner", is("Mary Johnson")),
                        hasProperty("features", contains("Close to public transportation", "Garden"))
                ),
                allOf(
                        hasProperty("apartmentLocation", is(new ApartmentLocation("Salem", "Lake Avenue", 2, 20))),
                        hasProperty("owner", is("John Smith")),
                        hasProperty("features", contains("Attic", "Balcony", "Swimming pool"))
                )
        ));
    }

    @Test
    public void testFindOne_whenExistingKeyPassed_returnsCorrectRecord() throws Exception {
        // when
        final ApartmentLocation inputArgument = new ApartmentLocation(GIVEN_PK_CITY, GIVEN_PK_STREET,
                GIVEN_PK_HOUSE_NUMBER, GIVEN_PK_APARTMENT_NUMBER);
        final Apartment result = plainJavaApartmentRepository.findOne(inputArgument);

        // then
        assertThat(result, allOf(
                hasProperty("apartmentLocation", is(new ApartmentLocation(GIVEN_PK_CITY,
                        GIVEN_PK_STREET, GIVEN_PK_HOUSE_NUMBER, GIVEN_PK_APARTMENT_NUMBER))),
                hasProperty("owner", is("Mary Johnson")),
                hasProperty("features", contains("Close to public transportation", "Garden"))
        ));
    }

    @Test
    public void testSave_whenNotExistingEntityPassed_insertsRecord() throws Exception {
        // given
        final String givenCity = "Springfield";
        final String givenStreet = "Second Street";
        final int givenHouseNumber = 3;
        final int givenApartmentNumber = 30;
        final String givenOwner = "Mary Thompson";
        final Set<String> givenFeatures = Collections.singleton("Nice view");

        // when
        final Apartment inputArgument = new Apartment(new ApartmentLocation(givenCity, givenStreet,
                givenHouseNumber, givenApartmentNumber), givenOwner, givenFeatures);
        plainJavaApartmentRepository.save(inputArgument);

        // then
        final List<Row> result = cassandraCQLUnit.getSession().execute(
                String.format(FIND_BY_PK_QUERY_TEMPLATE,
                        givenCity,
                        givenStreet,
                        givenHouseNumber,
                        givenApartmentNumber)).all();
        assertThat(result, contains(notNullValue())); // contains only one element which is not null
        assertThat(result.get(0).getString("city"), is(givenCity));
        assertThat(result.get(0).getString("street"), is(givenStreet));
        assertThat(result.get(0).getInt("house_number"), is(givenHouseNumber));
        assertThat(result.get(0).getInt("apartment_number"), is(givenApartmentNumber));
        assertThat(result.get(0).getString("owner"), is(givenOwner));
        assertThat(result.get(0).getSet("features", String.class), is(givenFeatures));
    }

    @Test
    public void testSave_whenExistingEntityPassed_updatesRecord() throws Exception {
        // given
        final String givenOwner = "Mary Thompson";
        final Set<String> givenFeatures = Collections.singleton("Nice view");

        // when
        final Apartment inputArgument = new Apartment(new ApartmentLocation(GIVEN_PK_CITY, GIVEN_PK_STREET,
                GIVEN_PK_HOUSE_NUMBER, GIVEN_PK_APARTMENT_NUMBER), givenOwner, givenFeatures);
        plainJavaApartmentRepository.save(inputArgument);

        // then
        final List<Row> result = cassandraCQLUnit.getSession().execute(
                String.format(FIND_BY_PK_QUERY_TEMPLATE,
                        GIVEN_PK_CITY,
                        GIVEN_PK_STREET,
                        GIVEN_PK_HOUSE_NUMBER,
                        GIVEN_PK_APARTMENT_NUMBER)).all();
        assertThat(result, contains(notNullValue())); // contains only one element which is not null
        assertThat(result.get(0).getString("city"), is(GIVEN_PK_CITY));
        assertThat(result.get(0).getString("street"), is(GIVEN_PK_STREET));
        assertThat(result.get(0).getInt("house_number"), is(GIVEN_PK_HOUSE_NUMBER));
        assertThat(result.get(0).getInt("apartment_number"), is(GIVEN_PK_APARTMENT_NUMBER));
        assertThat(result.get(0).getString("owner"), is(givenOwner));
        assertThat(result.get(0).getSet("features", String.class), is(givenFeatures));
    }

    @Test
    public void testDelete_whenExistingKeyPassed_deletesRecord() throws Exception {
        // when
        final ApartmentLocation inputArgument = new ApartmentLocation(GIVEN_PK_CITY, GIVEN_PK_STREET,
                GIVEN_PK_HOUSE_NUMBER, GIVEN_PK_APARTMENT_NUMBER);
        plainJavaApartmentRepository.delete(inputArgument);

        // then
        final List<Row> result = cassandraCQLUnit.getSession().execute(
                String.format(FIND_BY_PK_QUERY_TEMPLATE,
                        GIVEN_PK_CITY,
                        GIVEN_PK_STREET,
                        GIVEN_PK_HOUSE_NUMBER,
                        GIVEN_PK_APARTMENT_NUMBER)).all();
        assertThat(result, empty());
    }
}