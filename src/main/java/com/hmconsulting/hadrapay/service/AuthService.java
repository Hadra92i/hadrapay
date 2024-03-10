package com.hmconsulting.hadrapay.service;

import com.hmconsulting.hadrapay.dto.request.LoginDto;
import com.hmconsulting.hadrapay.dto.request.SignUpDto;
import com.hmconsulting.hadrapay.dto.response.LoginResponseDto;
import com.hmconsulting.hadrapay.dto.response.SignUpResponseDto;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<LoginResponseDto> login(LoginDto loginDto);
    ResponseEntity<SignUpResponseDto> register(SignUpDto signUpDto);
}
