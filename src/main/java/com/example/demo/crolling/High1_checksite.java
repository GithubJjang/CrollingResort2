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

@AllArgsConstructor
@Component
public class High1_checksite implements CreateCheckTable {

	private ResortService resortService;
	private SlopeService slopeService;
	private SlopeRepository slopeRepository;
	
	//@Scheduled(fixedDelay = 30000)
	public void test() throws IOException {
		
		// 한국 오늘 날짜 추출
		LocalDate date = LocalDate.now(ZoneId.of("Asia/Seoul"));
		//위리휄리 안됨
		//
		Document d = Jsoup.connect("https://www.high1.com/ski/slopeView.do?key=748&mode=p").get();
		//System.out.println(d);
		Element tbody = d.select("tbody.text_center").first();
		
		Elements trElements = tbody.select("tr");// tr만 모음
		Elements tdElements = null;// td만 모음
		
		Resort resort=null;
		String resortName = "HighOne";

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
		String slopeTag="";
		
		
		
		//이름을 제외한 나머지 정보
		ArrayList<String> dataBox = new ArrayList<>();
		
		
		for(Element e1 : trElements) {
			// tr내부 td를 추출한다.
			tdElements = e1.select("td");
			
			
			// 1. 슬로프 이름
			// 만약 새로운 요소를 만나면, 갱신을 함.
			if(tdElements.select("td[rowspan]").first()!=null) {
				slopeName = e1.select("td[rowspan]").text();
				// 이름 추출 후 삭제하기
				tdElements.remove(0);
			}
			
			// 2. 이름을 제외한 나머지 요소들 저장하기
			for(Element e2 : tdElements) {
				dataBox.add(e2.text());
			}
			
			// 2-1. 각 요소들을 맵핑하기
			//slopeName = ""; <- 이미 1.슬로프 이름 부분에서 추출함.
			slopeTag = dataBox.get(0);
			day = dataBox.get(1);
			night = dataBox.get(2);
			difficulty = dataBox.get(3);
			dawn="";
			slopeName = slopeName+" "+slopeTag;// 나중에 파싱해써 쓰느가 하자.


			Slope slope = Slope.builder()
					.resort(resort)
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
	public MultiKeyMap<String, Boolean> createCheckList(String name) {
		List<Slope> slopesFromDB = slopeRepository.findAll();//DB의 슬로프 이름을 가져온다.
		MultiKeyMap<String, Boolean> slopeCheckList =  new MultiKeyMap<>();
		//isSlopeInlist = new HashMap<String,Boolean>();


		for(Slope g: slopesFromDB) {
			//isSlopeInlist.put(g.getSlopeName(), false);

			// 슬로프명,날짜(LocalDate -> String),false
			slopeCheckList.put(g.getSlopeName(), g.getDate().toString(), false);
		}


		slopesFromDB.stream()
				.map((e)->
						slopeCheckList.put(e.getSlopeName(), e.getDate().toString(), false)
				)
				.close();


		System.out.println("SlopeCheckList 사이즈 : "+slopeCheckList.size());

		return slopeCheckList;

	}

	
	
	

}
