package com.bblets.baibuy.controllers;

import com.bblets.baibuy.models.Message;
import com.bblets.baibuy.models.MessageDto;
import com.bblets.baibuy.repository.MessageRepository;
import com.bblets.baibuy.security.AuthUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageRepository messageRepository;

    @PostMapping("/send")
    public String sendMessage(@RequestBody MessageDto dto, @AuthenticationPrincipal AuthUserDetails userDetails) {
        Message msg = Message.builder()
                .content(dto.getContent())
                .senderId(userDetails.getId())
                .receiverId(dto.getReceiverId())
                .productId(dto.getProductId())
                .build();

        messageRepository.save(msg);
        return "Message sent";
    }

    @GetMapping("/thread/{productId}")
    public List<Message> getThread(@PathVariable Integer productId, @AuthenticationPrincipal AuthUserDetails user) {
        return messageRepository.findByProductIdAndSenderIdOrReceiverIdOrderBySentAtAsc(
                productId, user.getId(), user.getId());
    }

}
