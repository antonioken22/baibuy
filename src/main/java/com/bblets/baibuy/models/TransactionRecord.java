package com.bblets.baibuy.models;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionRecord extends AuditableFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false)
    private User buyer;

    @ManyToOne(optional = false)
    private User seller;

    @ManyToOne(optional = false)
    private Product product;

    private boolean buyerReviewed = false;

    @Column(nullable = false)
    private Boolean sellerReviewed = false;

    public Boolean getSellerReviewed() {
        return sellerReviewed;
    }

    public boolean isSellerReviewed() {
        return Boolean.TRUE.equals(sellerReviewed);
    }

    public void setSellerReviewed(Boolean sellerReviewed) {
        this.sellerReviewed = sellerReviewed;
    }

    private LocalDateTime confirmedAt;
}
