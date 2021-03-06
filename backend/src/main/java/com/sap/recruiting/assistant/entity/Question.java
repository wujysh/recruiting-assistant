package com.sap.recruiting.assistant.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Jiaye Wu on 18-4-9.
 */
@Entity
@Table(name = "question")
public class Question implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 100)
    private String content;

    @Column(nullable = false)
    private int type;  // 0 - pending audit, 1 - approved

    @OneToOne(fetch = FetchType.LAZY)
    private Tag tag;

    @OneToOne(fetch = FetchType.LAZY)
    private Company company;

    public Question() {

    }

    public Question(String content, int type) {
        this.content = content;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}
