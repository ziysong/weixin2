package com.szy.weixin.weather;

public class Results {

	private String currentCity;
	private String pm25;
	private Index[] index;
	private WeatherData[] weather_data;
	
	public String getCurrentCity() {
		return currentCity;
	}
	public void setCurrentCity(String currentCity) {
		this.currentCity = currentCity;
	}
	public String getPm25() {
		return pm25;
	}
	public void setPm25(String pm25) {
		this.pm25 = pm25;
	}
	public Index[] getIndex() {
		return index;
	}
	public void setIndex(Index[] index) {
		this.index = index;
	}
	public WeatherData[] getWeather_data() {
		return weather_data;
	}
	public void setWeather_data(WeatherData[] weather_data) {
		this.weather_data = weather_data;
	}
	
}
