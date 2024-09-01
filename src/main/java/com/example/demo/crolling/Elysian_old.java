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
import org.springframework.stereotype.Component;


import com.example.demo.interfaceKit.Convert;
import com.example.demo.model.Resort;
import com.example.demo.model.Slope;
import com.example.demo.service.server.ResortService;
import com.example.demo.service.server.SlopeService;


//@AllArgsConstructor
//@Component
public class Elysian_old implements CreateCheckTable {
	@Override
	public MultiKeyMap<String, Boolean> createCheckList(String name) {
		return null;
	}


	/*

	private ResortService resortService;
	private SlopeService slopeService;
	private SlopeRepository slopeRepository;
	
	//@Scheduled(fixedDelay = 30000)
	public void doCrolling() throws IOException {

		// 1. 기본 세팅
		
		// 한국 오늘 날짜 추출
		LocalDate date = LocalDate.now(ZoneId.of("Asia/Seoul"));
		
		for(int i=0; i<7; i++) {
			
			Document d = Jsoup.connect("https://www.elysian.co.kr/gangchon/ski/ski_slope.asp?search_ymd="+date).get();
			
			Elements table = d.select("table[summary='구분, 연계슬로프, 활주로거리, 경사도, 이용리프트, 일자별 슬로프 현황 상세정보']");
			Element tbody = table.select("tbody").first();
			Elements trElements = tbody.select("tr");// tr만 모음
			
			Resort resort=null;
			String resortName = "Elysian";
	
			
			
			// 리조트 등록
			if(resortService.findResort(resortName)!=null) {
				resort = resortService.findResort(resortName);
			}
			else{
				// 에러가 발생했다 -> 최초의 실행이다.
				// insert를 해준다.
				resort = new Resort();
				resort.setSlopeName(resortName);
				resortService.saveResort(resort);
			}
			
			
			
			// 람다를 이용한 상태 변경
			// /images/common/icon_open.gif -> OPEN
			// /images/common/icon_close.gif -> CLOSE
			Convert convert = (String input)->{
				if(input.equals("/images/common/icon_open.gif")) {
					return "OPEN";
				}
				else {
					return "CLOSE";
				}
			};
			
			// 변수 선언
			String difficulty="";
			String slopeName="";
			String day="";
			String night="";
			String dawn="";
	
			List<String> dataBox = new ArrayList<String>();
			
			// 2. 내용 추출 코드
			for(Element tr: trElements) {
				
				
				// <td> text출력
				Elements td = tr.select("td");
				for(Element e1:td) {
					dataBox.add(e1.text());
				}
				
				// <td>내 이미지 출력
	
				Elements imgTdElements = tr.select("td:has(img)");
				for (Element imgTd : imgTdElements) {
				    	Element imgTag = imgTd.selectFirst("img");
				    	String srcValue = imgTag.attr("src");
				    	dataBox.add(srcValue);
				}
				
	
				
				
	
				slopeName=dataBox.get(0);//이름
				difficulty=dataBox.get(2);//경사도 <- 난이도 대체
				day=convert.result(dataBox.get(7));//주간
				night=convert.result(dataBox.get(8));//야간
				dawn=convert.result(dataBox.get(9));
				
				Slope slope = new Slope()
						  .buildResort(resort)
						  .buildSlopeName(slopeName)
						  .buildDifficulty(difficulty)
						  .buildDay(day)
						  .buildNight(night)
						  .buildDawn(dawn)
						  .buildDate(date)
						  ;
				
				slopeService.update(slope,resort);
				dataBox.clear();
	
			}
			
			
			date=date.plusDays(1);

		}
		slopeService.deleteSlope();
		
	}

	public MultiKeyMap<String, Boolean> createCheckList() {
		List<Slope>  slopesFromDB = slopeRepository.findAll();//DB의 슬로프 이름을 가져온다.
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

	 */
	

}
