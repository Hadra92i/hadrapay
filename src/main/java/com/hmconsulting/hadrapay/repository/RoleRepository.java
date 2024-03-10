package com.hmconsulting.hadrapay.repository;

import com.hmconsulting.hadrapay.entity.ERole;
import com.hmconsulting.hadrapay.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
