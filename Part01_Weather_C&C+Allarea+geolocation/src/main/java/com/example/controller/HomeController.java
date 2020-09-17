package com.example.controller;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.Util.DateLoader;
import com.example.dto.DistrictVO;
import com.example.dto.FcastWeatherVO;
import com.example.dto.NowWeatherVO;
import com.example.service.MainpageSErviceImpl;
import com.example.service.MainpageService;
import com.example.service.areaService;
import com.example.service.areaServiceImpl;
import com.example.service.weatherService;

@Controller
public class HomeController {

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	@Inject
	private weatherService service;
	private MainpageService mainService;
	private areaService areaService;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) throws Exception {

		logger.info("home");
		logger.info("Welcome home! The client locale is {}.", locale);

		String now = new DateLoader().DateLader(); // 현재 날짜 시간
		logger.info("현재시간" + now);

		// 메인화면 구성
		logger.info("=== 1) 메인화면 구성");
		mainService = new MainpageSErviceImpl();
		mainService.execute(model, now);

		return "home";
	}

	// 현재 지역 현재날씨 ajax
	@RequestMapping(value = "/nowWeather", method = RequestMethod.POST, produces = "application/text; charset=utf8") // 한글인코딩해서넘기기
	public @ResponseBody String ajax_nowWeather(@RequestBody String ID, Model model) throws Exception {

		List<DistrictVO> areaList = service.selectarea(); // 지역테이블 준비
		String now = new DateLoader().DateLader();
		String areaID = ID.replaceAll("[^0-9]", ""); // 숫자만가져오기

		areaID = areaID.substring(0, 4); // 시도로 사용할꺼니까 앞에 4자리만 필요 , 광역시면 2자리
		String district_ID = areaID + "000000";
		logger.debug("=== 1) 현재 받아온 지역 ID = " + district_ID);

		// 현재날씨 요청 및 출력
		logger.info("=== 2) 현재날씨 시작");
		service.insertnw(now, district_ID, areaList);
		List<NowWeatherVO> nwList = service.selectnw(now, district_ID);
		String nowWeather = service.NWjsonObject_view(nwList, areaList, now);

		return nowWeather;

	}

	// 현재 지역 예보날씨 ajax
	@RequestMapping(value = "/now_area_fcWeatherList", method = RequestMethod.POST, produces = "application/text; charset=utf8") // 한글인코딩해서넘기기
	public @ResponseBody String now_area_fcWeatherList(@RequestBody String ID, Model model) throws Exception {

		List<DistrictVO> areaList = service.selectarea(); // 지역테이블 준비

		String now = new DateLoader().DateLader();
		String nowday = now.substring(0, 8);
		String areaID = ID.replaceAll("[^0-9]", ""); // 숫자만가져오기

		areaID = areaID.substring(0, 4); // 시도로 사용할꺼니까 앞에 4자리만 필요 , 광역시면 2자리
		String district_ID = areaID + "000000";
		logger.debug("=== 1) nowarea_fcWeatherList 현재 받아온 지역 ID = " + district_ID);

		// 선택한 지역이름,예보날씨 보내기
		List<FcastWeatherVO> fcDtos = service.selectfw(nowday, areaList, district_ID); // 지역테이블 준비
		String Sfw = service.FWjsonObject_view(fcDtos, areaList, now);
		logger.debug("nowarea_fcWeatherList 자바스크립트 보낼꺼 확인" + Sfw);
		return Sfw;
	}

	// 선택한 지역 중심
	@RequestMapping(value = "/area_centroid", method = RequestMethod.POST, produces = "application/text; charset=utf8") // 한글인코딩해서넘기기
	public @ResponseBody String area_centroid(@RequestBody String area_code) throws Exception {

		List<DistrictVO> areaList = service.selectarea(); // 지역테이블 준비
		String area_code_step01 = area_code.replaceAll("[^0-9]", "") + "00000000";
		logger.debug("=== 1) 현재 받아온 지역코드 = " + area_code_step01);

		// 선택지역 X,Y 찾기
		areaService = new areaServiceImpl();
		String area_centroid = areaService.select_centroid(area_code_step01, areaList);

		return area_centroid;
	}

	// 선택한 지역 현재날씨
	@RequestMapping(value = "/select_area_NowWeather", method = RequestMethod.POST, produces = "application/text; charset=utf8") // 한글인코딩해서넘기기
	public @ResponseBody String select_area_NowWeather(@RequestBody String area_code) throws Exception {

		List<DistrictVO> areaList = service.selectarea(); // 지역테이블 준비
		String area_code_step01 = area_code.replaceAll("[^0-9]", "");
		logger.debug("=== 1) 현재 받아온 지역광역시도코드 = " + area_code_step01);

		// 선택한 지역별 광역시도 지역리스트
		List<DistrictVO> select_step01_area = areaService.select_step01_area(area_code_step01, areaList); // 지역테이블 준비

		// 광역시도별 지역리스트 현재날씨 요청
		String now = new DateLoader().DateLader();
		service.insertnw_select_step01_nw(now, select_step01_area);

		List<NowWeatherVO> select_area_nwList = service.select_step01_nw(now, area_code_step01);

		String area_nwList = service.NWareajsonObject_view(select_area_nwList, select_step01_area, now);
		logger.debug("광역시도별 자바스크립트 보낼꺼 확인" + area_nwList);

		return area_nwList;
	}

	// 선택한 지역 예보날씨 default
	@RequestMapping(value = "/select_area_fcWeatherList", method = RequestMethod.POST, produces = "application/text; charset=utf8") // 한글인코딩해서넘기기
	public @ResponseBody String select_area_fcWeatherList(@RequestBody String ID, Model model) throws Exception {

		List<DistrictVO> areaList = service.selectarea(); // 지역테이블 준비

		String now = new DateLoader().DateLader();
		String nowday = now.substring(0, 8);
		String areaID = ID.replaceAll("[^0-9]", ""); // 숫자만가져오기
		String district_ID = areaID + "00000000";
		logger.debug("=== 1) nowarea_fcWeatherList 현재 받아온 지역 ID = " + district_ID);

		// 선택한 지역이름,예보날씨 보내기
		List<FcastWeatherVO> fcDtos = service.selectfw(nowday, areaList, district_ID); // 지역테이블 준비
		String Sfw = service.FWjsonObject_view(fcDtos, areaList, now);
		logger.debug("nowarea_fcWeatherList 자바스크립트 보낼꺼 확인" + Sfw);
		return Sfw;
	}

	// 선택한 지역 예보날씨
	@RequestMapping(value = "/select_area_fcWeatherList_step02", method = RequestMethod.POST, produces = "application/text; charset=utf8") // 한글인코딩해서넘기기
	public @ResponseBody String select_area_fcWeatherList_step02(@RequestBody String ID, Model model) throws Exception {

		List<DistrictVO> areaList = service.selectarea(); // 지역테이블 준비

		String now = new DateLoader().DateLader();
		String nowday = now.substring(0, 8);
		String areaID = ID.replaceAll("[^0-9]", ""); // 숫자만가져오기
		logger.debug("=== 1) nowarea_fcWeatherList 현재 받아온 지역 ID = " + areaID);

		// 선택한 지역이름,예보날씨 보내기
		List<FcastWeatherVO> fcDtos = service.selectfw(nowday, areaList, areaID); // 지역테이블 준비
		String Sfw = service.FWjsonObject_view(fcDtos, areaList, now);
		logger.debug("nowarea_fcWeatherList 자바스크립트 보낼꺼 확인" + Sfw);
		return Sfw;
	}

}
