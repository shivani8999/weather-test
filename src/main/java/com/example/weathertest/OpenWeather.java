package com.example.weathertest;

import java.io.IOException;
import java.net.URLEncoder;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.net.www.http.HttpClient;

public class OpenWeather extends HttpClient {

	private static final long serialVersionUID = 1L;
	private String apiBase = "https://samples.openweathermap.org/data/2.5/forecast/hourly?q=";

	private String apiKey = "b6907d289e10d714a6e88b30761fae22";
	public final static Logger log = LoggerFactory.getLogger(OpenWeather.class);

	

	public OpenWeather(String reservedKey) {
		super();
	}

	private JSONObject fetch(String location) throws ClientProtocolException, IOException, JSONException {

		// https://samples.openweathermap.org/data/2.5/forecast/hourly?q=London,us&appid=b6907d289e10d714a6e88b30761fae22
		JSONObject obj = null;
		String apiUrl = apiBase + URLEncoder.encode(location, "utf-8") + "&appid=" + apiKey;

		//String output = null;
		String response = this.get(apiUrl);
	//	System.out.println("response :: " + response.toString());
		log.debug("Respnse: {}", response);
		// output = apiUrl.replace("[", "").replace("]", "");
		try {
			obj = new JSONObject(response.substring(response.indexOf('{')));
		} catch (Exception e) {

		}
		return obj;
	}

	private String get(String apiUrl) {
		String apiUrl1 = apiBase + "London" + "," + "us" + "&appid=" + apiKey;
		return apiUrl1;
	}

	private String dayWithMaximumTemperature(String location, int i)
			throws ClientProtocolException, IOException, JSONException {
		String maxTemp = null;
		JSONObject obj = fetch(location);
		if (null != obj) {
			maxTemp = obj.getJSONObject("main").get("temp_max").toString();
			System.out.println("maxTemp: " + maxTemp);
		}
		return maxTemp;
	}

	private String[] forGivenDateTempMinMaxHumidityRain(String location, int date)
			throws ClientProtocolException, JSONException, IOException {
		String[] result = new String[4];
		JSONObject obj = fetch(location);
		if (null != obj) {
			result[1] = obj.getJSONObject("main").get("temp_max").toString();
			result[2] = obj.getJSONObject("main").get("temp_min").toString();
			result[3] = obj.getJSONObject("main").get("humidity").toString();
			System.out.println("temp_max :: " + result[1]);
			System.out.println("temp_min :: " + result[2]);
			System.out.println("humidity :: " + result[3]);
		}
		return result;
	}

	public String[] fetchCurrentWeather(String location) throws ClientProtocolException, IOException, JSONException {
		String[] result = new String[10];
		result[0] = "error";
		try {
			JSONObject obj = fetch(location);
			result[0] = obj.getJSONArray("weather").getJSONObject(0).get("description").toString();
			result[1] = obj.getJSONObject("main").get("temp").toString();
			result[2] = location;
			result[3] = obj.getJSONArray("weather").getJSONObject(0).get("id").toString();
			result[4] = obj.getJSONObject("main").get("pressure").toString();
			result[5] = obj.getJSONObject("main").get("humidity").toString();
			result[6] = obj.getJSONObject("main").get("temp_min").toString();
			result[7] = obj.getJSONObject("main").get("temp_max").toString();
			result[8] = obj.getJSONObject("wind").get("speed").toString();
			result[9] = obj.getJSONObject("wind").get("deg").toString();
			result[9] = obj.getJSONObject("clouds").get("all").toString();
			return result;
		} catch (Exception e) {
			log.error("openweathermap error ( api ? ) : ", e);
			return result;
		}
	}

	private void error(String string) {
		// TODO Auto-generated method stub

	}

	public String getApiBase() {
		return apiBase;
	}

	public void setApiBase(String apiBase) {
		this.apiBase = apiBase;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public static void main(String[] args) {

		OpenWeather owm = new OpenWeather("weather");
		owm.setApiKey("b6907d289e10d714a6e88b30761fae22");
		// owm.startService();
		try {
			
			 

			String dayWithMaximumTemperature = owm.dayWithMaximumTemperature("London,us", 0);

			String[] forGivenDateTempMinMaxHumidityRain = owm.forGivenDateTempMinMaxHumidityRain("London,us",
					1553709600);

			// String sentence = "("+fetchForecast[3]+") In " + fetchForecast[2] + " the
			// weather is " + fetchForecast[0] + ". " + fetchForecast[1] + " degrees " +
			// fetchForecast[10];
			// log.info(sentence);
			log.info(dayWithMaximumTemperature);
			// log.info(forGivenDateTempMinMaxHumidityRain);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
