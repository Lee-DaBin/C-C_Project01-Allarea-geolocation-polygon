package com.example.service;

import java.util.List;

import org.json.simple.JSONObject;

import com.example.dto.DistrictVO;



public interface areaService {

	String select_centroid(String area_code_step01, List<DistrictVO> areaList);

	List<DistrictVO> select_step01_area(String area_code_step01, List<DistrictVO> areaList);
}