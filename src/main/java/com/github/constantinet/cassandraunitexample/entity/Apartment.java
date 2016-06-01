package com.github.constantinet.cassandraunitexample.entity;

import lombok.*;
import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.Indexed;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

import java.io.Serializable;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Table("apartments")
public class Apartment implements Serializable {

    @PrimaryKey
    private ApartmentLocation apartmentLocation;

    @Column
    @Indexed
    private String owner;

    @Column
    private Set<String> features;
}