package com.csye6225.webservice.RESTfulWebService.Configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("storage")
public class StorageProperties {

    /**
     * Folder location for storing files
     */
    private String location = "/Users/bboysticker/Development/webapp/src/main/resources/upload-dir/";

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}