package com.example.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;

import com.example.dto.DistrictVO;


public class MainpageSErviceImpl implements MainpageService {
	
	private static final Logger logger = LoggerFactory.getLogger(MainpageSErviceImpl.class);

	@Override
	public void execute(Model model,String now) {
		
		// 첫화면 구성
		String now_day = now.substring(0, 8);
		String now_time = now.substring(8);

		logger.info("현재 날짜 = " + now_day);
		logger.info("현재 시간 = " + now_time);

		model.addAttribute("y", now_day.substring(0, 4));
		model.addAttribute("M", now_day.substring(4, 6));
		model.addAttribute("d", now_day.substring(6, 8));
		// 시간
		model.addAttribute("nowTime", now_time.substring(0, 4));

		// 오늘, 내일, 모레
		Date date = null;
		String fcday = null;
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		
		try {
			date = dateFormat.parse(now_day.substring(0, 8));
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		cal.add(Calendar.DATE, +1);
		fcday = dateFormat.format(cal.getTime());
		model.addAttribute("d2", fcday.substring(6, 8));

		cal.add(Calendar.DATE, +1);
		fcday = dateFormat.format(cal.getTime());
		model.addAttribute("d3", fcday.substring(6, 8));

		//// 요일 구하기
		cal = Calendar.getInstance();
		// 1 2 3 4 5 6 7
		final String[] week = { "일", "월", "화", "수", "목", "금", "토" };

		String W = week[cal.get(Calendar.DAY_OF_WEEK) - 1];
		model.addAttribute("w", W);

	}

}
