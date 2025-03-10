package com.npci.integration.service;

import com.npci.integration.dto.UserDTO;
import com.npci.integration.exception.BadRequestException;
import com.npci.integration.mapper.AuthMapper;
import com.npci.integration.models.UserRoles;
import com.npci.integration.models.Users;
import com.npci.integration.repository.RoleRepository;
import com.npci.integration.repository.UserRepository;
import com.npci.integration.repository.UserRoleRepository;
import com.npci.integration.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;


@Slf4j
@Service
public class AuthService {

    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;
    private final AuthMapper authMapper;
    private final PasswordEncoder encoder;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    public AuthService(UserRoleRepository userRoleRepository, RoleRepository roleRepository, AuthMapper authMapper, PasswordEncoder encoder, UserRepository userRepository, AuthenticationManager authenticationManager, CustomUserDetailsService userDetailsService, JwtUtil jwtUtil) {
        this.userRoleRepository = userRoleRepository;
        this.roleRepository = roleRepository;
        this.authMapper = authMapper;
        this.encoder = encoder;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @Transactional(rollbackFor = Exception.class)
    public Long register(UserDTO userDto) {
        userDto.setPassword(encoder.encode(userDto.getPassword()));
        Users user = authMapper.userDtoToUser(userDto);
        try {
            userRepository.save(user);
        }catch(Exception e){
            log.error("Failed to save user.");
        }
        userDto.setId(user.getId());
        saveUserRoles(userDto);
        return user.getId();
    }

    private void saveUserRoles(UserDTO userDto){
        if(userDto.getRoleDTOList() == null || userDto.getRoleDTOList().isEmpty()){
            throw new BadRequestException("Please enter a valid role.");
        }

        userDto.getRoleDTOList().forEach(roleDto ->{
            UserRoles userRoles = new UserRoles();
            userRoles.setRoleId(roleDto.getId());
            userRoles.setUserId(userDto.getId());
            userRoleRepository.save(userRoles);
        });

    }

    public Map<String, String> login(UserDTO userDTO) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                userDTO.getEmail(), userDTO.getPassword()));

        UserDetails userDetails = userDetailsService.loadUserByUsername(userDTO.getEmail());
        String token = jwtUtil.generateToken(userDetails);

        return Map.of("token", token);
    }
}
