package com.csye6225.webservice.RESTfulWebService.Service;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SQSServiceImpl implements SQSService, Runnable {

    @Value("${aws.sqs.url}")
    private String sqsUrl;

    private Logger logger = LogManager.getLogger(getClass());

    public void putMessage(String recordId, String ownerId) {
        Map<String, Object> map = new HashMap<>();
        map.put("recordId", recordId);
        map.put("ownerId", ownerId);

        AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
        SendMessageRequest send_msg_request = new SendMessageRequest()
                .withQueueUrl(sqsUrl)  // url of aws sqs service
                .withMessageBody(new JSONObject(map).toString())
                .withDelaySeconds(5);
        sqs.sendMessage(send_msg_request);
    }

    public List<Message> pollMessages() {
        AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
        return sqs.receiveMessage(sqsUrl).getMessages();
    }

    public void deleteMessage(Message message) {
        AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
        sqs.deleteMessage(sqsUrl, message.getReceiptHandle());
    }

    @lombok.SneakyThrows
    @Override
    public void run() {
        while (true) {
            // once SQS has message, trigger SNS service
            List<Message> messages = pollMessages();
            for (Message message: messages) {
//                JSONObject jsonObject = new JSONObject(message.getBody());
                Map<String, Object> map = new ObjectMapper().readValue(message.getBody(), HashMap.class);
                logger.info("Successfully obtain message from AWS SQS...");
                logger.info("Message body: " + map.toString());
            }
        }
    }
}
