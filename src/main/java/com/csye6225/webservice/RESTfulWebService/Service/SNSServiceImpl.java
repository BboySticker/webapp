package com.csye6225.webservice.RESTfulWebService.Service;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import org.springframework.beans.factory.annotation.Value;

public class SNSServiceImpl implements SNSService {

    @Value("${aws.sns.arn}")
    private String snsArn;

    public void publishRequest(String message) {

        AmazonSNS snsClient = AmazonSNSClientBuilder.defaultClient();
        final PublishRequest publishRequest = new PublishRequest(snsArn, message);
        final PublishResult publishResponse = snsClient.publish(publishRequest);

    }

}
