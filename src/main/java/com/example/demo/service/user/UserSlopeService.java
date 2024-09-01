package com.example.demo.service.user;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Resort;
import com.example.demo.model.Slope;
import com.example.demo.repository.user.UserResortRepository;
import com.example.demo.repository.user.UserSlopeRepository;

import jakarta.transaction.Transactional;

@Service
public class UserSlopeService {
	
	@Autowired
	private UserSlopeRepository userSlopeRepository;
	
	@Autowired
	private UserResortRepository userResortRepository;
	
	// 1. 특정 리조트의 모든 슬로프 가져오기
	@Transactional
	public List<Slope> getRelatedSlopes(int id) {
		List<Slope> slopeList = userSlopeRepository.findByResort_id(id);
		return slopeList;
	}
	
	// 3. 특정 id에 해당하는 리조트의 슬로프 조회 + 날짜정보
	@Transactional
	public List<Slope> getRelatedSlopesWithDate(Resort resort,String date){
		
		System.out.println("Resort: "+resort.getResortName());
        // Define the format of the input String
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Parse the String to LocalDateTime
        LocalDate localDate= LocalDate.parse(date, formatter);


		List<Slope> slopeList = userSlopeRepository.findByResortAndDate(resort,localDate);
		for(Slope s: slopeList) {
			System.out.println(s.getSlopeName()+": "+s.getDate());
		}
		return slopeList;
	}
	

}
