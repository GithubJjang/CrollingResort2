package com.example.demo.crolling;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

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
public class Alpensia implements CreateCheckTable {

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

		// Slope, 수영장 등... 여러 곳에서 데이터 갱신이 필요한 곳에 유용하게 사용될 듯.

		
		try {
			driver.get("https://www.alpensia.com/ski/slope-now.do");
			
			
			// 첫 시작들 할때, 당사 리조트 등록 및 PK를 Slope에게 넘겨준다.
			// 그 이후 여러번 실행시에는 PK만 넘겨준다.
			// update시 -> resort의 PK 필요, slopeName이 중복가능해서
			// insert시 -> 필요 x
			// delete시 -> reosrt의 PK 필요, slopeName이 중복가능해서
			Resort resort;
			final String resortName = "Alpensia";

			MultiKeyMap<String, Boolean> slopeCheckList = createCheckList(resortName);
	
			// Resort 등록
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
			
			ArrayList<String> dataBox = new ArrayList<String>();
			
			
			// 내용추출
			WebElement tbodyElement = driver.findElement(By.tagName("tbody"));
			List<WebElement> lst = tbodyElement.findElements(By.tagName("tr"));
			for(WebElement e1: lst) {
				List<WebElement> tdElement = e1.findElements(By.tagName("td"));
				// dataBox에 권종(슬로프이름), 난이도 주간, 야간 순으로 데이터가 담긴다.
				for(WebElement e2: tdElement) {
					dataBox.add(e2.getText());
				}
				slopeName=dataBox.get(0);
				difficulty=dataBox.get(1);
				day=dataBox.get(2);
				night=dataBox.get(3);
				dawn="";

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
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			driver.quit();
		}
		
	}

	// 테이블 생성
	public MultiKeyMap<String, Boolean>  createCheckList(String resort) {
		List<Slope>  slopesFromDB = slopeRepository.findByResortName(resort);//DB의 슬로프 이름을 가져온다.
		MultiKeyMap<String, Boolean> slopeCheckList =  new MultiKeyMap<>();

		for(Slope s: slopesFromDB) {
			slopeCheckList.put(s.getSlopeName(),s.getResortName(), false);
		}

		return slopeCheckList;
	}
	
}



