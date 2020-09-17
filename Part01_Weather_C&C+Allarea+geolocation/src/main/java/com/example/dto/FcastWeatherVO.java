package com.example.dto;


public class FcastWeatherVO {
	
	public String fcastDate; // 예보날짜
	public String fcastTime; // 예보시간
	
	public String district_ID;
	public String district_name_step1;
	public String district_name_step2;

	public String SKY;
	public String FTH;
	
	public String day1;
	public String day3;
	
	public String getDay1() {
		return day1;
	}


	public void setDay1(String day1) {
		this.day1 = day1;
	}


	public String getDay2() {
		return day3;
	}


	public void setDay2(String day2) {
		this.day3 = day2;
	}

	
	
	
	public int ny;
	public int nx;
	

	public FcastWeatherVO() {
		
	}

	public FcastWeatherVO(String areaID, String nowday, String day3) {
		super();
		this.district_ID = areaID;
		this.day1 = nowday;
		this.day3 = day3;
		
	}


	public FcastWeatherVO(String district_ID, String fcastTime, String fcastDate, String sKY, String fTH) {
		super();
		this.district_ID = district_ID;
		this.fcastTime = fcastTime;
		this.fcastDate = fcastDate;
		this.SKY = sKY;
		this.FTH = fTH;
	}


	public String getFcastDate() {
		return fcastDate;
	}

	public void setFcastDate(String fcastDate) {
		this.fcastDate = fcastDate;
	}

	public String getFcastTime() {
		return fcastTime;
	}

	public void setFcastTime(String fcastTime) {
		this.fcastTime = fcastTime;
	}

	public String getDistrict_ID() {
		return district_ID;
	}

	public void setDistrict_ID(String district_ID) {
		this.district_ID = district_ID;
	}

	public String getDistrict_name_step1() {
		return district_name_step1;
	}

	public void setDistrict_name_step1(String district_name_step1) {
		this.district_name_step1 = district_name_step1;
	}

	public String getSKY() {
		return SKY;
	}

	public void setSKY(String sKY) {
		SKY = sKY;
	}

	public String getFTH() {
		return FTH;
	}

	public void setFTH(String fTH) {
		FTH = fTH;
	}

	public int getNy() {
		return ny;
	}

	public void setNy(int ny) {
		this.ny = ny;
	}

	public int getNx() {
		return nx;
	}

	public void setNx(int nx) {
		this.nx = nx;
	}
	
	public String getDistrict_name_step2() {
		return district_name_step2;
	}


	public void setDistrict_name_step2(String district_name_step2) {
		this.district_name_step2 = district_name_step2;
	}


	public String getDay3() {
		return day3;
	}


	public void setDay3(String day3) {
		this.day3 = day3;
	}



	
}
