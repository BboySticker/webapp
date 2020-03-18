package com.csye6225.webservice.RESTfulWebService.Service;

import java.io.File;
import java.io.IOException;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.csye6225.webservice.RESTfulWebService.Utils.ConvertFromMultipart;
import com.timgroup.statsd.StatsDClient;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import org.springframework.web.multipart.MultipartFile;


@Service
public class S3ServicesImpl implements S3Services {

    private java.util.logging.Logger logger = java.util.logging.Logger.getLogger(getClass().getName());

    @Autowired
    private AmazonS3 s3client;

    @Autowired
    private StatsDClient statsDClient;

    @Value("${aws.s3.bucket.name}")
    private String bucketName;

    @Override
    public PutObjectResult uploadFile(String keyName, MultipartFile file) throws IOException {

        File theFile = ConvertFromMultipart.convertFromMultipart(file);

        try {
            // Upload a file as a new object with ContentType and title specified.
            PutObjectRequest request = new PutObjectRequest(bucketName, keyName, theFile);

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.addUserMetadata("x-amz-meta-title", theFile.getName());
            request.setMetadata(metadata);

            long s3StartTime = System.currentTimeMillis();
            PutObjectResult obj = s3client.putObject(request);
            long s3EndTime = System.currentTimeMillis();
            statsDClient.recordExecutionTime("s3.ops.endpoint.file.upload", s3EndTime - s3StartTime);

            logger.info("===================== Upload File - Done! =====================");

            return obj;
        }
        catch (AmazonServiceException ase) {
            logger.info("Caught an AmazonServiceException from PUT requests, rejected reasons:");
            logger.info("Error Message:    " + ase.getMessage());
            logger.info("HTTP Status Code: " + ase.getStatusCode());
            logger.info("AWS Error Code:   " + ase.getErrorCode());
            logger.info("Error Type:       " + ase.getErrorType());
            logger.info("Request ID:       " + ase.getRequestId());
        }
        catch (AmazonClientException ace) {
            logger.info("Caught an AmazonClientException: ");
            logger.info("Error Message: " + ace.getMessage());
        }

        return null;
    }

    @Override
    public void deleteFile(String keyName) {

        try {
            long s3StartTime = System.currentTimeMillis();
            s3client.deleteObject(new DeleteObjectRequest(bucketName, keyName));
            long s3EndTime = System.currentTimeMillis();
            statsDClient.recordExecutionTime("s3.ops.endpoint.file.delete", s3EndTime - s3StartTime);
            logger.info("===================== Download File - Done! =====================");
        }
        catch (AmazonServiceException ase) {
            logger.info("Caught an AmazonServiceException from GET requests, rejected reasons:");
            logger.info("Error Message:    " + ase.getMessage());
            logger.info("HTTP Status Code: " + ase.getStatusCode());
            logger.info("AWS Error Code:   " + ase.getErrorCode());
            logger.info("Error Type:       " + ase.getErrorType());
            logger.info("Request ID:       " + ase.getRequestId());
        }
        catch (AmazonClientException ace) {
            logger.info("Caught an AmazonClientException: ");
            logger.info("Error Message: " + ace.getMessage());
        }
    }

}

