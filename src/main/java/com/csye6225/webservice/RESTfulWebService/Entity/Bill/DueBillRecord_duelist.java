package com.csye6225.webservice.RESTfulWebService.Entity.Bill;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "record_duebills")
public class DueBillRecord_duelist {

    @Id
    @Column(name = "record_id")
    private String recordId;

    @Column(name = "due_bill")
    private String billId;

    public DueBillRecord_duelist() {
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    @Override
    public String toString() {
        return "DueBillRecord_duelist{" +
                "recordId='" + recordId + '\'' +
                ", billId='" + billId + '\'' +
                '}';
    }
}
