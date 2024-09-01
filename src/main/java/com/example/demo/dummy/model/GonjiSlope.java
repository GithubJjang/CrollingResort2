package com.example.demo.dummy.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class GonjiSlope {
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String slopeName;
	private String difficulty;
	private String 주간;
	private String 야간;
	private String 백야;
	
	// 기본생성자 필수
	public GonjiSlope() {
		
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
	public String get백야() {
		return 백야;
	}
	public void set백야(String 백야) {
		this.백야 = 백야;
	}

	// 빌더패턴
	// id는 auto_increment라서 안해도 됨.

	public GonjiSlope buildSlopeName(String slopeName) {
		this.slopeName=slopeName;
		return this;
	}

	public GonjiSlope buildDifficulty(String difficulty) {
		this.difficulty=difficulty;
		return this;
	}

	public GonjiSlope build주간(String 주간) {
		this.주간=주간;
		return this;
	}

	public GonjiSlope build야간(String 야간) {
		this.야간=야간;
		return this;
	}

	public GonjiSlope build백야(String 백야) {
		this.백야=백야;
		return this;
	}
	

	
	
	

}
