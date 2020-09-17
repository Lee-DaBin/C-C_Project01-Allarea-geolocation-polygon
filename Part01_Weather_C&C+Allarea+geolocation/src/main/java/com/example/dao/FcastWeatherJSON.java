package com.example.dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.dto.FcastWeatherVO;


public class FcastWeatherJSON {
	
	private static final Logger logger = LoggerFactory.getLogger(FcastWeatherJSON.class);

	final static String servicekey = "Mrwu0SqEmSX9PWA2wgTPid3kzKQlcBOuJV76ees1NkwS7aR3xsYx47eZt4bmM7gYn6QoN4J55hBElGdputuRpw%3D%3D";
	String apiUrl = "http://apis.data.go.kr/1360000/VilageFcstInfoService/getVilageFcst";
	String numOfRows = "270";// 3일 =270

	String baseDate;
	String baseTime;

	// 기준 날짜, 시간, 위도, 경도를 입력하면 해당하는 지역의 동내기상정보를 JSON데이터로 가져와 NowWeater객체를 만들어 반환합니다.
	public List<FcastWeatherVO> getFcastWeather(String areaname,String districtID,int now_day, int now_time, int x, int y) throws IOException, ParseException {
		logger.info("===================" +areaname+"[동네예보 API] ===================");

		// 결과 데이터를 저장할 기상객체를 만듭니다.
		FcastWeatherVO fcDto = new FcastWeatherVO();
		
		fcDto.district_name_step1 = areaname;
		fcDto.district_ID = districtID;
		
		// 현재시간, 현재날짜 API기준 시간 날짜로 변환해주기
		// 동네예보 API Base_time -> (0000,0200,0500,0800,1100,1400,1700,2000,2300) 제공시간은
		// 3시간 간격 10분
		logger.debug("현재날짜 =" + now_day+"현재시간 =" + now_time);
		String[] baseTime_list = { "2310", "0210", "0510", "0810", "1110", "1410", "1710", "2010" };

		baseDate = Integer.toString(now_day);
		now_time=now_time/100;

		logger.debug("처음에 보내줄때 현재 시간 값 시:분 ===" + now_time);
		if (now_time / 100 < 2 && now_time % 100 < 10) {

			DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
			Date date = null;
			
			try {
					date = dateFormat.parse(baseDate);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.DATE,-1);
			
			baseDate =  dateFormat.format(cal.getTime());
			baseTime = "2310";

		} else {

			if (now_time / 100 < 10) {
				if (23 < now_time / 100 && now_time / 100 <= 2) {
					baseTime = baseTime_list[7];
				} else if (2 < now_time / 100 && now_time / 100 <= 5) {
					baseTime = baseTime_list[0];
				} else if (5 < now_time / 100 && now_time / 100 <= 8) {
					baseTime = baseTime_list[1];
				} else if (8 < now_time / 100 && now_time / 100 <= 11) {
					baseTime = baseTime_list[2];
				} else if (11 < now_time / 100 && now_time / 100 <= 14) {
					baseTime = baseTime_list[3];
				} else if (14 < now_time / 100 && now_time / 100 <= 17) {
					baseTime = baseTime_list[4];
				} else if (17 < now_time / 100 && now_time / 100 <= 20) {
					baseTime = baseTime_list[5];
				} else if (20 < now_time / 100 && now_time / 100 <= 23) {
					baseTime = baseTime_list[6];
				}
			} else {

				if (23 <= now_time / 100 && now_time / 100 <= 2) {
					baseTime = baseTime_list[0];
				} else if (2 <= now_time / 100 && now_time / 100 < 5) {
					baseTime = baseTime_list[1];
				} else if (5 <= now_time / 100 && now_time / 100 < 8) {
					baseTime = baseTime_list[2];
				} else if (8 <= now_time / 100 && now_time / 100 < 11) {
					baseTime = baseTime_list[3];
				} else if (11 <= now_time / 100 && now_time / 100 < 14) {
					baseTime = baseTime_list[4];
				} else if (14 <= now_time / 100 && now_time / 100 < 17) {
					baseTime = baseTime_list[5];
				} else if (17 <= now_time / 100 && now_time / 100 < 20) {
					baseTime = baseTime_list[6];
				} else if (20 <= now_time / 100 && now_time / 100 < 23) {
					baseTime = baseTime_list[7];
				}
			}

		}
		
		//logger.info("=================== [동네예보 API 현재시간 날짜 API: baseTime, baseDate확인] ===================");
		//logger.debug("현재 날짜 : " + now_day);
		//logger.debug("API 조회 날짜 : " + baseDate);
		//logger.debug("현재 시간 : " + now_time);
		//logger.debug("API 조회 시간 : " + baseTime);
		//logger.info("=========================================================================================");
		
		// JSON데이터를 요청하는 URL
		StringBuilder urlBuilder = new StringBuilder(apiUrl);
		String nx = Integer.toString(x);
		String ny = Integer.toString(y);

		// 격자값 넣어주기
		fcDto.nx = Integer.parseInt(nx);
		fcDto.ny = Integer.parseInt(ny);

		JSONArray SkyList = new JSONArray();
		JSONArray T1HList = new JSONArray();
		
		List<FcastWeatherVO> fwlist = new ArrayList<FcastWeatherVO>();
		
		try {
			urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "=" + servicekey);
			urlBuilder.append("&" + URLEncoder.encode("nx", "UTF-8") + "=" + URLEncoder.encode(nx, "UTF-8")); // 경도
			urlBuilder.append("&" + URLEncoder.encode("ny", "UTF-8") + "=" + URLEncoder.encode(ny, "UTF-8")); // 위도
			urlBuilder
					.append("&" + URLEncoder.encode("base_date", "UTF-8") + "=" + URLEncoder.encode(baseDate, "UTF-8"));
			urlBuilder
					.append("&" + URLEncoder.encode("base_time", "UTF-8") + "=" + URLEncoder.encode(baseTime, "UTF-8"));
			urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8"));
			urlBuilder.append(
					"&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode(numOfRows, "UTF-8"));

			// GET방식으로 전송해서 파라미터 받아오기
			URL url = new URL(urlBuilder.toString());

			System.out.println(url); // 넘어가는지 확인

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-type", "application/json");

			logger.debug("Response code: " + conn.getResponseCode());
			BufferedReader rd;

			if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
				rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			} else {
				rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
			}

			StringBuilder sb = new StringBuilder();
			String line;

			while ((line = rd.readLine()) != null) {
				sb.append(line);
			}

			rd.close();
			conn.disconnect();
			String result = sb.toString();

			// 받아온 데이터 확인
			logger.debug("요청하고 값가져온거 확인:" + result);
			
			// Json parser를 만들어 만들어진 문자열 데이터를 객체화
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObj = (JSONObject) jsonParser.parse(result);

			//// response 키를 가지고 데이터를 파싱
			JSONObject parse_response = (JSONObject) jsonObj.get("response");

			// response 로 부터 body 찾기
			JSONObject parse_body = (JSONObject) parse_response.get("body");

			// body 로 부터 items 찾기
			JSONObject parse_items = (JSONObject) parse_body.get("items");

			// items로 부터 itemlist 를 받기
			JSONArray parse_item = (JSONArray) parse_items.get("item");

			JSONObject obj;
			String category;
			
			// 해당 item을 가져옴
			for (int i = 0; i < parse_item.size(); i++) {
				obj = (JSONObject) parse_item.get(i);
				fcDto.fcastDate = (String) obj.get("fcstDate");
				fcDto.fcastTime = (String) obj.get("fcstTime");
				category = (String) obj.get("category");

				switch (category) {
				case "SKY":
					fcDto.SKY = (String) (obj.get("fcstValue"));
					break;
				case "T3H":
					fcDto.FTH = (String) (obj.get("fcstValue"));
					logger.debug("fcDto 다들어갓는지 확인 "+fcDto.district_ID+fcDto.fcastDate + "/" + fcDto.fcastTime + "/" + fcDto.FTH +"/" +fcDto.SKY);
					FcastWeatherVO fw = new FcastWeatherVO(fcDto.district_ID,fcDto.fcastTime,fcDto.fcastDate,fcDto.SKY,fcDto.FTH);
					fwlist.add(fw);
					break;
				}
				
			}

		} catch (MalformedURLException e) {
			logger.debug("MalformedURLException : " + e.getMessage());
		} catch (IOException e) {
			logger.debug("IOException : " + e.getMessage());
		}
		
	return fwlist;
	}

}
