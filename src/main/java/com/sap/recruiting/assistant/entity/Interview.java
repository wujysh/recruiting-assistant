package com.sap.recruiting.assistant.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Jiaye Wu on 18-4-17.
 */
@Entity
@Table(name = "interview", indexes = @Index(name = "idx_on_wx_id_and_company_id", columnList = "wxId, company_id", unique = true))
public class Interview implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 100)
    private String wxId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Company company;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(nullable = false, length = 20)
    private String phone;

    @Column(nullable = false)
    private int score;

    @Column(nullable = false)
    private boolean finished;

    public Interview() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWxId() {
        return wxId;
    }

    public void setWxId(String wxId) {
        this.wxId = wxId;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }
}
