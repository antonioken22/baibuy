package com.bblets.baibuy.models;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review extends AuditableFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false)
    private User reviewer;

    @ManyToOne(optional = false)
    private User reviewedUser;

    @ManyToOne(optional = false)
    private Product product;

    @Column(nullable = false)
    private int rating;

    @Column(length = 1000)
    private String comment;

    private LocalDateTime transactionDate;
}
