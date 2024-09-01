package com.example.demo.crolling;

import java.time.LocalDate;
import java.time.ZoneId;
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
import com.example.demo.model.Resort;
import com.example.demo.model.Slope;
import com.example.demo.service.server.ResortService;
import com.example.demo.service.server.SlopeService;

@AllArgsConstructor
@Component
public class PhyungChang implements CreateCheckTable{

	private ResortService resortService;
	private SlopeService slopeService;
	private SlopeRepository slopeRepository;
	
	@Scheduled(fixedDelay = 30000)
	public void doCrolling() {

		// 한국 오늘 날짜 추출
		LocalDate date = LocalDate.now(ZoneId.of("Asia/Seoul"));
	
		System.setProperty("webdriver.chrome.driver", "C:\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");
		
		// 실행할때마다 브라우저가 안 뜨도록 추가 설정
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--headless");  // Run Chrome in headless mode (without opening the browser window)
		options.addArguments("--remote-allow-orgins=*");
		// 기본설정
		WebDriver driver= new ChromeDriver(options);
		
		try {
			driver.get("https://phoenixhnr.co.kr/static/pyeongchang/snowpark/slope-lift");

			Resort resort=null;
			String resortName = "Pyeongchang";

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
			
			// 7 -> 8 맨 앞에 난이도 추가
			// 6 -> 7 맨 뒤에 NULL 추가
			// 5 -> 6 맨 뒤에 두 곳에 NULL 추가
			
			// 내용추출
			WebElement tbodyElement = driver.findElement(By.tagName("tbody"));
			List<WebElement> lst = tbodyElement.findElements(By.tagName("tr"));
			for(WebElement e : lst) {
				// tr에서 td,th분리하기
				
				//1. th 내용
				List<WebElement> thElement = e.findElements(By.tagName("th"));
				if(thElement.size()==2) {
					// 이 th배열은 난이도 + 슬로프이름 으로 구성
					difficulty=thElement.get(0).getText();
					slopeName=thElement.get(1).getText();
				}
				else {
					// 슬로프 이름으로만 구성
					slopeName=thElement.get(0).getText();
				}
				
				
				//2. td 내용(연장,표고차,경사도,주간,야간) <- 고정
				List<WebElement> tdElement = e.findElements(By.tagName("td"));
				//연장=tdElement.get(0).getText();
				//표고차=tdElement.get(1).getText();
				//경사도=tdElement.get(2).getText();
				day=tdElement.get(3).getText();
				night=tdElement.get(4).getText();

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
			}
			slopeService.clearUncheckedSlope(slopeCheckList,resort);
		}
			catch (Exception e) {
				// 혹시나 모르는 driver 오류를 잡기위해서 설정추가.
				e.printStackTrace();
		}finally {
				// quit()까지는 문제없이 잘 실행이 된다.
				driver.quit();
		}
	// 위 과정 이후 발생하는 "Connection Reset" 오류는 무시해도 됨.
	// 어차피, crolling 데이터 수집완료해서 상관없음.

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