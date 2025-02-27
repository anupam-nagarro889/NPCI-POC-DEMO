package com.npci.integration.mapper;

import com.npci.integration.dto.TransactionDTO;
import com.npci.integration.models.Transactions;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import java.util.List;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    TransactionMapper INSTANCE = Mappers.getMapper( TransactionMapper.class );

    @Mapping(source = "fromMerchant.name", target = "fromMerchant")
    @Mapping(source = "toMerchant.name", target = "toMerchant")
    TransactionDTO transactionToTransactionDto(Transactions transaction);

    List<TransactionDTO> transactionToDtoList(List<Transactions> transactions);

}
