package com.csye6225.webservice.RESTfulWebService.Entity.Bill;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "record")
public class DueBillRecord {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "owner_id")
    private String ownerId;

    @Column(name = "due_bill")
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> dueBills = new ArrayList<>();

    public DueBillRecord() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public List<String> getDueBills() {
        return dueBills;
    }

    public void setDueBills(List<String> dueBills) {
        this.dueBills = dueBills;
    }

    @Override
    public String toString() {
        return "DueBillRecord{" +
                "id='" + id + '\'' +
                ", ownerId='" + ownerId + '\'' +
                ", dueBills=" + dueBills +
                '}';
    }
}
