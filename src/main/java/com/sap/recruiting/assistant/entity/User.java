package com.sap.recruiting.assistant.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Jiaye Wu on 18-3-25.
 */
@Entity
@Table(name = "user")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, nullable = false, length = 40)
    private String username;

    @Column(nullable = false, length = 100)
    private String password;

    @ManyToOne(fetch = FetchType.EAGER)
    private Company company;

    public User() {

    }

    public User(String username) {
        this.username = username;
    }

    public User(Company company, String username) {
        this.company = company;
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}
