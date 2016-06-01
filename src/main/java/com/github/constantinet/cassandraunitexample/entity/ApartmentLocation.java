package com.github.constantinet.cassandraunitexample.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@PrimaryKeyClass
public class ApartmentLocation implements Serializable {

    @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED, ordinal = 0)
    private String city;

    @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED, ordinal = 1)
    private String street;

    @PrimaryKeyColumn(name = "house_number", type = PrimaryKeyType.CLUSTERED, ordinal = 2)
    private int houseNumber;

    @PrimaryKeyColumn(name = "apartment_number", type = PrimaryKeyType.CLUSTERED, ordinal = 3)
    private int apartmentNumber;
}