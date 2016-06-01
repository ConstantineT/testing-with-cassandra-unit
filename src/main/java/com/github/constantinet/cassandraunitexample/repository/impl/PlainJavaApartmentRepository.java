package com.github.constantinet.cassandraunitexample.repository.impl;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.github.constantinet.cassandraunitexample.repository.ApartmentRepository;
import com.github.constantinet.cassandraunitexample.entity.Apartment;
import com.github.constantinet.cassandraunitexample.entity.ApartmentLocation;

import java.util.ArrayList;
import java.util.stream.Collectors;

import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;

public class PlainJavaApartmentRepository implements ApartmentRepository {

    private final Session session;

    public PlainJavaApartmentRepository(final Session session) {
        this.session = session;
    }

    @Override
    public Iterable<Apartment> findAll() {
        final ResultSet rs = session.execute("SELECT * FROM apartments");

        return rs.all().stream().map(this::buildApartment).collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public Iterable<Apartment> findByOwner(final String owner) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Apartment findOne(final ApartmentLocation apartmentLocation) {
        final Statement statement = QueryBuilder.select().from("apartments")
                .where(eq("city", apartmentLocation.getCity()))
                .and(eq("street", apartmentLocation.getStreet()))
                .and(eq("house_number", apartmentLocation.getHouseNumber()))
                .and(eq("apartment_number", apartmentLocation.getApartmentNumber()));

        final ResultSet rs = session.execute(statement);

        return buildApartment(rs.one());
    }

    @Override
    public Apartment save(final Apartment apartment) {
        final Statement statement = QueryBuilder.insertInto("apartments")
                .value("city", apartment.getApartmentLocation().getCity())
                .value("street", apartment.getApartmentLocation().getStreet())
                .value("house_number", apartment.getApartmentLocation().getHouseNumber())
                .value("apartment_number", apartment.getApartmentLocation().getApartmentNumber())
                .value("owner", apartment.getOwner())
                .value("features", apartment.getFeatures()); // upsert

        session.execute(statement);

        return apartment;
    }

    @Override
    public void delete(final ApartmentLocation apartmentLocation) {
        final Statement statement = QueryBuilder.delete().from("apartments")
                .where(eq("city", apartmentLocation.getCity()))
                .and(eq("street", apartmentLocation.getStreet()))
                .and(eq("house_number", apartmentLocation.getHouseNumber()))
                .and(eq("apartment_number", apartmentLocation.getApartmentNumber()));

        session.execute(statement);
    }

    @Override
    public void deleteAll() {
        throw new UnsupportedOperationException();
    }

    private Apartment buildApartment(final Row row) {
        final Apartment apartment = new Apartment();
        apartment.setApartmentLocation(new ApartmentLocation(row.getString("city"), row.getString("street"),
                row.getInt("house_number"), row.getInt("apartment_number")));
        apartment.setOwner(row.getString("owner"));
        apartment.setFeatures(row.getSet("features", String.class));
        return apartment;
    }
}