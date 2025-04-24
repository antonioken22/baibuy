package com.bblets.baibuy.repository;

import com.bblets.baibuy.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Integer> {
    List<Message> findByProductIdAndSenderIdOrReceiverIdOrderBySentAtAsc(
            Integer productId, Integer senderId, Integer receiverId);
}
