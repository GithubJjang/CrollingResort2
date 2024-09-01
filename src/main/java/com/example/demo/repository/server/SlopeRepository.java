package com.example.demo.repository.server;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Resort;
import com.example.demo.model.Slope;

public interface SlopeRepository extends JpaRepository<Slope, Integer> {
	Slope findBySlopeNameAndResort(String slopeName, Resort resort);
	List<Slope> findByResortName(String resortName);// checkList 용도
	void deleteBySlopeNameAndResort(String key, Resort resort);
}

