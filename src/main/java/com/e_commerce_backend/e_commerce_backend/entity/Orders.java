package com.e_commerce_backend.e_commerce_backend.entity;

import com.e_commerce_backend.e_commerce_backend.entity.Enum.OrderStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Orders {


        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String orderNumber;

        @ManyToOne
        @JoinColumn(name = "user_id")
        @JsonIgnore
        private UserEntity user;

        private Double totalAmount;

        @Enumerated(EnumType.STRING)
        private OrderStatus orderStatus;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "address_id")
        @JsonIgnore
        private Address address;

        private String paymentStatus;

        private Instant createdAt;
        private Instant updatedAt;

        @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
        private List<OrderItem> orderItems;

    }


