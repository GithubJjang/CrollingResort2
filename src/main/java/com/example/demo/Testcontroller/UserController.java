package com.example.demo.Testcontroller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.SlopeDTO;
import com.example.demo.model.Resort;
import com.example.demo.model.Slope;
import com.example.demo.service.server.ResortService;
import com.example.demo.service.server.SlopeService;
import com.example.demo.service.user.UserResortService;
import com.example.demo.service.user.UserSlopeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class UserController {
	
	@Autowired
	private UserSlopeService userSlopeService;
	
	@Autowired
	private UserResortService userResortService;
	
	@GetMapping(path = "/resorts")
	public void showResortList() throws JsonProcessingException {
		// 국내 모든 스키장 리조트 보여주기.
		// 버튼을 클릭하면, 해당 리조트의 id를 넘겨준다.(수정)
		/*
		List<Resort>findAllResorts=userResortService.getResorts();
		ObjectMapper toJson = new ObjectMapper();
		String json = toJson.writeValueAsString(findAllResorts);
		return json;

		 */
	}
	
	@GetMapping(path ="/resorts/{id}")
	public void getResort(@PathVariable(value = "id") int resortId) throws JsonProcessingException {
		// 특정 id에 해당하는 리조트만 가져오기.
		// 리조트명 + id만 가지고 있는 상태 (수정)
		/*
		Resort resort = userResortService.getResort(resortId);
		ObjectMapper obj = new ObjectMapper();
		String json = obj.writeValueAsString(resort);
		
		return json;

		 */
	}
	
	@GetMapping(path="/resorts/{id}/slopes")
	public void getSlopeInfo(
			@RequestParam(value = "day")String day,
			@PathVariable(value = "id") int resortId) throws JsonProcessingException {
			// resortId  <- 특정리조트 조회 목적
			// day <- 특정 날짜의 슬로프 조회 목적.
		/*
		System.out.println(day);
		System.out.println(resortId);
		Resort resort = userResortService.getResort(resortId);
		List<Slope> rawSlopeList = userSlopeService.getRelatedSlopesWithDate(resort, day);
		
		// 수정<-필요
		
		List<SlopeDTO> slopeList = new ArrayList<>();
		for(Slope s: rawSlopeList) {
			slopeList.add(SlopeDTO.builder()
										.(s.getResort().getId())
										.buildDifficulty(s.getDifficulty())
										.buildSlopeName(s.getSlopeName())
										.buildDay(s.getDawn())
										.buildNight(s.getNight())
										.buildDawn(s.getDawn())
										.buildDate(s.getDate())			
			);
		}
		
		ObjectMapper obj = new ObjectMapper();
		String json = obj.writeValueAsString(slopeList);
		
		return json;

		 */
	}
}
