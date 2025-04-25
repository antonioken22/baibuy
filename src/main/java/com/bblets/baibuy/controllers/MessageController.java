package com.bblets.baibuy.controllers;

import com.bblets.baibuy.models.Message;
import com.bblets.baibuy.models.MessageDto;
import com.bblets.baibuy.models.Product;
import com.bblets.baibuy.repository.MessageRepository;
import com.bblets.baibuy.repository.ProductsRepository;
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

    @Autowired
    private ProductsRepository productsRepository;

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
    public List<Message> getThread(@PathVariable Integer productId,
            @RequestParam(required = false) Integer senderId,
            @AuthenticationPrincipal AuthUserDetails user) {
        Integer currentUserId = user.getId();

        if (senderId != null && senderId != 0) {
            return messageRepository.findConversationBetweenUserAndSeller(productId, senderId, currentUserId);
        }

        Integer sellerId = productsRepository.findById(productId)
                .map(Product::getCreatedBy)
                .orElse(0);

        return messageRepository.findConversationBetweenUserAndSeller(productId, currentUserId, sellerId);
    }
}
