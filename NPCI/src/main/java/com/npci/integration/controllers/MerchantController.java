package com.npci.integration.controllers;

import java.util.List;
import java.util.Map;

import com.npci.integration.dto.MerchantDTO;
import com.npci.integration.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.npci.integration.models.Merchant;
import com.npci.integration.service.MerchantService;
import com.npci.integration.service.RedisService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/rest/service/merchant/api")
public class MerchantController {

	@Autowired
	private RedisService redisService;

	@Autowired
	private MerchantService merchantService;

	@GetMapping("/getAllMerchants")
	public List<MerchantDTO> getMerchants() {
		return merchantService.getAllMerchant();

	}

	@GetMapping("/getMerchant/{id}")
	public ResponseEntity<MerchantDTO> getMerchantById(@PathVariable(value = "id") Long id)
			throws ResourceNotFoundException {
		return ResponseEntity.ok(merchantService.getMerchantByID(id));
	}

	@PostMapping("/addMerchant")
	public MerchantDTO addMerchant(@Valid @RequestBody MerchantDTO merchantDTO) {
		return merchantService.addMerchantDetails(merchantDTO);
	}
	
	@PutMapping("/updateMerchant")
	public ResponseEntity<MerchantDTO> updateMerchantDetails(@Valid @RequestBody MerchantDTO merchantDTO) throws ResourceNotFoundException {
		return ResponseEntity.ok(merchantService.updateMerchantData(merchantDTO));
	}
	
	@DeleteMapping("/deleteMerchant/{id}")
	public Map<String, Boolean> deleteMerchant(@PathVariable(value = "id") Long Id) throws ResourceNotFoundException {
		return merchantService.deleteMerchant(Id);
		
	}


}
