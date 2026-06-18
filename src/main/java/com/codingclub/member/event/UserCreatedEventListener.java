package com.codingclub.member.event;

import com.codingclub.common.event.UserCreatedEvent;
import com.codingclub.member.model.Member;
import com.codingclub.member.repository.MemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class UserCreatedEventListener {

    private static final Logger log = LoggerFactory.getLogger(UserCreatedEventListener.class);

    @Autowired
    private MemberRepository memberRepository;

    @KafkaListener(topics = "user-created-topic", groupId = "member-group")
    public void handleUserCreatedEvent(UserCreatedEvent event) {
        log.info("Received UserCreatedEvent for user ID: {}", event.getUserId());
        
        // Ensure idempotency
        if (memberRepository.findByUserId(event.getUserId()).isEmpty()) {
            Member member = new Member();
            member.setUserId(event.getUserId());
            member.setFullName(event.getUsername());
            
            memberRepository.save(member);
            log.info("Successfully created Member profile for user ID: {}", event.getUserId());
        } else {
            log.info("Member profile already exists for user ID: {}", event.getUserId());
        }
    }
}
