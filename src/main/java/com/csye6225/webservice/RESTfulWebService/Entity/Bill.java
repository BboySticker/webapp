package com.csye6225.webservice.RESTfulWebService.Entity;

import com.fasterxml.jackson.annotation.JsonFilter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "bill")
@JsonFilter("BillFilter")
public class Bill {

    @Id
    @Column(name = "id")
    private String id;  // read-only

    @Column(name = "created_ts")
    private Date createdTs;  // read-only

    @Column(name = "updated_ts")
    private Date updatedTs;  // read-only

    @Column(name = "owner_id")
    private String ownerId;  // read-only

    @Column(name = "vendor")
    private String vendor;

    @Column(name = "bill_date")
    private Date billDate;

    @Column(name = "due_date")
    private Date dueDate;

    @Column(name = "amount_due")
    private double amountDue;

    @Column(name = "categories")
    private String categories;

    @Column(name = "payment_status")
    private String paymentStatus;

    public Bill() {
    }

    public Bill(String id, Date createdTs, Date updatedTs, String ownerId, String vendor, Date billDate, Date dueDate, double amountDue, String categories, String paymentStatus) {
        this.id = id;
        this.createdTs = createdTs;
        this.updatedTs = updatedTs;
        this.ownerId = ownerId;
        this.vendor = vendor;
        this.billDate = billDate;
        this.dueDate = dueDate;
        this.amountDue = amountDue;
        this.categories = categories;
        this.paymentStatus = paymentStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreatedTs() {
        return createdTs;
    }

    public void setCreatedTs(Date createdTs) {
        this.createdTs = createdTs;
    }

    public Date getUpdatedTs() {
        return updatedTs;
    }

    public void setUpdatedTs(Date updatedTs) {
        this.updatedTs = updatedTs;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public Date getBillDate() {
        return billDate;
    }

    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public double getAmountDue() {
        return amountDue;
    }

    public void setAmountDue(double amountDue) {
        this.amountDue = amountDue;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    @Override
    public String toString() {
        return "Bill{" +
                "id='" + id + '\'' +
                ", createdTs=" + createdTs +
                ", updatedTs=" + updatedTs +
                ", ownerId='" + ownerId + '\'' +
                ", vendor='" + vendor + '\'' +
                ", billDate=" + billDate +
                ", dueDate=" + dueDate +
                ", amountDue=" + amountDue +
                ", categories=" + categories +
                ", paymentStatus='" + paymentStatus + '\'' +
                '}';
    }
}
