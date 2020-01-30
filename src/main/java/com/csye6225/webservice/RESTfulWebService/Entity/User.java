package com.csye6225.webservice.RESTfulWebService.Entity;

import com.fasterxml.jackson.annotation.JsonFilter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="user")
@JsonFilter("UserFilter")
public class User {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "password")
    private String password;

    @Column(name = "first_name")
    private String first_name;

    @Column(name = "last_name")
    private String last_name;

    @Column(name = "email_address")
    private String email_address;

    @Column(name = "account_created")
    private Date account_created;

    @Column(name = "account_updated")
    private Date account_updated;

    @Column(name = "token")
    private String token;


    public User() {
    }

    public User(String id, String password, String first_name, String last_name, String email_address, Date account_created, Date account_updated, String token) {
        this.id = id;
        this.password = password;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email_address = email_address;
        this.account_updated = account_updated;
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail_address() {
        return email_address;
    }

    public void setEmail_address(String email_address) {
        this.email_address = email_address;
    }

    public Date getAccount_created() {
        return account_created;
    }

    public void setAccount_created(Date account_created) {
        this.account_created = account_created;
    }

    public Date getAccount_updated() {
        return account_updated;
    }

    public void setAccount_updated(Date account_updated) {
        this.account_updated = account_updated;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", password='" + password + '\'' +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", email_address='" + email_address + '\'' +
                ", account_created=" + account_created +
                ", account_updated=" + account_updated +
                ", token='" + token + '\'' +
                '}';
    }
}
