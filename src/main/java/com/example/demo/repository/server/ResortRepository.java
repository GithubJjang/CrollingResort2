package com.example.demo.repository.server;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Resort;

public interface ResortRepository extends JpaRepository<Resort, Integer> {
	Resort findByResortName(String resortName);

}
