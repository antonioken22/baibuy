package com.bblets.baibuy.models;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password; // hashed + salted

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // Audit Fields
    private Date createdAt;
    private String createdBy;
    private Date updatedAt;
    private Integer updatedBy;
    private Date deletedAt;
    private Integer deletedBy;
    private boolean isBlocked;
    private Date blockedAt;
    private Integer blockedBy;

    // Report handling – for future feature
    @ElementCollection
    private List<Integer> reportIds;

    public enum Role {
        USER,
        SELLER,
        ADMIN
    }
}
