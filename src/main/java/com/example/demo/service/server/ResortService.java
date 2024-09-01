package com.example.demo.service.server;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Resort;
import com.example.demo.repository.server.ResortRepository;

@Service
public class ResortService {
	@Autowired
	private ResortRepository resortRepository;
	
	public void saveResort(Resort  resort) {
		resortRepository.save(resort);
	}
	
	public Resort findResort(String resortName) {
		return resortRepository.findByResortName(resortName);
	}
	
}
