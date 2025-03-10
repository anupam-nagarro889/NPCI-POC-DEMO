package com.npci.integration.mapper;

import com.npci.integration.dto.UserDTO;
import com.npci.integration.models.Users;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AuthMapper {
    AuthMapper INSTANCE = Mappers.getMapper(AuthMapper.class);

    Users userDtoToUser(UserDTO userDTO);

}
