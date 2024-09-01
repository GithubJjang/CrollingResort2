package com.example.demo.dummy.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


public class AlpensiaSlope {
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String slopeName;
	private String difficulty;
	private String 주간;
	private String 야간;

	
	// 기본생성자 필수
	public AlpensiaSlope() {
		
	}
	
	// getter - setter
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
	public String getDifficulty() {
		return difficulty;
	}
	public void setDifficulty(String difficulty) {
		this.difficulty = difficulty;
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


	// 빌더패턴
	// id는 auto_increment라서 안해도 됨.

	public AlpensiaSlope buildSlopeName(String slopeName) {
		this.slopeName=slopeName;
		return this;
	}

	public AlpensiaSlope buildDifficulty(String difficulty) {
		this.difficulty=difficulty;
		return this;
	}

	public AlpensiaSlope build주간(String 주간) {
		this.주간=주간;
		return this;
	}

	public AlpensiaSlope build야간(String 야간) {
		this.야간=야간;
		return this;
	}

	

	
	
	

}
