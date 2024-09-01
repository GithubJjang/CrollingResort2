package com.example.demo.crolling;

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
public class O2 implements CreateCheckTable  {

	private ResortService resortService;
	private SlopeService slopeService;
	private SlopeRepository slopeRepository;
	
	@Scheduled(fixedDelay = 30000)
	public void doCrolling() throws Exception {
		
		// 한국 오늘 날짜 추출
		LocalDate date = LocalDate.now(ZoneId.of("Asia/Seoul"));
		
		System.setProperty("webdriver.chrome.driver", "C:\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");
		
		// 실행할때마다 브라우저가 안 뜨도록 추가 설정
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--headless");  // Run Chrome in headless mode (without opening the browser window)
		
		// 기본설정
		WebDriver driver= new ChromeDriver(options);


		
		try {
			driver.get("https://www.o2resort.com/SKI/slopeOpen.jsp");
			//System.out.println(driver.getPageSource());


			// 내용추출
			WebElement tbodyElement = driver.findElement(By.tagName("tbody"));
			List<WebElement> trElement = tbodyElement.findElements(By.tagName("tr"));
	
			// 쓰레기 데이터 제거
			trElement.remove(0);// 0번쨰는 목차 -> 제거
			trElement.remove(trElement.size()-1); // 뒤에서 1~3은 각종 부대시설 -> 제거
			trElement.remove(trElement.size()-1);
			trElement.remove(trElement.size()-1);
			
			// 각종 변수들
			String slopeName="";//0번
			String difficulty="";//1번
			//String day="";//2번
			//String night="";//3번
			//String dawn="";//4번
			//String date="";//5번
			
			//int size=7;// 난이도포함, 6은 난이도 없음
			
			Resort resort=null;
			String resortName = "O2";

			MultiKeyMap<String, Boolean> slopeCheckList = createCheckList(resortName);
			
			// 리조트 등록
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
			
			
			List<String> databox = new ArrayList<String>();
			List<Slope> slopeList= new ArrayList<Slope>();// 슬로프 저장공간
			List<WebElement> tdElement = null;
			
			// 본 코드
			// 2. 난이도와 슬로프명 추출 -> 오픈현황과 분리되어 있으므로.
			for(WebElement e: trElement) {
				tdElement = e.findElements(By.tagName("td"));
				// dataBox에 옮겨담기
				for(WebElement w: tdElement) {
					databox.add(w.getText());
				}
				// 난이도 + 슬로프명
				if(tdElement.size()==7) {
					// 0: 난이도
					// 1: 순번 -> 필요x
					// 2: 슬로프명
					difficulty=databox.get(0);//1번
					slopeName=databox.get(2);//0번
				}
				else if(tdElement.size()==6){
					// 난이도(x) + 슬로프명
					// 0: 순번
					// 1: 슬로프명
					slopeName = databox.get(1);
				}
				else {
					// 만약 데이터의 길이가 6 or 7이 아닌 경우 수정을 해야한다
					throw new Exception("O2 슬로프 정보 확인 및 수정하세요!!!");
					
				}
				//System.out.println(slopeName+difficulty);
				slopeList.add(
						Slope.builder()
								.resort(resort)
								.slopeName(slopeName)
								.difficulty(difficulty)
								.build()
				);
				databox.clear();
			}
			
			Convert convert = (String input)->{
				if(input.equals("https://www.o2resort.com/common/images/ski/icon_slope_open.jpg")) {
					return "OPEN";
				}
				else {
					return "CLOSE";
				}
			};
			
			// 3. 슬로프 오픈 여부 추출		
			List<WebElement> slopeLV1Rows = driver.findElements(By.cssSelector("div.skiRightBox table.tblBasic tr.slopeLV1"));
			for(WebElement e: slopeLV1Rows) {
				
				List<WebElement> tds = e.findElements(By.cssSelector("td:has(img)"));
				for (WebElement imgTd : tds) {
				    WebElement imgTag = imgTd.findElement(By.tagName("img"));
				    String srcValue = imgTag.getAttribute("src");
				    databox.add(srcValue);// 주간 야간 심야 순으로
				    
				}
	
				
				
				if(slopeList.isEmpty()) break;// <- 종료조건
				
				// slopeList 꺼내온 후, 주간/야간/심야 갱신한 후 save한다.
				Slope slope = slopeList.remove(0);
				slope.setResortName(resortName);
				slope.setDay(convert.result(databox.get(0)));
				slope.setNight(convert.result(databox.get(1)));
				slope.setDawn(convert.result(databox.get(2)));
				slope.setDate(date);
				databox.clear();
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
