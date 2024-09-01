package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class MoozuSlope {
	
		// 수정보류
		@Id @GeneratedValue(strategy = GenerationType.AUTO)
		private int id;
		
		@ManyToOne
		private Resort resort;
		
		private String difficulty="";
		private String slopeName="";
		private String 새벽="";
		private String 오전="";
		private String 오후="";
		private String 야간="";
		private String 자정="";
		// 수정
		private String date;
		
		public MoozuSlope() {
			
		}

		//getter - setter
		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public Resort getResort() {
			return resort;
		}

		public void setResort(Resort resort) {
			this.resort = resort;
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

		public String get새벽() {
			return 새벽;
		}

		public void set새벽(String 새벽) {
			this.새벽 = 새벽;
		}

		public String get오전() {
			return 오전;
		}

		public void set오전(String 오전) {
			this.오전 = 오전;
		}

		public String get오후() {
			return 오후;
		}

		public void set오후(String 오후) {
			this.오후 = 오후;
		}

		public String get야간() {
			return 야간;
		}

		public void set야간(String 야간) {
			this.야간 = 야간;
		}

		public String get자정() {
			return 자정;
		}

		public void set자정(String 자정) {
			this.자정 = 자정;
		}

		public String getDate() {
			return date;
		}

		public void setDate(String date) {
			this.date = date;
		}
		
		
		
	


}
