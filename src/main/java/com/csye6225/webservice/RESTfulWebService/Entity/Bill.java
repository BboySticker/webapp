package com.csye6225.webservice.RESTfulWebService.Entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "bill")
//@JsonFilter("BillFilter")
public class Bill {

    private enum PaymentStatus{
        paid("paid"),
        due("due"),
        past_due("past_due"),
        no_payment_required("no_payment_required");

        private String value;
        private PaymentStatus(String value){
            this.value = value;
        }
        public String getValue() {
            return value;
        }
    }

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

    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "categories")
    private List<String> categories = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus;

    public Bill() {
    }

    public Bill(String id, Date createdTs, Date updatedTs, String ownerId, String vendor, Date billDate, Date dueDate, double amountDue, List<String> categories, PaymentStatus paymentStatus) {
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

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
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
