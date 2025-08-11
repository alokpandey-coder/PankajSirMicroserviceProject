package com.notificationservice.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.notificationservice.constants.AppConstants;
import com.notificationservice.dto.EmailRequest;

@Service
public class EmailConsumer {
	
	@Autowired
	private JavaMailSender javaMailSender;

	@KafkaListener(topics = AppConstants.TOPIC, groupId="group_email")
	public void kafakSubscriberContent(String emailRequest) {
		
		ObjectMapper mapper = new ObjectMapper();
		
		try {
            EmailRequest emailContent = mapper.readValue(emailRequest, EmailRequest.class);
            
            SimpleMailMessage sm = new SimpleMailMessage();
            sm.setTo(emailContent.getTo());
            sm.setSubject(emailContent.getSubject());
            sm.setText(emailContent.getBody());
            
            javaMailSender.send(sm);
            
        } catch (Exception e) {
            // Handle parsing failure gracefully
            System.err.println("Failed to parse EmailRequest: " + e.getMessage());
        }
		
	}
}