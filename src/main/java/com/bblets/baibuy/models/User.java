package com.bblets.baibuy.models;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction("deleted_at IS NULL")
public class User extends AuditableFields {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password; // hashed + salted

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = true)
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // Report handling – for future feature
    @ElementCollection
    @CollectionTable(name = "user_report_ids", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "report_id")
    private List<Integer> reportIds;

    // Enums
    public enum Role {
        USER,
        ADMIN
    }
}
