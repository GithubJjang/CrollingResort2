package com.example.demo.dto;

import java.time.LocalDate;

import com.example.demo.model.Resort;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SlopeDTO {


	private int resortId;// resort의 PK를 가지고 있음.
	private String difficulty;
	private String slopeName;
	private String day;
	private String night;
	private String dawn;
	private String date;

}
