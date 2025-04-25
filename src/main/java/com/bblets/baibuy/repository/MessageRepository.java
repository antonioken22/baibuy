package com.bblets.baibuy.repository;

import com.bblets.baibuy.models.User;

import com.bblets.baibuy.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface MessageRepository extends JpaRepository<Message, Integer> {
    @Query("SELECT m FROM Message m WHERE m.productId = :productId AND ((m.senderId = :senderId AND m.receiverId = :receiverId) OR (m.senderId = :receiverId AND m.receiverId = :senderId)) ORDER BY m.sentAt ASC")
    List<Message> findConversationBetweenUserAndSeller(
            @Param("productId") Integer productId,
            @Param("senderId") Integer senderId,
            @Param("receiverId") Integer receiverId);

    @Query("SELECT DISTINCT u FROM User u JOIN Message m ON u.id = m.senderId WHERE m.productId = :productId")
    Set<User> findDistinctSendersByProductId(@Param("productId") Integer productId);

}
