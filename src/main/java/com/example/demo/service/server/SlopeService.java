package com.example.demo.service.server;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.util.List;

import lombok.AllArgsConstructor;
import org.apache.commons.collections4.map.MultiKeyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Resort;
import com.example.demo.model.Slope;
import com.example.demo.repository.server.SlopeRepository;

import jakarta.transaction.Transactional;

@AllArgsConstructor
@Service
public class SlopeService {


	private SlopeRepository slopeRepository;



	@Transactional
	public void update(Slope slope,Resort resort, MultiKeyMap<String, Boolean> slopeCheckList) {

		// 크롤링한 데이터가 DB에 있다 -> true로 기록
		verifySlope(slope,slopeCheckList);
		
		try {
			// DB에 찾아보고 있으면 Dirty checking을 통한 업데이트를 한다.(update)
			// 아니면, 새로 추가된 슬로프이므로, DB에 추가를 한다.(insert)

			Slope orig = slopeRepository.findBySlopeNameAndResort(slope.getSlopeName(),resort);
			orig.setSlopeName(slope.getSlopeName());
			orig.setDifficulty(slope.getDifficulty());
			orig.setDay(slope.getDay());
			orig.setNight(slope.getNight());
			orig.setDawn(slope.getDawn());
			orig.setDate(slope.getDate());
			
		} catch (NullPointerException e) {
			slopeRepository.save(slope);

		}
	}

	private void verifySlope(Slope slope, MultiKeyMap<String, Boolean> slopeCheckList){
		String slopeName = slope.getSlopeName();
		String resortName = slope.getResortName();
		if(slopeCheckList.get(slopeName,resortName)!=null) {
			// 해당 데이터가 있다 -> true로 바꾸자
			slopeCheckList.put(slopeName, resortName, true);
		}

	}

	@Transactional
	public void clearUncheckedSlope(MultiKeyMap<String, Boolean> slopeCheckList,Resort resort) {

		slopeCheckList.forEach((keys,value) -> {
			String slopeName = keys.getKey(0);
			System.out.println(slopeName+" : "+value);
			// 기존 슬로프에서 사용안하는 부분이 삭제되었다.
			if(!value) {
				// 자기네들 리조트만 각각 삭제를 한다. -> 맞는 로직
				slopeRepository.deleteBySlopeNameAndResort(slopeName, resort);


			}
		});
}

	//////////////////////////////////////////////////////////////////////////////////////

	// 초기 설정
	/*
	@Autowired
	private SlopeRepository slopeRepository;
	//Map<String, Boolean> isSlopeInlist;
	MultiKeyMap<String, Boolean> isSlopeInlist;

	List<Slope> lst;
	Boolean init=true;
	Resort resort = null;
	 */



	/*
	@Transactional
	public void update(Slope slope,Resort resort) {
		String slopeName = slope.getSlopeName();
		this.resort = resort;
		
		if(init) {
			initMap();
		}
		// Slope가 있으면 true로 초기화를 한다.
		isSlopein(slope);

		try {
			// DB에 찾아보고 있으면 Dirty checking을 통한 업데이트를 한다.(update)
			// 아니면, 새로 추가된 슬로프이므로, DB에 추가를 한다.(insert)
			
			Slope orig = slopeRepository.findBySlopeNameAndResortAndDate(slopeName,resort,slope.getDate());
			orig.setSlopeName(slope.getSlopeName());
			orig.setDifficulty(slope.getDifficulty());
			orig.setDay(slope.getDay());
			orig.setNight(slope.getNight());
			orig.setDawn(slope.getDawn());
			orig.setDate(slope.getDate());
			
			
		} catch (NullPointerException e) {
			//isSlopeInlist.put(slope.getSlopeName(), true);
			isSlopeInlist.put(slope.getSlopeName(), slope.getDate().toString(),true);
			slopeRepository.save(slope);

		}
	}
	
	
	// 3. Map 초기화
	// DB의 슬로프 이름을 가져온다.
	private void initMap() {
		lst = slopeRepository.findAll();//DB의 슬로프 이름을 가져온다.
		//isSlopeInlist = new HashMap<String,Boolean>();
		isSlopeInlist = new MultiKeyMap<>();
		
		
		// Map 초기화
		for(Slope g: lst) {
			//isSlopeInlist.put(g.getSlopeName(), false);
			
			// 슬로프명,날짜(LocalDate -> String),false
			isSlopeInlist.put(g.getSlopeName(), g.getDate().toString(), false);
		}
		init=false; // 이거 설정안하면 슬로프 정보 매 확인마다 Map 만들고 지우는 과정을 반복한다.
		
	}
	
	//3-1. 슬로프가 있나요?
	private void isSlopein(Slope slope) {
		// 슬로프명, 날짜 두개를 이용해서 확
		String slopeName = slope.getSlopeName();
		String date = slope.getDate().toString();
		
		if(isSlopeInlist.get(slopeName,date)!=null) {
			// 해당 데이터가 있다 -> true로 바꾸자
			isSlopeInlist.put(slopeName, date, true);
		}	
	}
	
	//3-2. 존재하지 않는 슬로프만 삭제를 한다.
	// 언제 함? -> 위 insert, update과정 다 끝난 후 진행
	@Transactional
	public void deleteSlope() {
		isSlopeInlist.forEach((keys,value) -> {
			String slopeName = keys.getKey(0);
			String getDate = keys.getKey(1);
			
			// 기존 슬로프에서 사용안하는 부분이 삭제되었다.
			if(value==false) {
		        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		        // Parse the String to LocalDateTime
		        LocalDate date= LocalDate.parse(getDate, formatter);


				slopeRepository.deleteBySlopeNameAndResortAndDate(slopeName, resort, date);
			}
		});
		init=true; // 다음 사이클을 위해서 초기화를 한다.
	}

	 */
	
	// 날짜 관련 로직
	/*
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		// Parse the String to LocalDateTime
		LocalDate date= LocalDate.parse(getDate, formatter);
				
	*/	

	
}
