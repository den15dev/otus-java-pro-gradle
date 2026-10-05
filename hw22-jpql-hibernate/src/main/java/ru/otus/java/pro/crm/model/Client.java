package ru.otus.java.pro.crm.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "client")
public class Client implements Cloneable {

    @Id
    @SequenceGenerator(
        name = "client_gen",
        sequenceName = "client_seq",
        allocationSize = 1
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "client_gen"
    )
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToMany(
        mappedBy = "client",
        cascade = CascadeType.ALL,
        fetch = FetchType.EAGER
    )
    private List<Phone> phones = new ArrayList<>();

    public Client() {}

    public Client(String name) {
        this.id = null;
        this.name = name;
    }

    public Client(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Client(Long id, String name, Address address, List<Phone> phones) {
        this.id = id;
        this.name = name;
        this.address = address;

        if (phones != null) {
            this.phones = new ArrayList<>(phones);
            this.phones.forEach(phone -> phone.setClient(this));
        }
    }

    public Client(String name, Address address, List<Phone> phones) {
        this(null, name, address, phones);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Address getAddress() {
        return address;
    }

    public List<Phone> getPhones() {
        return phones;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    @SuppressWarnings({"java:S2975", "java:S1182"})
    public Client clone() {
        return new Client(
            this.id,
            this.name,
            this.address,
            this.phones
        );
    }

    @Override
    public String toString() {
        return "Client{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", address=" + address +
            ", phones=" + phones +
            '}';
    }
}
