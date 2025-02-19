package com.npci.integration.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.npci.integration.exception.ResourceNotFoundException;
import com.npci.integration.models.Merchant;
import com.npci.integration.repository.MerchantRepository;

@Service
public class MerchantService {
	
	@Autowired
	private MerchantRepository repository;
	
	public List<Merchant> getAllMerchant(){
		
	    return repository.findAll();
		
	}
	
	public Merchant getMerchantByID(Long id) throws ResourceNotFoundException {
		Merchant merchant = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Merchant not found for this id :: " + id));
		
		return merchant;
		
	}
	
	public Merchant addMerchantDetails(Merchant merchant) {
		return repository.save(merchant);
		
	}
	
	public Merchant updateMerchantData(Long id, Merchant merchant) throws ResourceNotFoundException {
		
		Merchant merch = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Merchant not found for this id :: " + id));
		
		merch.setId(id);
		merch.setName(merchant.getName());
		merch.setCategory(merchant.getCategory());
		final Merchant updatedMerchant = repository.save(merch); 
		return updatedMerchant;
		
	}
	
	public Map<String, Boolean> deleteMerchant(Long id) throws ResourceNotFoundException {
		
		Merchant merchant = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Merchant not found for this id :: " + id));
		
		repository.delete(merchant);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
		
	}

}
