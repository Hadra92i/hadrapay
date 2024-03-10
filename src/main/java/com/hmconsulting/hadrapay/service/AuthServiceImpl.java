package com.hmconsulting.hadrapay.service;

import com.hmconsulting.hadrapay.dto.request.LoginDto;
import com.hmconsulting.hadrapay.dto.request.SignUpDto;
import com.hmconsulting.hadrapay.dto.response.LoginResponseDto;
import com.hmconsulting.hadrapay.dto.response.SignUpResponseDto;
import com.hmconsulting.hadrapay.entity.ERole;
import com.hmconsulting.hadrapay.entity.Role;
import com.hmconsulting.hadrapay.entity.User;
import com.hmconsulting.hadrapay.repository.RoleRepository;
import com.hmconsulting.hadrapay.repository.UserRepository;
import com.hmconsulting.hadrapay.security.jwt.JwtUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService{
    private AuthenticationManager authenticationManager;

    private PasswordEncoder passwordEncoder;
    private JwtUtils jwtUtils;

    private UserRepository userRepository;

    private RoleRepository roleRepository;

    @Override
    public ResponseEntity<LoginResponseDto> login(LoginDto loginDto) {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginDto.getPhoneNumberOrEmail(), loginDto.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String token = jwtUtils.generateJWT(authentication);
            String role = extractRole(authentication);
            LoginResponseDto responseDto = new LoginResponseDto(token,role,"You are logged in successfully !");

            return ResponseEntity.ok(responseDto);
        }catch(AuthenticationException e){
            LoginResponseDto responseDto = new LoginResponseDto(null,null, "Connection failed: Verify your entries !");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseDto);
        }
    }

    private String extractRole(Authentication authentication){
        return authentication.getAuthorities()
                .stream()
                .filter(authority ->authority instanceof SimpleGrantedAuthority)
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse(null);
    }
    @Override
    public ResponseEntity<SignUpResponseDto> register(SignUpDto signUpDto) {
       try{
           if(userRepository.existsByPhoneNumber(signUpDto.getPhoneNumber())){
               return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                       .body(new SignUpResponseDto("The phone number is already taken !"));
           }

           if(userRepository.existsByEmail(signUpDto.getEmail())){
               return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                       .body(new SignUpResponseDto("The email is already taken !"));
           }

           User user = new User();
           user.setFullName(signUpDto.getFullName());
           user.setEmail(signUpDto.getEmail());
           user.setNni(signUpDto.getNni());
           user.setPhoneNumber(signUpDto.getPhoneNumber());
           user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));

           Role role = roleRepository.findByName(ERole.valueOf(signUpDto.getRole())).orElseThrow(() -> new RuntimeException("Role not found !"));
           user.setRoles(Collections.singleton(role));

           userRepository.save(user);

           return ResponseEntity.status(HttpStatus.CREATED).body(new SignUpResponseDto("User registered successfully !"));
       }catch(AuthenticationException e){
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new SignUpResponseDto("Error occurred during registration !"));
       }
    }
}
