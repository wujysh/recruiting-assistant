package com.sap.recruiting.assistant.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Jiaye Wu on 18-3-25.
 */
@Entity
@Table(name = "tag")
public class Tag implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, nullable = false, length = 40)
    private String name;

    @Column(length = 100)
    private String description;

    public Tag() {

    }

    public Tag(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
