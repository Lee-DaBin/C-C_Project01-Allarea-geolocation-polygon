package com.example.dao;

import java.util.List;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.example.dto.DistrictVO;
import com.example.dto.FcastWeatherVO;
import com.example.dto.NowWeatherVO;
import com.example.service.weatherServiceImpl;

@Repository
public class weatherDAOImpl implements weatherDAO {
	
	private static final Logger logger = LoggerFactory.getLogger(weatherDAOImpl.class);

	@Inject
	private SqlSession sqlSession;
	
	private static final String Namespace = "com.example.mapper.areaMapper";
	
	
	@Override
	public List<DistrictVO> selectarea() throws Exception {

		return sqlSession.selectList(Namespace+".selectarea");
	}


	@Override
	public List<FcastWeatherVO> selectfw(FcastWeatherVO fwVO_S) {
		return sqlSession.selectList(Namespace+".selectFcast_weather",fwVO_S);
	}

	@Override
	public List<NowWeatherVO> selectnw(NowWeatherVO nwVO_S) {
		return sqlSession.selectList(Namespace+".selectNow_weather",nwVO_S);
	}
	
	@Override
	public List<NowWeatherVO> area_selectnw(NowWeatherVO nwVO_S) {
		return sqlSession.selectList(Namespace+".area_selectNow_weather",nwVO_S);
	}


	@Override
	public int insertnw(NowWeatherVO nwVO_S) {
		return sqlSession.insert(Namespace+".insertNow_weather",nwVO_S);
	}


	@Override
	public int insertfw(List<FcastWeatherVO> fwList) {
		return sqlSession.insert(Namespace+".insertFcast_weather",fwList);
	}


	
}
