package com.hmconsulting.hadrapay.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignUpDto {
    private String fullName;
    private String email;
    private String nni;
    private String phoneNumber;
    private String password;
    private String role;
}
