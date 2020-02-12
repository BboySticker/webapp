package com.csye6225.webservice.RESTfulWebService.Entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "file")
public class File {

    /*
    file_name*	    string
                    readOnly: true
                    example: mybill.pdf
    id*	            string($uuid)
                    readOnly: true
                    example: d290f1ee-6c54-4b01-90e6-d701748f0851
    url*	        string
                    readOnly: true
                    example: /tmp/file.jpg
    upload_date*	string($date)
                    readOnly: true
                    example: 2020-01-12
     */

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "url")
    private String url;

    @Column(name = "upload_date")
    private Date uploadDate;

    @Column(name = "size")
    private long size;

    @Column(name = "bill_id")
    private String billId;

    @Column(name = "owner_id")
    private String ownerId;

    @OneToOne(mappedBy = "attachment")
    private Bill bill;

    public File() {
    }

    public File(String id, String fileName, String url, Date uploadDate, long size, String billId, String ownerId) {
        this.id = id;
        this.fileName = fileName;
        this.url = url;
        this.uploadDate = uploadDate;
        this.size = size;
        this.billId = billId;
        this.ownerId = ownerId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    @Override
    public String toString() {
        return "File{" +
                "id='" + id + '\'' +
                ", fileName='" + fileName + '\'' +
                ", url='" + url + '\'' +
                ", uploadDate=" + uploadDate +
                ", size=" + size +
                ", billId='" + billId + '\'' +
                ", ownerId='" + ownerId + '\'' +
                '}';
    }
}
