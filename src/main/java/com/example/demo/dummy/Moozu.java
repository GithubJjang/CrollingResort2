package com.example.demo.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


import com.example.demo.model.MoozuSlope;
import com.example.demo.model.Resort;
import com.example.demo.service.server.ResortService;
import com.example.demo.service.server.SlopeService;


//@Component
public class Moozu {
	
	@Autowired
	private ResortService resortService;
	
	@Autowired
	private SlopeService slopeService;
	
	//@Scheduled(fixedDelay = 15000)
	public void doCrolling() {
		System.setProperty("webdriver.chrome.driver", "C:\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");
		
		// 실행할때마다 브라우저가 안 뜨도록 추가 설정
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--headless");  // Run Chrome in headless mode (without opening the browser window)
		options.addArguments("--remote-allow-orgins=*");
		// 기본설정
		WebDriver driver= new ChromeDriver(options);
		
		
		driver.get("https://www.mdysresort.com/convert_main_slope_221207.asp");
		//System.out.println(driver.getPageSource());
		
		
		// 리조트 생성
		Resort resort=null;
		String resortName = "Moozu";

		

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
		
		
		// 1. 슬로프 이름 추출
		WebElement slopeNameElements = driver.findElement(By.cssSelector("article.point ul"));
		List<WebElement> slopeNameList = slopeNameElements.findElements(By.tagName("li"));
		
		// 딕셔너리를 사용하자 -> Map구조를 이용하자.
		Map<String, MoozuSlope> SlopeList = new HashMap<>();

		for(WebElement e: slopeNameList) {
			WebElement e2 = e.findElement(By.tagName("span"));
			
			String rawSlopeName = e2.getAttribute("innerHTML");	
			String slopeName =	rawSlopeName.substring(4,rawSlopeName.length()-3);// last는 포함안함.
			MoozuSlope mSlope = new MoozuSlope();
			mSlope.setSlopeName(slopeName);
			SlopeList.put(slopeName, mSlope);
			
			//System.out.println(slopeName);
		}
		//System.out.println();
		// 슬로프명: 스키학교 강습장
		// 다시 추가: [스키학교 강습장_] -> [스키학교 강습장], 혼자 공백이 있음.
		SlopeList.remove("스키학교 강습장 ");
		
		MoozuSlope addSlope = new MoozuSlope();
		addSlope.setSlopeName("스키학교 강습장");
		SlopeList.put("스키학교 강습장", addSlope);
		
		
		// 2. 슬로프 오픈 정보 추출
		WebElement courseList = driver.findElement(By.className("course_list"));
		
		// ul리스트(슬로프 정보) 추출한다. -> 그렇지 않을 경우, 3중 for문을 사용해야 한다.
		WebElement first = courseList.findElement(By.tagName("ul"));
		
		// 2-1. 난이도 추출
        // Get the innerHTML of the element
        String innerHTML = courseList.getAttribute("innerHTML");
        List<String> difficultyList = new ArrayList<>();
        // Extract HTML comments from innerHTML
        String[] comments = innerHTML.split("<!--");
        for (String comment : comments) {
            if (comment.contains("-->")) {
                String extractedComment = comment.split("-->")[0];
                if (extractedComment.subSequence(0, 1).equals(" ")) {
                	int idx =extractedComment.indexOf("(");
                	String difficulty = extractedComment.substring(1, idx);
                	difficultyList.add(difficulty);
				}
                
            }
        }
        int idx=0;
		for (String key : SlopeList.keySet()) {
		    MoozuSlope mSlope =SlopeList.get(key);
		    mSlope.setDifficulty(difficultyList.get(idx++));
		}
        
        
		
		
		List<WebElement> list = first.findElements(By.tagName("li"));
		
		// 데이터를 담기위한 임시공간.
		List<String> dataBox = new ArrayList<>();
		
		
		// 2-2. 반복문을 통한 슬로프 오픈 여부 추출
		for(WebElement e:list) {
			
			
			
			//isEmpty()가 false -> 유효한 값. 그래서 추출 #참고," 의도와 다르게 li이 137개 이다" )<- 질문
			if(e.findElements(By.className("on")).isEmpty()==false) {
				
				// 1. 슬로프 이름 추출
				WebElement name = e.findElement(By.tagName("p"));
				String slopeName = name.getText();// 조회 목적
				
				// 2. 슬로프 오픈정보 추출
				List<WebElement> slopeOpenInfo = e.findElements(By.className("on"));
				
				for(WebElement e2: slopeOpenInfo) {
					String rawSlopeStatus = e2.getAttribute("innerHTML");
					String SlopeStatus = rawSlopeStatus.substring(4,rawSlopeStatus.length()-3);// last는 포함안함.
					dataBox.add(SlopeStatus);
					}
				
				// 0번째 인덱스 -> 슬로프 이름
				// 나머지는 슬로프 오픈 상태

				MoozuSlope mSlope = SlopeList.get(slopeName);
				// 각 상태를 초기화 한다.
				for(String s: dataBox) {
					switch (s) {
					case "새벽스키": {
						mSlope.set새벽(s);
						break;
					}
					case "오전스키": {
						mSlope.set오전(s);
						break;
					}
					case "오후스키": {
						mSlope.set오후(s);
						break;
						}
					case "야간스키": {
						mSlope.set야간(s);
						break;
					}
					case "자정스키": {
						mSlope.set자정(s);
						break;
					}
					default:
						throw new IllegalArgumentException("Unexpected value: " + s);
					}
				}
				dataBox.clear();
				
			}
		}

		for (String key : SlopeList.keySet()) {
		    MoozuSlope mSlope =SlopeList.get(key);
		    System.out.println(mSlope.getSlopeName());
		    System.out.println(mSlope.get새벽());
		    System.out.println(mSlope.get야간());
		    System.out.println(mSlope.get오전());
		    System.out.println(mSlope.get오후());
		    System.out.println(mSlope.get자정());
		    System.out.println(mSlope.getDifficulty());
		    System.out.println();
		}

		
		


		driver.quit();

		
	}


}
