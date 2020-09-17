package com.example.dao;

import java.util.List;

import com.example.dto.DistrictVO;
import com.example.dto.FcastWeatherVO;
import com.example.dto.NowWeatherVO;

public interface weatherDAO {
	

	public List<DistrictVO> selectarea() throws Exception;

	public List<FcastWeatherVO> selectfw(FcastWeatherVO fwVO_S);

	public List<NowWeatherVO> selectnw(NowWeatherVO nwVO_S);
	
	public List<NowWeatherVO> area_selectnw(NowWeatherVO nwVO_S);

	public int insertnw(NowWeatherVO nwVO_S);

	public int insertfw(List<FcastWeatherVO> fwList);
	
	

}
