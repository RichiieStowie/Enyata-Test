package com.example.enyata.models;

import com.example.enyata.enums.UserStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Generated;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "user_profile")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;
    private String email;
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDate dateJoined;
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;
}
