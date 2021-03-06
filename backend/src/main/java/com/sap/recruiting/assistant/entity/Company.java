package com.sap.recruiting.assistant.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Map;

/**
 * Created by Jiaye Wu on 18-3-25.
 */
@Entity
@Table(name = "company")
public class Company implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, nullable = false, length = 40)
    private String name;

    @ElementCollection
    @CollectionTable(name = "company_tag", joinColumns = @JoinColumn(name = "company_id", referencedColumnName = "id"))
    @Column(name = "content", length = 10000)
    @MapKeyJoinColumn(name = "tag_id", referencedColumnName = "id")
    private Map<Tag, String> properties;

    public Company() {

    }

    public Company(String name) {
        this.name = name;
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

    public Map<Tag, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<Tag, String> properties) {
        this.properties = properties;
    }
}
