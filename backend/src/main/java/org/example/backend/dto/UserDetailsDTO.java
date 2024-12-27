package org.example.backend.dto;

import lombok.*;
import org.example.backend.enums.UserStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDetailsDTO {

    String username;
    String role;
    String status;
}
