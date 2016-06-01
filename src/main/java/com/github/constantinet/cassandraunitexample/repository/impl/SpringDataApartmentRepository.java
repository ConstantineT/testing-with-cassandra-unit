package com.github.constantinet.cassandraunitexample.repository.impl;

import com.github.constantinet.cassandraunitexample.entity.Apartment;
import com.github.constantinet.cassandraunitexample.entity.ApartmentLocation;
import com.github.constantinet.cassandraunitexample.repository.ApartmentRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface SpringDataApartmentRepository extends ApartmentRepository, CrudRepository<Apartment, ApartmentLocation> {

    // declarative query methods are not supported yet
    @Query("SELECT * FROM apartments WHERE owner=?0")
    Iterable<Apartment> findByOwner(String owner);
}