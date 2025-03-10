package com.npci.integration.service;


import com.npci.integration.exception.ResourceNotFoundException;
import com.npci.integration.models.Roles;
import com.npci.integration.models.UserRoles;
import com.npci.integration.models.Users;
import com.npci.integration.repository.RoleRepository;
import com.npci.integration.repository.UserRepository;
import com.npci.integration.repository.UserRoleRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;

    public CustomUserDetailsService(UserRepository userRepository, UserRoleRepository userRoleRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(getRolesListForUser(user).stream().map(Roles::getName).toArray(String[]::new))
                .build();
    }
    private Set<Roles> getRolesListForUser(Users user){

        List<UserRoles> userRoleList = userRoleRepository.findAllByUserId(user.getId());
        Set<Roles> rolesList = userRoleList.stream()
                .map(userRoles -> {
                    return roleRepository.findById(userRoles.getRoleId())
                            .orElseThrow(() -> new ResourceNotFoundException("Role not found."));
                }).collect(Collectors.toSet());
        return rolesList;
    }
}
