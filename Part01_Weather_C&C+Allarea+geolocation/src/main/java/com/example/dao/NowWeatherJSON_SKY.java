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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.dto.NowWeatherVO;

public class NowWeatherJSON_SKY {
		
		// NowWeatherJSON_SKY = 초단기예보에서 SKY 정보가져오는 클래스
		private static final Logger logger = LoggerFactory.getLogger(NowWeatherJSON_SKY.class);
		
		final static String servicekey = "Mrwu0SqEmSX9PWA2wgTPid3kzKQlcBOuJV76ees1NkwS7aR3xsYx47eZt4bmM7gYn6QoN4J55hBElGdputuRpw%3D%3D";
		String apiUrl = "http://apis.data.go.kr/1360000/VilageFcstInfoService/getUltraSrtFcst";
		String numOfRows = "100";
		String baseDate;
		String baseTime;
		
		// 기준 날짜, 시간, 위도, 경도를 입력하면 해당하는 지역의 동내기상정보를 JSON데이터로 가져와 NowWeater객체를 만들어 반환합니다.
		public String getSKY(String now_day, String now_time, int x, int y) throws IOException, ParseException {
			logger.info("=================== [초단기예보 API] ===================");
			
			NowWeatherVO now ;
			baseDate = now_day;
			
			int Today = Integer.parseInt(now_day);
			int NOWTime = Integer.parseInt(now_time);
			
			NOWTime=NOWTime/100;
			
			// 현재시간, 현재날짜 API기준 시간 날짜로 변환해주기
			// 초단기예보 API Base_time -> (0030,0130,0230,0330, ...... 2330) 제공시간은 매시 45분
			
			if (NOWTime / 100 == 0 && NOWTime % 100 < 45) {
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
				baseTime = "2345";
				logger.debug("전날꺼 API 요청 확인 :"+ baseDate+"/"+baseTime);

			} else {

				if (NOWTime % 100 < 45) {

					NOWTime = NOWTime / 100 - 1;

					if (NOWTime < 10) {
						baseTime = "0" + Integer.toString(NOWTime) + "45";
					} else {
						baseTime = Integer.toString(NOWTime) + "45";
					}
				} else {
					NOWTime = NOWTime / 100;

					if (NOWTime < 10) {
						baseTime = "0" + Integer.toString(NOWTime) + "45";
					} else {
						baseTime = Integer.toString(NOWTime) + "45";
					}

				}
			}
			/*
			logger.info(("=================== [초단기 예보API 현재시간 날짜 API: baseTime, baseDate확인] ===================");
			logger.debug("현재 날짜 : " +Today+"API 조회 날짜 : " + baseDate);
			logger.debug("현재 시간 : " + NOWTime+"API 조회 시간 : " + baseTime);
			System.out.println("=========================================================================================");
			*/
			
			// JSON데이터를 요청하는 URL
			StringBuilder urlBuilder = new StringBuilder(apiUrl);
			String nx = Integer.toString(x);
			String ny = Integer.toString(y);
			JSONArray SkyList = new JSONArray();

			try {
				urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "=" + servicekey);
				urlBuilder.append("&" + URLEncoder.encode("nx", "UTF-8") + "=" + URLEncoder.encode(nx, "UTF-8")); // 경도
				urlBuilder.append("&" + URLEncoder.encode("ny", "UTF-8") + "=" + URLEncoder.encode(ny, "UTF-8")); // 위도
				urlBuilder.append("&" + URLEncoder.encode("base_date", "UTF-8") + "="
						+ URLEncoder.encode(baseDate, "UTF-8")); 
				urlBuilder.append("&" + URLEncoder.encode("base_time", "UTF-8") + "="
						+ URLEncoder.encode(baseTime, "UTF-8")); 
				urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "="
						+ URLEncoder.encode("JSON", "UTF-8")); 
				urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "="
						+ URLEncoder.encode(numOfRows, "UTF-8")); 

				// GET방식으로 전송해서 파라미터 받아오기
				URL url = new URL(urlBuilder.toString());

				//System.out.println(url); // 넘어가는지 확인

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
				logger.debug("요청하고 값가져온거 확인:" +result);

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

				// 확인 //System.out.println("itme입니다= "+parse_item);
				JSONObject obj;
				String category;
				
				// 해당 item을 가져옴
				for(int i = 0; i < parse_item.size(); i++) {
					obj = (JSONObject) parse_item.get(i);
					Object fcstValue = obj.get("fcstValue");
					Object fcstDate = obj.get("fcstDate");
					Object fcstTime = obj.get("fcstTime");
					category = (String)obj.get("category"); 
					
						
					if(category.equals("SKY")){
						logger.debug("현재날짜: "+ now_day+"현재시간: "+ now_time);
						logger.debug("\tcategory : " + category+","+"fast_time :"+fcstTime+" fcst_Value : " + fcstValue );
						SkyList.add(fcstValue);
						}		
				}
				
			} catch (MalformedURLException e) {System.out.println("MalformedURLException : " + e.getMessage());
			
			} catch (IOException e) {System.out.println("IOException : " + e.getMessage());
			
			} 
			return (String) SkyList.get(0);

		}

}
