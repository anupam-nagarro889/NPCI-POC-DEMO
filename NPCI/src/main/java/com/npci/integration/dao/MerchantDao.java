package com.npci.integration.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class MerchantDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	//this method is used to create table in DB
	public void creatTable(){
		
		String query = "Create table merchant(id serial primary key, name varchar(255) not null, category varchar(255))";
		
		int response = this.jdbcTemplate.update(query);
		System.out.println(response);

	}
	

}
