package com.example.service;

import java.io.IOException;
import java.util.List;

import org.json.simple.parser.ParseException;
import org.springframework.ui.Model;

import com.example.dto.DistrictVO;
import com.example.dto.FcastWeatherVO;
import com.example.dto.NowWeatherVO;

public interface weatherService {

	public List<DistrictVO> selectarea() throws Exception;

	public List<FcastWeatherVO> selectfw(String nowday, List<DistrictVO> areaList, String areaID) throws Exception;

	public List<NowWeatherVO> selectnw(String now,String areaID);

	public void insertnw(String nwnow, String areaID,List<DistrictVO> areaList) throws IOException, ParseException;

	public void insertfw(String now, List<DistrictVO> areaList) throws IOException, ParseException;

	public String FWjsonObject_view(List<FcastWeatherVO> fcDtos, List<DistrictVO> areaList, String now);
	
	public String NWjsonObject_view(List<NowWeatherVO> nwList, List<DistrictVO> areaList, String now);

	public void insertnw_select_step01_nw(String now, List<DistrictVO> select_step01_area)throws IOException, ParseException;

	public List<NowWeatherVO> select_step01_nw(String now, String area_code_step01);

	public String NWareajsonObject_view(List<NowWeatherVO> select_area_nwList, List<DistrictVO> select_step01_area,String now);


}
