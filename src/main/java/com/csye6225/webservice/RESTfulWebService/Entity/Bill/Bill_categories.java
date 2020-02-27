package com.csye6225.webservice.RESTfulWebService.Entity.Bill;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "bill_categories")
public class Bill_categories {

    @Id
    @Column(name = "bill_id")
    private String billId;

    @Column(name = "categories")
    private String categories;

    public Bill_categories() {
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    @Override
    public String toString() {
        return "Bill_categories{" +
                "billId='" + billId + '\'' +
                ", categories='" + categories + '\'' +
                '}';
    }
}
