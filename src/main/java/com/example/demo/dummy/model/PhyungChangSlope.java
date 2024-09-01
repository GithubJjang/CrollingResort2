package com.example.demo.dummy.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


public class PhyungChangSlope {
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String difficulty;
	private String slopeName;
	private String 연장;
	private String 표고차;
	private String 경사도;
	private String 주간;
	private String 야간;
	
	
	// 생성자
	public PhyungChangSlope() {
		
	}
	
	//
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(String difficulty) {
		this.difficulty = difficulty;
	}

	public String getSlopeName() {
		return slopeName;
	}
	public void setSlopeName(String slopeName) {
		this.slopeName = slopeName;
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
	public String get경사도() {
		return 경사도;
	}
	public void set경사도(String 경사도) {
		this.경사도 = 경사도;
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
	
	// 빌더 패턴
	// Id는 auto_increment이므로 안해도 됨.
	
	public PhyungChangSlope BuildDifficulty(String difficulty) {
		this.difficulty = difficulty;
		return this;
	}

	public PhyungChangSlope BuildslopeName(String slopeName) {
		this.slopeName = slopeName;
		return this;
	}
	public PhyungChangSlope Build연장(String 연장) {
		this.연장 = 연장;
		return this;
	}
	public PhyungChangSlope Build표고차(String 표고차) {
		this.표고차 = 표고차;
		return this;
	}
	public PhyungChangSlope Build경사도(String 경사도) {
		this.경사도 = 경사도;
		return this;
	}
	public PhyungChangSlope Build주간(String 주간) {
		this.주간 = 주간;
		return this;
	}
	public PhyungChangSlope Build야간(String 야간) {
		this.야간 = 야간;
		return this;
	}
}
