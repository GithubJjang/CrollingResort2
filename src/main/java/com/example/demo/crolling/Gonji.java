package com.example.demo.crolling;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import com.example.demo.repository.server.SlopeRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.map.MultiKeyMap;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.demo.model.Resort;
import com.example.demo.model.Slope;
import com.example.demo.service.server.ResortService;
import com.example.demo.service.server.SlopeService;

// S.H.D 버전2
@AllArgsConstructor
@Component
// 9개
public class Gonji implements CreateCheckTable {

	private ResortService resortService;
	private SlopeService slopeService;
	private SlopeRepository slopeRepository;
	
	@Scheduled(fixedDelay = 30000)
	public void doCrolling() throws IOException {
		
		// 한국 오늘 날짜 추출
		LocalDate date = LocalDate.now(ZoneId.of("Asia/Seoul"));
		
		Document d = Jsoup.connect("https://www.konjiamresort.co.kr/ski/slopeOpenClose.dev").get();
		// tbody 추출
		Element tbody = d.select("tbody").first();
		Elements trElements = tbody.select("tr");// tr만 모음
		Elements tdElements = null;// td만 모음
		
		Resort resort=null;
		String resortName = "Gonji";

		MultiKeyMap<String, Boolean> slopeCheckList = createCheckList(resortName);

		

		if(resortService.findResort(resortName)!=null) {
			resort = resortService.findResort(resortName);
		}
		else{
			// 에러가 발생했다 -> 최초의 실행이다.
			// insert를 해준다.
			resort = new Resort();
			resort.setResortName(resortName);
			resortService.saveResort(resort);
		}
		
		String slopeName="";//0번
		String difficulty="";//1번
		String day="";//2번
		String night="";//3번
		String dawn="";//4번
		
		// 추출한 정보를 담기위한 공간
		ArrayList<String> dataBox = new ArrayList<>();
		
		for(Element e1: trElements) {
			tdElements = e1.select("td");// td끼리 묶기
			
			for(Element e2: tdElements) {
				dataBox.add(e2.text());
			}
			slopeName=dataBox.get(0);
			difficulty=dataBox.get(1);
			day=dataBox.get(2);
			night=dataBox.get(3);
			dawn=dataBox.get(4);

			Slope slope = Slope.builder()
					.resort(resort)
					.resortName(resortName)
					.slopeName(slopeName)
					.difficulty(difficulty)
					.day(day)
					.night(night)
					.dawn(dawn)
					.date(date)
					.build()
					;

			slopeService.update(slope,resort,slopeCheckList);
			dataBox.clear();
			
		}
		slopeService.clearUncheckedSlope(slopeCheckList,resort);
	}

	public MultiKeyMap<String, Boolean>  createCheckList(String resort) {
		List<Slope>  slopesFromDB = slopeRepository.findByResortName(resort);//DB의 슬로프 이름을 가져온다.
		MultiKeyMap<String, Boolean> slopeCheckList =  new MultiKeyMap<>();

		for(Slope s: slopesFromDB) {
			slopeCheckList.put(s.getSlopeName(),s.getResortName(), false);
		}

		return slopeCheckList;
	}
}
