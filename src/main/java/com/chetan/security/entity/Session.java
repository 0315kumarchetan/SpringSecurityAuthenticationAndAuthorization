package com.chetan.security.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Session {
    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true,nullable = false,length = 512)
    private String refreshToken;
    @ManyToOne
    private User user;
    @CreationTimestamp
    private LocalDateTime lastUsedAt;
}
