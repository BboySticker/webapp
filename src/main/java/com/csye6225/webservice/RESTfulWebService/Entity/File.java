package com.csye6225.webservice.RESTfulWebService.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
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

    public File() {
    }

    public File(String id, String fileName, String url, Date uploadDate) {
        this.id = id;
        this.fileName = fileName;
        this.url = url;
        this.uploadDate = uploadDate;
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

    @Override
    public String toString() {
        return "File{" +
                "id='" + id + '\'' +
                ", fileName='" + fileName + '\'' +
                ", url='" + url + '\'' +
                ", uploadDate=" + uploadDate +
                '}';
    }
}
