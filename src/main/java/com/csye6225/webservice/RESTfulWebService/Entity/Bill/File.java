package com.csye6225.webservice.RESTfulWebService.Entity.Bill;

import javax.persistence.*;

@Entity
@Table(name = "file")
public class File {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "url")
    private String url;

    @Column(name = "upload_date")
    private String uploadDate;

    @Column(name = "size")
    private long size;

    @Column(name = "bill_id")
    private String billId;

    @Column(name = "owner_id")
    private String ownerId;

    @Column(name = "s3_metadata")
    private String s3Metadata;

    @OneToOne(mappedBy = "attachment")
    private Bill bill;

    public File() {
    }

    public File(String id, String fileName, String url, String uploadDate, long size, String billId, String ownerId, String s3Metadata) {
        this.id = id;
        this.fileName = fileName;
        this.url = url;
        this.uploadDate = uploadDate;
        this.size = size;
        this.billId = billId;
        this.ownerId = ownerId;
        this.s3Metadata = s3Metadata;
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

    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
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

    public String getS3Metadata() {
        return s3Metadata;
    }

    public void setS3Metadata(String s3Metadata) {
        this.s3Metadata = s3Metadata;
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
                ", s3Metadata='" + s3Metadata + '\'' +
                '}';
    }
}
