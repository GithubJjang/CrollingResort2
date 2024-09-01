package com.example.demo.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Slope {
	
	// 수정보류
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private long id; // querydsl 적용시 long type임 + @Entity 생성시 @Id 식별자 반드시 필요함.
	
	@ManyToOne
	private Resort resort;
	private String resortName;
	private String difficulty;
	private String slopeName;
	private String day;
	private String night;
	private String dawn;
	// 수정
	private LocalDate date;
	

	// 빌더패턴
	// id는 auto_increment라서 안해도 됨.
	
	public Slope buildResort(Resort resort) {
		this.resort=resort;
		return this;
	}

	public Slope buildSlopeName(String slopeName) {
		this.slopeName=slopeName;
		return this;
	}

	public Slope buildDifficulty(String difficulty) {
		this.difficulty=difficulty;
		return this;
	}


}
