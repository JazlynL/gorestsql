package com.careerdevs.gorestsql.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.criteria.CriteriaBuilder;

@Entity
public class Users {


    // coming from java persistence.
    @Id

    // auto identity sequence and table
    @GeneratedValue(strategy = GenerationType.AUTO)

    private Integer id;
    private String name;
    private String email;
    private String status;
    private String gender;


    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getStatus() {
        return status;
    }

    public String getGender() {
        return gender;
    }

    @Override
    public String toString() {
        return "Users{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", status='" + status + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }
}
