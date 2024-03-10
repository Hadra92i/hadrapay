package com.hmconsulting.hadrapay.service;

import com.hmconsulting.hadrapay.entity.ERole;
import com.hmconsulting.hadrapay.entity.User;
import com.hmconsulting.hadrapay.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String phoneNumberOrEmail) throws UsernameNotFoundException {
        User user = userRepository.findByPhoneNumberOrEmail(phoneNumberOrEmail, phoneNumberOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with : " + phoneNumberOrEmail));

        Set<GrantedAuthority> authorities = user.getRoles()
                .stream()
                .map((role) -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toSet());

        return new org.springframework.security.core.userdetails.User(phoneNumberOrEmail, user.getPassword(), authorities);
    }
}
