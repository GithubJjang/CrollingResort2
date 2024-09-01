package com.example.demo.dummy.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


public class High1Slope {
	// 자동으로 넘버링을 한다.
	@Id 
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Column(name = "슬로프이름")
	private String slopeName;
	
	private String slopeTag;
	
	private String 주간;
	
	private String 야간;
	
	@Column(name = "난이도")
	private String difficulty;

	@Column(name = "연장(m)")
	private String 연장;
	
	@Column(name = "표고차(m)")
	private String 표고차;
	
	@Column(name = "평균폭(m)")
	private String 평균폭;
	
	private String 경사각_평균;
	
	private String 경사각_최대;
	
	private String 혼잡;
	

	
	// 성성자
	public High1Slope() {
		
	}
	
	//getter - setter
	public int getId() {
		return id;
	}



	
	public void setId(int id) {
		this.id = id;
	}




	public String getSlopeName() {
		return slopeName;
	}




	public void setSlopeName(String slopeName) {
		this.slopeName = slopeName;
	}




	public String getSlopeTag() {
		return slopeTag;
	}




	public void setSlopeTag(String slopeTag) {
		this.slopeTag = slopeTag;
	}




	public String get주간() {
		return 주간;
	}




	public void set주간(String 주간) {
		this.주간 = 주간;
	}




	public String get야간() {
		return 야간;
	}




	public void set야간(String 야간) {
		this.야간 = 야간;
	}




	public String getDifficulty() {
		return difficulty;
	}




	public void setDifficulty(String difficulty) {
		this.difficulty = difficulty;
	}




	public String get연장() {
		return 연장;
	}




	public void set연장(String 연장) {
		this.연장 = 연장;
	}




	public String get표고차() {
		return 표고차;
	}




	public void set표고차(String 표고차) {
		this.표고차 = 표고차;
	}




	public String get평균폭() {
		return 평균폭;
	}




	public void set평균폭(String 평균폭) {
		this.평균폭 = 평균폭;
	}




	public String get경사각_평균() {
		return 경사각_평균;
	}




	public void set경사각_평균(String 경사각_평균) {
		this.경사각_평균 = 경사각_평균;
	}




	public String get경사각_최대() {
		return 경사각_최대;
	}




	public void set경사각_최대(String 경사각_최대) {
		this.경사각_최대 = 경사각_최대;
	}




	public String get혼잡() {
		return 혼잡;
	}
	
	
	
	public void set혼잡(String 혼잡) {
		this.혼잡 = 혼잡;
	}

	// 빌더 패턴을 위한 메서드


	// Id는 Auto_increment이므로 build할 필요가 없다.

	public High1Slope buildSlopeName(String slopeName) {
		this.slopeName = slopeName;
		return this;
	}



	public High1Slope buildSlopeTag(String slopeTag) {
		this.slopeTag = slopeTag;
		return this;
	}



	public High1Slope build주간(String 주간) {
		this.주간 = 주간;
		return this;
	}



	public High1Slope build야간(String 야간) {
		this.야간 = 야간;
		return this;
	}



	public High1Slope buildDifficulty(String difficulty) {
		this.difficulty = difficulty;
		return this;
	}



	public High1Slope build연장(String 연장) {
		this.연장 = 연장;
		return this;
	}



	public High1Slope build표고차(String 표고차) {
		this.표고차 = 표고차;
		return this;
	}



	public High1Slope build평균폭(String 평균폭) {
		this.평균폭 = 평균폭;
		return this;
	}



	public High1Slope build경사각_평균(String 경사각_평균) {
		this.경사각_평균 = 경사각_평균;
		return this;
	}



	public High1Slope build경사각_최대(String 경사각_최대) {
		this.경사각_최대 = 경사각_최대;
		return this;
	}



	public High1Slope build혼잡(String 혼잡) {
		this.혼잡 = 혼잡;
		return this;
	}




	
	
}
