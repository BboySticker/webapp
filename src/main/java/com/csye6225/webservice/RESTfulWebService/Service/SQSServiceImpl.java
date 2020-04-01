package com.csye6225.webservice.RESTfulWebService.Service;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SQSServiceImpl implements SQSService {

    public void putMessage(String recordId, String ownerId) {
        Map<String, Object> map = new HashMap<>();
        map.put("recordId", recordId);
        map.put("ownerId", ownerId);

        AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
        SendMessageRequest send_msg_request = new SendMessageRequest()
                .withQueueUrl("https://sqs.us-east-1.amazonaws.com/480616773130/MySQSQueue")  // url of aws sqs service
                .withMessageBody(new JSONObject(map).toString())
                .withDelaySeconds(5);
        sqs.sendMessage(send_msg_request);
    }

}
