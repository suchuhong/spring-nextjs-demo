package com.only4play.backend.users;

import com.only4play.backend.entity.AbstractEntity;
import com.only4play.backend.users.data.CreateUserRequest;
import com.only4play.backend.util.ApplicationContextProvider;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLUpdate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToOne;

@Entity
@SQLUpdate(sql = "ALTER TABLE my_table ENGINE=InnoDB")
@Getter
@NoArgsConstructor
public class User extends AbstractEntity {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    @Setter
    private boolean verified = false;
    @Setter
    private String profileImageUrl;
    @Enumerated(EnumType.STRING)
    @Setter
    private Role role;

    @Setter
    @OneToOne(mappedBy = "user")
    private VerificationCode verificationCode;


    public User(CreateUserRequest data) {
        PasswordEncoder passwordEncoder = ApplicationContextProvider.bean(PasswordEncoder.class);
        this.email = data.getEmail();
        this.password = passwordEncoder.encode(data.getPassword());
        this.firstName = data.getFirstName();
        this.lastName = data.getLastName();
        this.role = Role.USER;
    }
}