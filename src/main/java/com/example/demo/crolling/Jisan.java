package com.example.demo.crolling;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import com.example.demo.repository.server.SlopeRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.map.MultiKeyMap;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.demo.interfaceKit.Convert;
import com.example.demo.model.Resort;
import com.example.demo.model.Slope;
import com.example.demo.service.server.ResortService;
import com.example.demo.service.server.SlopeService;

@AllArgsConstructor
@Component
public class Jisan implements CreateCheckTable {

	private ResortService resortService;
	private SlopeService slopeService;
	private SlopeRepository slopeRepository;
	
	// 1. 이름과 난이도 추출함수
	private List<String> extractElement1(List<WebElement> trList2,int i) {
		
		// 변수 선언
		WebElement extractList = null;
		List<WebElement> slope = null;
		List<String> returnSlope = null;
		
		// 본 코드
		extractList = trList2.get(i);
	    slope = extractList.findElements(By.tagName("th"));
	    
	    if(i==0) slope.remove(0);
	    
		returnSlope = new ArrayList<>();
		for(WebElement w: slope) {
			returnSlope.add(w.getText());
		}
		
		return returnSlope;
	}
	
	// 2. 주간, 야간, 심야 (시간대 추출 함수)
	private List<String> extractElement2(WebElement slopeStatus, Convert convert){
		List<String> resultList = new ArrayList<>();
		List<WebElement> tds2 = slopeStatus.findElements(By.cssSelector("td:has(img)"));
		for (WebElement imgTd : tds2) {
		    WebElement imgTag = imgTd.findElement(By.tagName("img"));
		    convert.result(imgTag.getAttribute("src"));
		    resultList.add(convert.result(imgTag.getAttribute("src")));
		}
		return resultList;
	}
	
	@Scheduled(fixedDelay = 30000)
	public void doCrolling() throws IOException {
		
		// 한국 오늘 날짜 추출
		LocalDate date = LocalDate.now(ZoneId.of("Asia/Seoul"));
		// 0. 기본 세팅
		System.setProperty("webdriver.chrome.driver", "C:\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");
		
		// 실행할때마다 브라우저가 안 뜨도록 추가 설정
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--headless");  // Run Chrome in headless mode (without opening the browser window)
		
		WebDriver driver= new ChromeDriver(options);
		
		try {
			driver.get("https://www.jisanresort.co.kr/w/ski/slopes/info.asp");
			
			
			// 0-1. 리조트 등록
			Resort resort=null;
			String resortName = "Jisan";

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
			
			// 1. 변수선언 
			Convert convert = (String input)->{
				// "https://www.jisanresort.co.kr/w/asset/images/sub/ski/slope_close.png" <- CLOSE
				// "https://www.jisanresort.co.kr/w/asset/images/sub/ski/slope_open.png" <- OPEN
				if(input.equals("https://www.jisanresort.co.kr/w/asset/images/sub/ski/slope_open.png")) {
					return "OPEN";
				}
				return "CLOSE";
			};//png내 OPEN,CLOSE 속성을 추출하기 위한 람다식.
			
			
			// 1-1. thead를 가져온다.(슬로프명, 난이도 추출 목적)
			WebElement thead = driver.findElement(By.tagName("thead"));
			
			// tr을 분리하자. 0번째 -> 슬로프명, 1번째 -> 난이도
			List<WebElement> trList = thead.findElements(By.tagName("tr"));
			
			// 슬로프명 추출, 난이도 추출
			List<String>slopeNameList = this.extractElement1(trList, 0);
			List<String>slopeDifficultyList = this.extractElement1(trList, 1);
			
	
			
			// 1-2. tbody를 가져온다.(주간, 야간, 심야 추출 목적)
			WebElement tbody = driver.findElement(By.tagName("tbody"));
			List<WebElement> trList2 = tbody.findElements(By.tagName("tr"));
			
			// 주간, 야간 심야, 추출
			List<String>dayList = extractElement2(trList2.get(0), convert);
			List<String>nightList = extractElement2(trList2.get(1), convert);
			List<String>dawnList = extractElement2(trList2.get(2), convert);
			
			
			// 2. 슬로프 저장 및 수정
			for(int i=0; i<slopeNameList.size(); i++) {
				Slope slope = Slope.builder()
					      .resort(resort)
						  .resortName(resortName)
						  .slopeName(slopeNameList.get(i))
						  .difficulty(slopeDifficultyList.get(i))
						  .day(dayList.get(i))
						  .night(nightList.get(i))
						  .dawn(dawnList.get(i))
						  .date(date)
						  .build()
						  ;
				slopeService.update(slope,resort,slopeCheckList);
			}
			slopeService.clearUncheckedSlope(slopeCheckList,resort);
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			driver.quit();
		}
		
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
