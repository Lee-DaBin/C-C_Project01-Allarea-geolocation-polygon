<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><!-- 한글깨질때 사용코드 -->
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ page session="false"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<title>Home</title>

<head>
<link rel="stylesheet" href="resources/style/testcss.css" />

</head>
<body>
	<div class="map_wrap">
		<div id="map"
			style="width: 60%; height: 100%; position: relative; overflow: hidden; float: left;"></div>
		<!-- 지도 -->

		<div id="menu_wrap1" class="bg_white">
			<img alt="로고 사진"
				src='<spring:url value="/resources/img/logo.png"></spring:url>'
				id="logo">
		</div>

		<div id="menu_wrap3" class="custom_typecontrol radius_border bg_white">
			<span id="myWeather" class="selected_btn"
				onclick="setMapType('myWeather',map)">내 위치</span> <span
				id="area_Weather" class="btn"
				onclick="setMapType('area_Weather',map)">전국날씨</span>
		</div>

	</div>


	<!-- 예보날씨 div-->
	<div id="fwWather">

		<div id="fwWather_01">

			<h3 id="main_text">${y}년
				${M}월 ${d}일 ${w}요일 <br> <b id="mainAreaName"
					style="color: #030066;">${mainAreaName}</b> 기상 예보
			</h3>
			<br>

		</div>

		<div id="fwWather_table">
			<br>
			<table id="fc">

				<tr>
					<td id="DateTD" colspan="8"><b>오늘 </b><small><b>${d}일</b></small></td>
				</tr>

				<tr id="fcWeather_DAY_tr1">

					<td id="DataTD_now"></td>
					<td id="DataTD"></td>

				</tr>
				<tr>
					<td></td>
				</tr>
				<tr>
					<td id="DateTD" colspan="8" align="center"><b>내일 </b><small>
							<b>${d2}일</b>
					</small></td>
				</tr>

				<tr id="fcWeather_DAY_tr2">
					<td id="DataTD"></td>
				</tr>
				<tr>
					<td></td>
				</tr>
				<tr>
					<td id="DateTD" colspan="8"><b>모레 </b><small><b>
								${d3}일</b></small></td>
				</tr>

				<tr id="fcWeather_DAY_tr3">
					<td id="DataTD"></td>
				</tr>

			</table>
		</div>

	</div>

	<div style="height: 10px; position: relative;"></div>

</body>

<script src="http://code.jquery.com/jquery-3.4.1.min.js"></script>
<!-- 카카오지도 -->
<script type="text/javascript"
	src="//dapi.kakao.com/v2/maps/sdk.js?appkey=6d476c3311dc5022627d4e6bf2604d8f&libraries=services,"></script>
<script>
	var mapContainer = document.getElementById('map'), // 지도를 표시할 div 

	mapOption = {
		center : new kakao.maps.LatLng(36.1, 127), // 지도의 중심좌표
		level : 4
	};

	var map = new kakao.maps.Map(mapContainer, mapOption); // 지도를 생성합니다
	var polygons = [];
	var markers = [];
	var overlays = [];

	var customOverlay = new kakao.maps.CustomOverlay({});

	main_now_Weather(map);

	// 현재 위치 ----------------------------------------------------------------------------------------------------------
	function main_now_Weather(map) {

		map.setMinLevel(3);
		map.setMaxLevel(13);

		// HTML5의 geolocation으로 사용할 수 있는지 확인합니다 
		if (navigator.geolocation) {

			// GeoLocation을 이용해서 접속 위치를 얻어옵니다
			navigator.geolocation.getCurrentPosition(function(position) {

				var lat = position.coords.latitude, // 위도
				lon = position.coords.longitude; // 경도

				var locPosition = new kakao.maps.LatLng(lat, lon) // 마커가 표시될 위치를 geolocation으로 얻어온 좌표로 생성합니다

				var geocoder = new kakao.maps.services.Geocoder();

				var callback = function(result, status) {
					if (status === kakao.maps.services.Status.OK) {

						now_district_name_step1 = result[0].address_name;
						console.log('1-1) 지역 명칭 : ' + result[0].address_name);
						now_district_ID = result[0].code;
						console.log('1-2) 행정구역 코드 : ' + now_district_ID);
						console.log('1-3) 행정구역 코드 시도 : '
								+ now_district_ID.substring(0, 4));
						console.log("위도/" + locPosition.getLng() + "경도/"
								+ locPosition.getLat());

						// 시도만 표기하기
						var area_names = now_district_name_step1.split(" "); // [0] = 시도 , [1] = 시군구 [2] = 읍면동

						area_names_step03 = area_names[2]; //현재위치현재날씨에 넣을 지역이름

						$('#mainAreaName').html(
								area_names[0] + " " + area_names[1]); //현재지역이름 메인 페이지에 전달

						default_fcastWeather(now_district_ID);
						default_nowWeather(now_district_ID, area_names_step03,
								locPosition);

						// 지도 중심좌표를 접속위치로 변경합니다
						map.setCenter(locPosition);

					}
				};

				geocoder.coord2RegionCode(locPosition.getLng(), locPosition
						.getLat(), callback);

			});

		} else { // HTML5의 GeoLocation을 사용할 수 없을때 마커 표시 위치와 인포윈도우 내용을 설정합니다

			var locPosition = new kakao.maps.LatLng(37, 127), message = 'geolocation을 사용할수 없어요..'
			console.log(message);

		}

	} // 현재위치------------------------------------------------------------------------------------------------------------------------------------

	// 현재해당지역 현재날씨 요청해오기 ----------------------------------------------------
	function default_nowWeather(now_district_ID, area_names_step03, locPosition) {
		$.ajax({
					url : "nowWeather",
					type : "POST",
					data : now_district_ID,

					success : function(data) {
						console.log("1) 받아온 현재날씨" + data);
						var nwList = JSON.parse(data);
						console.log(JSON.stringify(nwList));
						console.log("1) " + Object.keys(nwList));

						var keyList = Object.keys(nwList);

						for ( var i in keyList) {

							console.log("2) " + keyList[i] + ",  data="
									+ nwList[keyList[i]]);

							var obj = nwList[keyList[i]];

							for ( var key in obj) {
								//지역 선택
								console.log("3) " + "현재날씨리스트순서: " + key
										+ ", value: " + obj[key]);

								var obj2 = obj[key];

								// 현재날시에 나타낼 정보 변수  
								var district_name_step1;
								var district_name_step2;
								var d_ID;
								var NTH;
								var SKY;
								var X;
								var Y;

								for ( var area in obj2) {
									console.log("4) " + "현재날씨 내용 목록: " + area
											+ ", value: " + obj2[area]);

									if (area == 'District_ID') {
										d_ID = obj2[area];
									} else if (area == 'NTH') {
										NTH = obj2[area];
									} else if (area == 'SKY') {
										SKY = obj2[area];
									} else if (area == 'X') {
										X = obj2[area];
									} else if (area == 'Y') {
										Y = obj2[area];
									} else if (area == 'district_name_step1') {
										district_name_step1 = obj2[area];
									} else if (area == 'district_name_step2') {
										district_name_step2 = obj2[area];
									}
									;
								}
								//현재날씨 지역버튼생성하기
								console.log("지역 아이디 =" + d_ID + " / name_01 ="
										+ district_name_step1 + " / name_02 ="
										+ district_name_step2 + " / 하늘상태 ="
										+ SKY + "/ 온도 =" + NTH + "/ X =" + X
										+ " / Y =" + Y);

								// SKY 하늘상태 이미지화 
								console.log("하늘상태 확인 = " + SKY);

								if (SKY == 1) {
									SKY = "1.gif";
								} else if (SKY == 2) {
									SKY = "2.png";
								} else if (SKY == 3) {
									SKY = "3.png";
								} else {
									SKY = "4.png";
								};

								console.log("하늘상태 이미지 변환 주소확인 = " + SKY);

								// 버튼생성 HTML 
								// 현재날씨는  step03으로 이름 표시
								var content = '<div id = "nw_wather_text"value= "';
									content+= d_ID+'">';
									content+= area_names_step03
									content+= '  <b style="padding-top:20px;">';
									content+= NTH;
									content+= '℃</b>   <img src="resources/img/'+SKY+'"';
									content+= 'alt="날씨사진" id ="Nwimg" style="height: 50px; width: 70px;"> </div>';

								console.log("버튼 HTML 확인=" + content);

								// 마커를 생성합니다
								var imageSrc = "https://www.nsdi.go.kr/lxmap/images/marker/makers07.png"; // 마커 이미지
								var imageSize = new kakao.maps.Size(60, 60); //마커 이미지 크기  
								var markerImage = new kakao.maps.MarkerImage(
										imageSrc, imageSize); // 마커 이미지를 생성

								// 마커 생성
								var marker = new kakao.maps.Marker({
									image : markerImage, // 마커 이미지
									position : locPosition,
									yAnchor : 0.1
								});
										
								markers.push(marker)
								marker.setMap(map);

								//현재날씨 지역 버튼 생성 
								var overlay = new kakao.maps.CustomOverlay({
									content : content,
									map : map,
									position : locPosition,
									yAnchor : 1.5
								});

								overlays.push(overlay);
							}
						}

					},
					error : function() {
						alert("에러발생");
					}

				});
	} //현재 해당지역 현재 날씨 요청------------------------------------------------------------

	// 현재해당지역 예보 날씨 요청해오기 ----------------------------------------------------
	function default_fcastWeather(now_district_ID) {
		$
				.ajax({
					url : "now_area_fcWeatherList",
					type : "POST",
					data : now_district_ID,

					success : function(data) {
						console.log(" 1) 받아온 FCList" + data);

						var tag;
						var tag1;
						var tag2;
						var tag3;

						//3일 예보 날찌 
						var d1 = ${d};
						var d2 = ${d2};
						var d3 = ${d3};

						//현재 시간
						var nowTime = ${nowTime};

						var fwList = JSON.parse(data);
						console.log(fwList);
						console.log(JSON.stringify(fwList));

						console.log("1) " + Object.keys(fwList));

						var keyList = Object.keys(fwList);

						for ( var i in keyList) {
							console.log("2) " + keyList[i] + ",  data="+ fwList[keyList[i]]);

							var obj = fwList[keyList[i]];

							for ( var fcdatekey in obj) {
								console.log("3) " + "예보날씨 날짜 순서: " + fcdatekey+ ", value: " + obj[fcdatekey]);

								var obj2 = obj[fcdatekey];

								// 선택예보날씨에 나타낼 정보 변수  
								var d_name;
								var fcTime;
								var FTH;
								var fcDate;
								var SKY;

								for ( var area in obj2) {
									console.log("4) " + "예보날씨 내용 목록: " + area+ ", value: " + obj2[area]);

									if (area == 'district_name_step1') {
										d_name = obj2[area];
									} else if (area == 'fcTime') {
										fcTime = obj2[area];
									} else if (area == 'FTH') {
										FTH = obj2[area];
									} else if (area == 'fcDate') {
										fcDate = obj2[area];
									} else if (area == 'SKY') {
										SKY = obj2[area];
									};
								}

								console.log("지역이름/" + d_name + "/ 예보시간/"+ fcTime + "/ 예보온도/" + FTH + "/ 하늘상태/"+ SKY);

								// SKY 하늘상태 이미지화 
								console.log("예보날씨 하늘상태 확인 = " + SKY);

								if (SKY == 1) {
									SKY = "1.gif";
								} else if (SKY == 2) {
									SKY = "2.png";
								} else if (SKY == 3) {
									SKY = "3.png";
								} else {
									SKY = "4.png";
								}
								;

								console.log("하늘상태 이미지 변환 주소확인 = " + SKY);

								tag = fcTime.substr(0, 2)
										+ '시<br><img id="fwimg" src="resources/img/'+SKY+'"'
										+ 'alt="날씨사진"><br>'
										+ FTH.substr(0, 2) + '℃';

								console.log("예보날씨 HTML 확인=" + tag);
								console.log("예보시간별 <td>생성하기 시간비교="+ fcDate.substr(6, 8) + '같은지 확인' + d1);

								//예보날씨 예보시간별 <td>생성하기
								if (fcDate.substr(6, 8) == d1) {
									console.log("시간비교=" + parseInt(nowTime)+ "크기비교" + parseInt(fcTime));
									if (parseInt(nowTime) > parseInt(fcTime)) {
										
										console.log("지난예보");
										tag1 += '<td id ="DataTD_now">' + tag+ '</td>';

									} else {
										tag1 += '<td id ="DataTD">' + tag+ '</td>';
									}

								} else if (fcDate.substr(6, 8) == d2) {

									tag2 += '<td id ="DataTD">' + tag + '</td>';

								} else if (fcDate.substr(6, 8) == d3) {

									tag3 += '<td id ="DataTD">' + tag + '</td>';

								};
							}
						}
						console.log("예보날씨 HTML 확인=" + tag1);
						console.log("예보날씨 HTML 확인=" + tag2);
						console.log("예보날씨 HTML 확인=" + tag3);

						console.log("예보날씨지역 확인=" + d_name);

						$('#fcWeather_DAY_tr1').html(tag1);
						$('#fcWeather_DAY_tr2').html(tag2);
						$('#fcWeather_DAY_tr3').html(tag3);
					}
				});
	} ///////// 현재해당지역 예보날씨 요청--------------------------------------------------

	//지도 위 표시 제거
	function deletePolygon(polygons) {
		for (var i = 0; i < polygons.length; i++) {
			polygons[i].setMap(null);
		}
		polygons = [];
	}

	function setMarkers(map) {
		for (var i = 0; i < markers.length; i++) {
			markers[i].setMap(map);
		}
	}

	function setoverlays(map) {
		for (var i = 0; i < overlays.length; i++) {
			overlays[i].setMap(map);
		}
	}
	//------------------------------------------------------------------------------------

	// 버튼 기능 --------------------------------------------------------------------------------------------------------
	function setMapType(maptype, map) {
		var myWeather = document.getElementById('myWeather');
		var area_Weather = document.getElementById('area_Weather');

		if (maptype === 'myWeather') {

			main_now_Weather(map);

			if (polygons != null) {
				console.log("polygons 확인" + polygons);
				deletePolygon(polygons); // 폴리곤 제거
			}

			map.setLevel(4); // 확대 level 변경

			//버튼색바꾸기 
			myWeather.className = 'selected_btn';
			area_Weather.className = 'btn';

		} else {
			setMarkers(null);
			setoverlays(null);
			//버튼색바꾸기 
			area_Weather.className = 'selected_btn';
			myWeather.className = 'btn';

			// 지도 중심좌표를 접속위치로 변경합니다
			var area_locPosition = new kakao.maps.LatLng(37, 127);
			map.setCenter(area_locPosition);

			map.setLevel(13); // 확대 level 변경

			if (polygons != null) {
				console.log("polygons 확인" + polygons);
				deletePolygon(polygons); // 폴리곤 제거
			}

			DrowPolygon() // 폴리곤 삽입
		}

	}

	//행정구역 구분 = 폴리곤 그리는 함수 ------------------------------------------------------------------------------------------
	function DrowPolygon() {
		$.getJSON("resources/json/CTPRVN.json", function(geojson) {

			var data = geojson.features;
			console.log("1)" + data);

			var name = ''; //행정 구 이름
			var area_code = ''; // 행정구역 코드

			$.each(data, function(index, val) {

				name = val.properties.CTP_KOR_NM;
				area_code = val.properties.CTPRVN_CD;

				if (val.geometry.type == "MultiPolygon") {
					coordinates = val.geometry.coordinates;
					console.log("2-1)" + name + "/" + area_code);
					//console.log("좌표"+coordinates);
					displayArea(coordinates, name, area_code, true);

				} else {
					coordinates = val.geometry.coordinates;
					console.log("2-2)" + name + "/" + area_code);
					//console.log("좌표"+coordinates);
					displayArea(coordinates, name, area_code, false);
				}

			});
		});
	}; // 행정구역 구분 = 폴리곤 그리는 함수 -------------------------------------------------------------------------------------------

	// polygon 구분 함수  -------------------------------------------------------------------------------------------------------
	function displayArea(coordinates, name, area_code, multi) {
		var polygon;

		if (multi) {
			polygon = makeMultiPolygon(coordinates);
		} else {
			polygon = makePolygon(coordinates);
		}

		polygon.setMap(map);

		// 다각형에 mouseover 이벤트를 등록하고 이벤트가 발생하면 폴리곤의 채움색을 변경합니다 
		// 지역명을 표시하는 커스텀오버레이를 지도위에 표시합니다
		kakao.maps.event
				.addListener(
						polygon,
						'mouseover',
						function(mouseEvent) {

							polygon.setOptions({
								fillColor : '#09f'
							});

							customOverlay
									.setContent('<div id="step01_area_name" style=" position: absolute;background: #fff; font-weight: bolder; border-radius: 3px; font-size: 30px; top: -5px;left: 15px;padding:10px;">'
											+ name + '</div>');
							customOverlay.setPosition(mouseEvent.latLng);
							overlays.push(customOverlay);
							customOverlay.setMap(map);
						});

		// 다각형에 mousemove 이벤트를 등록하고 이벤트가 발생하면 커스텀 오버레이의 위치를 변경합니다 
		kakao.maps.event.addListener(polygon, 'mousemove',
				function(mouseEvent) {

					customOverlay.setPosition(mouseEvent.latLng);
				});

		// 다각형에 mouseout 이벤트를 등록하고 이벤트가 발생하면 폴리곤의 채움색을 원래색으로 변경합니다
		// 커스텀 오버레이를 지도에서 제거합니다 
		kakao.maps.event.addListener(polygon, 'mouseout', function() {
			customOverlay.setMap(null);
			polygon.setOptions({
				fillColor : '#fff'
			});

		});

		// 다각형에 click 이벤트를 등록하고 이벤트가 발생하면 해당 지역 확대을 확대합니다.
		kakao.maps.event.addListener(polygon, 'click', function() {
			customOverlay.setMap(null);
			console.log("클릭한 지역명" + name + "지역코드" + area_code);
			centroid(area_code, polygon, name);// 중심좌표 구해와서 확대하기
		});
	} // polygon 구분 함수  -------------------------------------------------------------------------------------------------------

	// 중심좌표 구해와서 확대 --------------------------------------------------------------------------------------------------------
	function centroid(area_code, polygon, name) {
		var X
		var Y

		$.ajax({
			url : "area_centroid",
			type : "POST",
			data : area_code,

			success : function(data) {
				var centroid = JSON.parse(data);
				console.log("1) 받아온 중심좌표" + JSON.stringify(centroid));

				var keyList = Object.keys(centroid);

				for ( var i in keyList) {
					console.log("2) " + keyList[i] + ",  data="
							+ centroid[keyList[i]]);

					if (keyList[i] == "X") {
						X = centroid[keyList[i]];
					} else if (keyList[i] == "Y") {
						Y = centroid[keyList[i]];
					}

				}
				console.log("3) " + X + "/" + Y);

				map.setLevel(9);
				// 지도 중심좌표를 접속위치로 변경합니다
				var area_locPosition = new kakao.maps.LatLng(X, Y);
				map.setCenter(area_locPosition);

				console.log("도or광역시 확인 = " + name.slice(-1));
				if (name.slice(-1) == "도") {
					map.setLevel(10);
				} else {
					map.setLevel(7);
				}

				$('#mainAreaName').html(name); //지역이름 메인 페이지에 전달
				deletePolygon(polygons); // 폴리곤 제거

				setoverlays(null);
				area_now_Weather(area_code, map);// 광역시별 -> 시군 현재날씨 
				area_fcast_Weather(area_code)// 광역시 예보날씨 default = 광역시 
			},
			error : function() {
				alert("에러발생");
			}

		});

	} // 중심좌표 구해와서 확대 --------------------------------------------------------------------------------------------------------

	// 광역시 예보날씨(default=광역시)-----------------------------------------------------------------------------------------------------------------------
	function area_fcast_Weather(area_code, map) {
		$.ajax({
					url : "select_area_fcWeatherList",
					type : "POST",
					data : area_code,

					success : function(data) {
						console.log(" 1) 받아온 FCList" + data);

						var tag;
						var tag1;
						var tag2;
						var tag3;

						//3일 예보 날찌 
						var d1 = $
						{
							d
						}
						;
						var d2 = $
						{
							d2
						}
						;
						var d3 = $
						{
							d3
						}
						;

						//현재 시간
						var nowTime = $
						{
							nowTime
						}
						;

						var fwList = JSON.parse(data);
						console.log(fwList);
						console.log(JSON.stringify(fwList));

						console.log("1) " + Object.keys(fwList));

						var keyList = Object.keys(fwList);

						for ( var i in keyList) {
							console.log("2) " + keyList[i] + ",  data="
									+ fwList[keyList[i]]);

							var obj = fwList[keyList[i]];

							for ( var fcdatekey in obj) {
								console.log("3) " + "예보날씨 날짜 순서: " + fcdatekey
										+ ", value: " + obj[fcdatekey]);

								var obj2 = obj[fcdatekey];

								// 선택예보날씨에 나타낼 정보 변수  
								var d_name;
								var fcTime;
								var FTH;
								var fcDate;
								var SKY;

								for ( var area in obj2) {
									console.log("4) " + "예보날씨 내용 목록: " + area
											+ ", value: " + obj2[area]);

									if (area == 'district_name_step1') {
										d_name = obj2[area];
									} else if (area == 'fcTime') {
										fcTime = obj2[area];
									} else if (area == 'FTH') {
										FTH = obj2[area];
									} else if (area == 'fcDate') {
										fcDate = obj2[area];
									} else if (area == 'SKY') {
										SKY = obj2[area];
									}
									;
								}

								console.log("지역이름/" + d_name + "/ 예보시간/"
										+ fcTime + "/ 예보온도/" + FTH + "/ 하늘상태/"
										+ SKY);

								// SKY 하늘상태 이미지화 
								console.log("예보날씨 하늘상태 확인 = " + SKY);

								if (SKY == 1) {
									SKY = "1.gif";
								} else if (SKY == 2) {
									SKY = "2.png";
								} else if (SKY == 3) {
									SKY = "3.png";
								} else {
									SKY = "4.png";
								}
								;

								console.log("하늘상태 이미지 변환 주소확인 = " + SKY);

								tag = fcTime.substr(0, 2)
										+ '시<br><img id="fwimg" src="resources/img/'+SKY+'"'
										+ 'alt="날씨사진"><br>'
										+ FTH.substr(0, 2) + '℃';

								console.log("예보날씨 HTML 확인=" + tag);

								//예보날씨 예보시간별 <td>생성하기
								if (fcDate.substr(6, 8) == d1) {

									if (parseInt(nowTime) > parseInt(fcTime)) {
										tag1 += '<td id ="DataTD_now">' + tag
												+ '</td>';

									} else {
										tag1 += '<td id ="DataTD">' + tag
												+ '</td>';
									}

								} else if (fcDate.substr(6, 8) == d2) {

									tag2 += '<td id ="DataTD">' + tag + '</td>';

								} else if (fcDate.substr(6, 8) == d3) {

									tag3 += '<td id ="DataTD">' + tag + '</td>';

								}
								;
							}
						}
						console.log("예보날씨 HTML 확인=" + tag1);
						console.log("예보날씨 HTML 확인=" + tag2);
						console.log("예보날씨 HTML 확인=" + tag3);

						console.log("예보날씨지역 확인=" + d_name);

						$('#fcWeather_DAY_tr1').html(tag1);
						$('#fcWeather_DAY_tr2').html(tag2);
						$('#fcWeather_DAY_tr3').html(tag3);
					}
				});

	}

	// 지역선택 현재날시 
	function area_now_Weather(area_code) {
		setoverlays(null);
		console.log(area_code + "시도별 현재날씨요청");

		$.ajax({
					url : "select_area_NowWeather",
					type : "POST",
					data : area_code,

					success : function(data) {

						var select_area_NowWeather = JSON.parse(data);

						console.log("1) 받아온 광역시별 현재날씨 모음 확인"
								+ JSON.stringify(select_area_NowWeather));

						var keyList = Object.keys(select_area_NowWeather);

						console.log("2) 받아온 광역시별 현재날씨 keyList = " + keyList);

						var area_name = '';

						for ( var i in keyList) { //01 for

							console.log("3) " + keyList[i] + ",  data="
									+ select_area_NowWeather[keyList[i]]);

							area_name = keyList[i];

							var obj = select_area_NowWeather[keyList[i]];

							for ( var nwW_index in obj) { //02 for
								console.log("4) " + "선택지역 현재날씨 " + nwW_index
										+ ", value: " + obj[nwW_index]);

								var obj2 = obj[nwW_index];

								// 현재날시에 나타낼 정보 변수  
								var district_name_step1;
								var district_name_step2;
								var d_ID;
								var NTH;
								var SKY;
								var X;
								var Y;

								for ( var area in obj2) { //03 for
									console.log("5) " + "현재날씨 내용 목록: " + area
											+ ", value: " + obj2[area]);

									if (area == 'District_ID') {
										d_ID = obj2[area];
									} else if (area == 'NTH') {
										NTH = obj2[area];
									} else if (area == 'SKY') {
										SKY = obj2[area];
									} else if (area == 'X') {
										X = obj2[area];
									} else if (area == 'Y') {
										Y = obj2[area];
									} else if (area == 'district_name_step1') {
										district_name_step1 = obj2[area];
									} else if (area == 'district_name_step2') {
										district_name_step2 = obj2[area];
									}

								} //03 for

								// 선택지역별 시도 현재날씨버튼 생성하기 
								//현재날씨 지역버튼생성하기
								console.log("지역 아이디 =" + d_ID + " / name_01 ="
										+ district_name_step1 + " / name_02 ="
										+ district_name_step2 + " / 하늘상태 ="
										+ SKY + "/ 온도 =" + NTH + "/ X =" + X
										+ " / Y =" + Y);

								// SKY 하늘상태 이미지화 
								console.log("하늘상태 확인 = " + SKY);

								if (SKY == 1) {
									SKY = "1.gif";
								} else if (SKY == 2) {
									SKY = "2.png";
								} else if (SKY == 3) {
									SKY = "3.png";
								} else {
									SKY = "4.png";
								}
								;

								console.log("하늘상태 이미지 변환 주소확인 = " + SKY);

								// 버튼생성 HTML 
								// 현재날씨는  step03으로 이름 표시
								var content = '<div id="select_area_name"'
										+ "onclick="
										+ '"select_fcWeahter_area_step01('
										+ "'" + d_ID + "')" + '">' + area_name
										+ '  <b style="padding-top:20px;">';
								content += NTH
										+ '℃</b>   <img src="resources/img/'+SKY+'"';
									content += 'alt="날씨사진" id ="Nwimg" style="height: 25px; width: 40px;"> </div>';

								console.log("버튼 HTML 확인=" + content);

								//현재날씨 지역 버튼 생성 
								overlay = new kakao.maps.CustomOverlay({
									content : content,
									map : map,
									position : new kakao.maps.LatLng(X, Y),
									yAnchor : 1.3
								});

								overlays.push(overlay);
							}//02 for
						}//01 for	

					},
					error : function() {
						alert("에러발생");
					}

				});
	}

	function makePolygon(coordinates, name, area_code) {

		var path = []; //폴리곤 그려줄 path

		$.each(coordinates[0], function(index, coordinate) { //console.log(coordinates)를 확인해보면 보면 [0]번째에 배열이 주로 저장이 됨.  그래서 [0]번째 배열에서 꺼내줌.

			path.push(new kakao.maps.LatLng(coordinate[1], coordinate[0])); //new daum.maps.LatLng가 없으면 인식을 못해서 path 배열에 추가
		})

		// 다각형을 생성합니다 
		var polygon = new daum.maps.Polygon({
			map : map, // 다각형을 표시할 지도 객체
			path : path,
			strokeWeight : 2,
			strokeColor : '#004c80',
			strokeOpacity : 0.8,
			fillColor : '#fff',
			fillOpacity : 0.7
		});
		polygons.push(polygon); //폴리곤 제거하기 위한 배열
		return polygon;
	}

	function makeMultiPolygon(coordinates, name, area_code) {

		var path = []; //폴리곤 그려줄 path

		$.each(coordinates, function(index, val2) {
			var coordinates2 = [];

			$.each(val2[0], function(index2, coordinate) {
				coordinates2.push(new kakao.maps.LatLng(coordinate[1],
						coordinate[0]));
			});

			path.push(coordinates2);
		});

		// 다각형을 생성합니다 
		var polygon = new daum.maps.Polygon({
			map : map, // 다각형을 표시할 지도 객체
			path : path,
			strokeWeight : 2,
			strokeColor : '#004c80',
			strokeOpacity : 0.8,
			fillColor : '#fff',
			fillOpacity : 0.7
		});
		polygons.push(polygon); //폴리곤 제거하기 위한 배열
		return polygon;
	}

	// 지역선택 예보날씨 가져오기
	function select_fcWeahter_area_step01(areaID) {
		console.log(" 받아온 areaID" + areaID);
		var areaID = areaID.toString();

		$.ajax({
					url : "select_area_fcWeatherList_step02",
					type : "POST",
					data : areaID,

					success : function(data) {
						console.log(" 1) 받아온 FCList" + data);

						var tag;
						var tag1;
						var tag2;
						var tag3;

						//3일 예보 날찌 
						var d1 = ${d};
						var d2 = ${d2};
						var d3 = ${d3};

						//현재 시간
						var nowTime = ${nowTime};

						var fwList = JSON.parse(data);
						console.log(fwList);
						console.log(JSON.stringify(fwList));

						console.log("1) " + Object.keys(fwList));

						var keyList = Object.keys(fwList);

						for ( var i in keyList) {
							console.log("2) " + keyList[i] + ",  data="+ fwList[keyList[i]]);

							var obj = fwList[keyList[i]];

							for ( var fcdatekey in obj) {
								console.log("3) " + "예보날씨 날짜 순서: " + fcdatekey+ ", value: " + obj[fcdatekey]);

								var obj2 = obj[fcdatekey];

								// 선택예보날씨에 나타낼 정보 변수  
								var district_name_step1;
								var district_name_step2;
								var fcTime;
								var FTH;
								var fcDate;
								var SKY;

								for ( var area in obj2) {
									console.log("4) " + "예보날씨 내용 목록: " + area
											+ ", value: " + obj2[area]);

									if (area == 'district_name_step1') {
										district_name_step1 = obj2[area];
									} else if (area == 'district_name_step2') {
										district_name_step2 = obj2[area];
									} else if (area == 'fcTime') {
										fcTime = obj2[area];
									} else if (area == 'FTH') {
										FTH = obj2[area];
									} else if (area == 'fcDate') {
										fcDate = obj2[area];
									} else if (area == 'SKY') {
										SKY = obj2[area];
									}
									;
								}

								console.log("지역별 이름 확인 district_name_step1 ="
										+ district_name_step1
										+ '// district_name_step2'
										+ district_name_step2)
								if (district_name_step2 == null) {
									$('#mainAreaName')
											.html(district_name_step1); //현재지역이름 메인 페이지에 전달

								} else {
									$('#mainAreaName').html(
											district_name_step1 + " "
													+ district_name_step2); //현재지역이름 메인 페이지에 전달
								}

								console.log("지역이름/" + district_name_step2
										+ "/ 예보시간/" + fcTime + "/ 예보온도/" + FTH
										+ "/ 하늘상태/" + SKY);

								// SKY 하늘상태 이미지화 
								console.log("예보날씨 하늘상태 확인 = " + SKY);

								if (SKY == 1) {
									SKY = "1.gif";
								} else if (SKY == 2) {
									SKY = "2.png";
								} else if (SKY == 3) {
									SKY = "3.png";
								} else {
									SKY = "4.png";
								};

								console.log("하늘상태 이미지 변환 주소확인 = " + SKY);

								tag = fcTime.substr(0, 2)
										+ '시<br><img id="fwimg" src="resources/img/'+SKY+'"'
										+ 'alt="날씨사진"><br>'
										+ FTH.substr(0, 2) + '℃';

								console.log("예보날씨 HTML 확인=" + tag);

								//예보날씨 예보시간별 <td>생성하기
								if (fcDate.substr(6, 8) == d1) {

									if (parseInt(nowTime) > parseInt(fcTime)) {
										tag1 += '<td id ="DataTD_now">' + tag
												+ '</td>';

									} else {
										tag1 += '<td id ="DataTD">' + tag
												+ '</td>';
									}

								} else if (fcDate.substr(6, 8) == d2) {

									tag2 += '<td id ="DataTD">' + tag + '</td>';

								} else if (fcDate.substr(6, 8) == d3) {

									tag3 += '<td id ="DataTD">' + tag + '</td>';

								};
							}
						}
						console.log("예보날씨 HTML 확인=" + tag1);
						console.log("예보날씨 HTML 확인=" + tag2);
						console.log("예보날씨 HTML 확인=" + tag3);

						console.log("예보날씨지역 확인=" + district_name_step2);

						$('#fcWeather_DAY_tr1').html(tag1);
						$('#fcWeather_DAY_tr2').html(tag2);
						$('#fcWeather_DAY_tr3').html(tag3);
					}
				});

	}
</script>

</html>
