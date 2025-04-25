package com.bblets.baibuy.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction("deleted_at IS NULL")
public class Product extends AuditableFields {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ElementCollection
    @CollectionTable(name = "product_image_urls", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "image_url", nullable = false)
    private List<String> imageUrls;

    @Column(nullable = false)
    private String name;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private String barangayName;

    @Column(nullable = false)
    private String category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductCondition productCondition;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer stocks;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_preference", nullable = false)
    private DeliveryPreference deliveryPreference;

    @ElementCollection
    @CollectionTable(name = "product_tags", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "tag", nullable = true)
    private List<String> tags;

    @ElementCollection
    @CollectionTable(name = "product_report_ids", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "report_id")
    private List<Integer> reportIds;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean isListed;

    @Column(nullable = true)
    private LocalDateTime listedAt;

    // Enums
    public enum ProductCondition {
        NEW, USED, REFURBISHED, DEFECTIVE
    }

    public enum DeliveryPreference {
        DELIVERY_APPS, MEETUP, PICK_UP, DROP_OFF
    }
}
