package com.npci.integration.mapper;

import com.npci.integration.dto.MerchantDTO;
import com.npci.integration.models.Merchant;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MerchantMapper {

    MerchantMapper INSTANCE = Mappers.getMapper(MerchantMapper.class);

    MerchantDTO merchantToMerchantDto(Merchant merchant);
    List<MerchantDTO> merchantToDtoList(List<Merchant> merchant);

    Merchant merchantDtoToMerchant(MerchantDTO merchantDTO);
}
