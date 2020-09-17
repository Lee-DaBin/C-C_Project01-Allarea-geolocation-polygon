package com.example.service;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.dto.DistrictVO;

@Service
public class areaServiceImpl implements areaService {
	
	private static final Logger logger = LoggerFactory.getLogger(areaServiceImpl.class);

	@Override
	public String select_centroid(String area_code_step01, List<DistrictVO> areaList) {
		JSONObject select_centroid = new JSONObject(); 
		
		for (int a = 0; a<areaList.size(); a++) {
			if(area_code_step01.contentEquals(areaList.get(a).getDistrict_ID())) {
				logger.debug("=== 2)현재 받아온 지역명  step1= "+areaList.get(a).getDistrict_name_step1());
				logger.debug("=== 2)현재 받아온 지역 X= "+areaList.get(a).getX()+"/ 현재 받아온 지역 Y= "+areaList.get(a).getY());
				select_centroid.put("District_ID", areaList.get(a).getDistrict_name_step1());
				select_centroid.put("X", areaList.get(a).getX());
				select_centroid.put("Y", areaList.get(a).getY());
			}
		}
		
		String centroid = select_centroid.toJSONString();
		logger.info("확인" + centroid);
		
		return centroid;
	}

	@Override
	public List<DistrictVO> select_step01_area(String area_code_step01, List<DistrictVO> areaList) {
		
		List<DistrictVO> select_step01_areaArray = new ArrayList<DistrictVO>(); // 지역이름 json정보 array
		
		for (int a = 0; a<areaList.size(); a++) {
			if(area_code_step01.equals(areaList.get(a).getDistrict_ID().substring(0,2))) {
				
				logger.debug("=== 1 둘이 같은지 확인 "+area_code_step01+"=="+areaList.get(a).getDistrict_ID().substring(0,2));
				logger.debug("=== 2-1) 현재 받아온 지역명  step1= "+areaList.get(a).getDistrict_name_step1());
				logger.debug("=== 2-2) 현재 받아온 지역명  step2= "+areaList.get(a).getDistrict_name_step2());
				logger.debug("=== 2-3) 현재 받아온 지역 NX= "+areaList.get(a).getNX());
				logger.debug("=== 2-4) 현재 받아온 지역 NY= "+areaList.get(a).getNY());
				logger.debug("=== 2-5) 현재 받아온 지역 X= "+areaList.get(a).getX());
				logger.debug("=== 2-6) 현재 받아온 지역 Y= "+areaList.get(a).getY());
				
				String district_ID = areaList.get(a).getDistrict_ID();
				String district_name_step1 = areaList.get(a).getDistrict_name_step1();
				String district_name_step2 = areaList.get(a).getDistrict_name_step2();
				double X = areaList.get(a).getX();
				double Y = areaList.get(a).getY();
				int NX = areaList.get(a).getNX();
				int NY = areaList.get(a).getNY();
				
				DistrictVO select_dto =  new DistrictVO(district_ID,district_name_step1,district_name_step2,NX,NY,X,Y);
				
				select_step01_areaArray.add(select_dto);
			
			}
		}
		
		return select_step01_areaArray;
	}

}
