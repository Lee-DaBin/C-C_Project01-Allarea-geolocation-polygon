package com.example.scheduler;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import com.example.Util.DateLoader;
import com.example.dao.FcastWeatherJSON;
import com.example.dto.DistrictVO;
import com.example.dto.FcastWeatherVO;
import com.example.service.weatherService;

public class SchedulerFcW {

	private static final Logger logger = LoggerFactory.getLogger(SchedulerFcW.class);
	
	@Inject
	private weatherService service;

	@Scheduled(fixedRate = 10800000) //5분마다 실행(300000),1시간 간격=(3600000) -> [10000*60*60], 3시간 간격=(10800000)
	public void scheduleRun() throws Exception {
		System.out.println("★★★★★★★★★★★★★★★★★  예보날씨 요청  Schedulerfc 실행 ★★★★★★★★★★★★★★★★★");
		
		logger.info("scheduler시작 = " );
		String now = new DateLoader().DateLader();
		String now_day = now.substring(0, 8);
		String now_time = now.substring(8);
		logger.debug("현재 날짜 = " + now_day+"현재 시간 = " + now_time);
		//List<DistrictVO> areaList = service.selectarea(); 
		//service.insertfw(now, areaList);
		logger.info("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★ ★★★★★★★★★★★★★★★★★★★");

	}
}
