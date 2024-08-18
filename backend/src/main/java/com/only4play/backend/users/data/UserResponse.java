package com.only4play.backend.users.data;

import com.only4play.backend.users.Role;
import com.only4play.backend.users.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
public class UserResponse {

    private Long id;
    private Role role;
    private String firstName;
    private String lastName;
    private String email;
    private String profileImageUrl;

    public UserResponse(User user) {
        this.id = user.getId();
        this.role = user.getRole();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.profileImageUrl = user.getProfileImageUrl();
    }

}
