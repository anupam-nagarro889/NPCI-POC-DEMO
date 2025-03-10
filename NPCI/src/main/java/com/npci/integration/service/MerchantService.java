package com.npci.integration.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.npci.integration.dto.MerchantDTO;
import com.npci.integration.exception.ResourceNotFoundException;
import com.npci.integration.mapper.MerchantMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.npci.integration.models.Merchant;
import com.npci.integration.repository.MerchantRepository;

@Slf4j
@Service
public class MerchantService {
	
	@Autowired
	private MerchantRepository repository;

	@Autowired
	private RedisService redisService;

	@Autowired
	private MerchantMapper merchantMapper;
	
	public List<MerchantDTO> getAllMerchant(){
	    return merchantMapper.merchantToDtoList(repository.findAll());
	}
	
	public MerchantDTO getMerchantByID(Long id) throws ResourceNotFoundException {

		Merchant merchant = redisService.get(id.toString(), Merchant.class);
		if(merchant != null) {
			log.info("Fetching data from Redis.");
			return merchantMapper.merchantToMerchantDto(merchant);
		}else {
			merchant = repository.findById(id)
					.orElseThrow(() -> new ResourceNotFoundException("Merchant not found for this id :: " + id));

			log.info("Fetching data from database");
			redisService.set(id.toString(), merchant, 500L);
			return merchantMapper.merchantToMerchantDto(merchant);
		}

	}
	
	public MerchantDTO addMerchantDetails(MerchantDTO merchantDto) {
		Merchant merchant = merchantMapper.merchantDtoToMerchant(merchantDto);
		log.info("Adding a new Merchant.");
		repository.save(merchant);
		//Adding to cache
		redisService.set(merchant.getId().toString(), merchant, 500L);
		return merchantMapper.merchantToMerchantDto(merchant);
	}
	
	public MerchantDTO updateMerchantData(MerchantDTO merchantDto) throws ResourceNotFoundException {

		Merchant merchantDetails = repository.findById(merchantDto.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Merchant not found for this id :: " + merchantDto.getId()));

		merchantDetails.setName(merchantDto.getName());
		merchantDetails.setCallbackUrl(merchantDto.getCallbackUrl());

		log.info("Updating a new Merchant.");
		Merchant updatedMerchant = repository.save(merchantDetails);

		// Update Redis cache
		redisService.delete(merchantDto.getId().toString());
		redisService.set(merchantDto.getId().toString(), updatedMerchant, 500L);

		log.info("Updated Merchant and refreshed Redis cache.");
		return merchantMapper.merchantToMerchantDto(updatedMerchant);
		
	}
	
	public Map<String, Boolean> deleteMerchant(Long id) throws ResourceNotFoundException {
		
		Merchant merchant = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Merchant not found for this id :: " + id));

		log.info("Deleting a Merchant.");
		repository.delete(merchant);

		//Delete the cache
		redisService.delete(id.toString());
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
		
	}



}
