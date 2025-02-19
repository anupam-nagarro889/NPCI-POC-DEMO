package com.npci.integration.controllers;

import java.util.List;
import java.util.Map;

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

import com.npci.integration.dao.MerchantDao;
import com.npci.integration.exception.ResourceNotFoundException;
import com.npci.integration.models.Merchant;
import com.npci.integration.service.MerchantService;
import com.npci.integration.service.RedisService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/rest/service/merchant")
public class MerchantController {

	@Autowired
	private MerchantDao merchantDao;
	
	@Autowired
	private RedisService redisService;

	@Autowired
	private MerchantService merchantService;

	@GetMapping("/api/creatTable")
	public void creatTableInDB() {
		this.merchantDao.creatTable();

	}

	@GetMapping("/api/getAllMerchants")
	public List<Merchant> getMerchants() {
		return merchantService.getAllMerchant();

	}

	@GetMapping("/api/getMerchant/{id}")
	public ResponseEntity<Merchant> getMerchantById(@PathVariable(value = "id") Long id)
			throws ResourceNotFoundException {
     Merchant merchant = redisService.get(id, Merchant.class);
     if(merchant != null) {
    	 System.out.println("Retrieving data from redis...");
    	 return ResponseEntity.ok().body(merchant);
		
     }else {
    	 
    	 Merchant merchant2 = merchantService.getMerchantByID(id);
    	 System.out.println("Retreivng data from database...");
    	 redisService.set(id, merchant2, 500l);
    	 
    	 return ResponseEntity.ok().body(merchant2);
     }
	}

	@PostMapping("/api/addMerchant")
	public Merchant addMerchant(@Valid @RequestBody Merchant merchant) {
		return merchantService.addMerchantDetails(merchant);
	}
	
	@PutMapping("/api/updateMerchant/{id}")
	public ResponseEntity<Merchant> updateMerchantDetails(@PathVariable(value = "id") Long id,
			@Valid @RequestBody Merchant merchant) throws ResourceNotFoundException {
		return ResponseEntity.ok(merchantService.updateMerchantData(id, merchant));
		
	}
	
	@DeleteMapping("/api/deleteMerchant/{id}")
	public Map<String, Boolean> deleteMerchant(@PathVariable(value = "id") Long Id) throws ResourceNotFoundException {
		return merchantService.deleteMerchant(Id);
		
	}

}
