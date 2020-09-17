package com.example.dto;

import java.awt.List;

public class DistrictVO {
	
	private int NY;
	private int NX;
	private double Y;
	private double X;
	private String district_ID;
	private String district_name_step1;
	private String district_name_step2;
	
	public DistrictVO(String district_ID, String district_name_step1,String district_name_step2,int NX, int NY, double X, double Y) {
		this.district_ID=district_ID;
		this.district_name_step1=district_name_step1;
		this.district_name_step2=district_name_step2;
		this.NX=NX;
		this.NY=NY;
		this.X=X;
		this.Y=Y;
		
	}
	
	public String getDistrict_name_step2() {
		return district_name_step2;
	}

	public void setDistrict_name_step2(String district_name_step2) {
		this.district_name_step2 = district_name_step2;
	}

	public void setDistrict_name_step1(String district_name_step1) {
		this.district_name_step1 = district_name_step1;
	}

	

	public DistrictVO() {
	}


	public int getNY() {
		return NY;
	}



	public void setNY(int nY) {
		NY = nY;
	}





	public int getNX() {
		return NX;
	}





	public void setNX(int nX) {
		NX = nX;
	}





	public double getY() {
		return Y;
	}





	public void setY(double y) {
		Y = y;
	}





	public double getX() {
		return X;
	}





	public void setX(double x) {
		X = x;
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


}
