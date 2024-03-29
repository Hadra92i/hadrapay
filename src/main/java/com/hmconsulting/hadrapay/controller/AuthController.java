package com.hmconsulting.hadrapay.controller;

import com.hmconsulting.hadrapay.dto.request.LoginDto;
import com.hmconsulting.hadrapay.dto.request.SignUpDto;
import com.hmconsulting.hadrapay.dto.response.LoginResponseDto;
import com.hmconsulting.hadrapay.dto.response.SignUpResponseDto;
import com.hmconsulting.hadrapay.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginDto loginDto) {
        ResponseEntity<LoginResponseDto> responseEntity = authService.login(loginDto);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.ok(responseEntity.getBody());
        }

        return responseEntity;
    }

    @PostMapping("/register")
    public ResponseEntity<SignUpResponseDto> register(@RequestBody SignUpDto signUpDto){
       ResponseEntity<SignUpResponseDto> responseEntity = authService.register(signUpDto);

       if(responseEntity != null && responseEntity.getBody() != null){
           SignUpResponseDto responseDto = responseEntity.getBody();
           return ResponseEntity.status(responseEntity.getStatusCode()).body(responseDto);
       }

       return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new SignUpResponseDto("An error occurred during registration !"));
    }
}
