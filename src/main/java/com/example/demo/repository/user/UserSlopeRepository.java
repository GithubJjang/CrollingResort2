package com.example.demo.repository.user;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Resort;
import com.example.demo.model.Slope;

public interface UserSlopeRepository extends JpaRepository<Slope, Integer> {
	// 해당 리조트의 모든 슬로프 조회
	List<Slope> findByResort_id(int resort_id);
	List<Slope> findByResortAndDate(Resort resort, LocalDate localDate);

}
