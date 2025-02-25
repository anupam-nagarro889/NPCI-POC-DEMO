package com.npci.integration.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.io.Serializable;

import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "merchants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Merchant implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String merchantCode;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String callbackUrl;

	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt = LocalDateTime.now();
}

