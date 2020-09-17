package com.example.dto;

public class NowWeatherVO {
	public String nowDate; // 현재날짜
	public String nowTime; // 현재시간
	public String SKY;
	public String NTH;
	
	public String district_ID;
	
	public String district_name_step1;
	public String district_name_step2;
	
	public int areanum;
	
	public double X;
	public double Y;
	
	public NowWeatherVO() {
		
	}
	
	public NowWeatherVO(String nowDate2, String nowTime2) {
		this.nowDate = nowDate2;
		this.nowTime = nowTime2;
	}
	
	public NowWeatherVO(String district_ID, String nowTime, String nowDate, String SKY,String NTH) {
		this.district_ID = district_ID;
		this.nowDate = nowDate;
		this.nowTime = nowTime;
		this.SKY = SKY;
		this.NTH = NTH;
	}
	
	public NowWeatherVO(String nowDate2, String nowTime2, String areaID) {
		this.district_ID = areaID;
		this.nowDate = nowDate2;
		this.nowTime = nowTime2;
	}

	public String getNowDate() {
		return nowDate;
	}
	public void setNowDate(String nowDate) {
		this.nowDate = nowDate;
	}
	public String getNowTime() {
		return nowTime;
	}
	public void setNowTime(String nowTime) {
		this.nowTime = nowTime;
	}
	public String getSKY() {
		return SKY;
	}
	public void setSKY(String sKY) {
		SKY = sKY;
	}
	public String getNTH() {
		return NTH;
	}
	public void setNTH(String nTH) {
		NTH = nTH;
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
	public int getAreanum() {
		return areanum;
	}
	public void setAreanum(int areanum) {
		this.areanum = areanum;
	}
	public double getX() {
		return X;
	}
	public void setX(double x) {
		X = x;
	}
	public double getY() {
		return Y;
	}
	public void setY(double y) {
		Y = y;
	}
	public String getDistrict_name_step2() {
		return district_name_step2;
	}

	public void setDistrict_name_step2(String district_name_step2) {
		this.district_name_step2 = district_name_step2;
	}
	
}
