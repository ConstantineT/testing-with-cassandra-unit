package com.github.constantinet.cassandraunitexample.repository;

import com.github.constantinet.cassandraunitexample.entity.Apartment;
import com.github.constantinet.cassandraunitexample.entity.ApartmentLocation;

public interface ApartmentRepository {

    Iterable<Apartment> findAll();

    // querying with no partition key against real data has a large performance overhead
    Iterable<Apartment> findByOwner(String owner);

    Apartment findOne(ApartmentLocation apartmentLocation);

    Apartment save(Apartment entity);

    void delete(ApartmentLocation apartmentLocation);

    void deleteAll();
}