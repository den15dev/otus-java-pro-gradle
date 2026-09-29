package ru.otus.java.pro.crm.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "address")
public class Address {
    @Id
    @SequenceGenerator(
        name = "address_gen",
        sequenceName = "address_seq",
        allocationSize = 1
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "address_gen"
    )
    @Column(name = "id")
    private Long id;

    @Column(name = "street")
    private String street;

    public Address() {}

    public Address(String street) {
        this.street = street;
    }

    public Long getId() {
        return id;
    }

    public String getStreet() {
        return street;
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", street='" + street + '\'' +
                '}';
    }
}