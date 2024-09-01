package com.example.demo.dummy.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


public class YongPyongSlope {
	
	// 자동으로 넘버링을 한다.
	@Id 
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Column(name = "슬로프이름")
	private String slopeName;
	
	private String 주간;
	
	private String 야간;
	
	private String 심야;
	
	private String 비고;
	
	@Column(name = "난이도")
	private String difficulty;
	
	// 기본생성자는 항상 있어야 됨
	public YongPyongSlope() {
		
	}
	
	public YongPyongSlope(String slopeName, String 주간, String 야간,
			String 심야, String 비고, String difficulty) {
			this.slopeName=slopeName;
			this.주간=주간;
			this.야간=야간;
			this.심야=심야;
			this.비고=비고;
			this.difficulty=difficulty;
	}
	
	
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
	public String get심야() {
		return 심야;
	}
	public void set심야(String 심야) {
		this.심야 = 심야;
	}
	public String get비고() {
		return 비고;
	}
	public void set비고(String 비고) {
		this.비고 = 비고;
	}

	public String getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(String difficulty) {
		this.difficulty = difficulty;
	}

	//
	
}
