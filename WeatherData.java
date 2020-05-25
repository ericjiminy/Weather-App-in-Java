/**
 * 
 * @author Eric Chun
 *
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class WeatherData {

	protected static HttpURLConnection connection;
	protected String city;
	protected String state;
	protected String mapQuestURL;
	protected final String MAPQUESTKEY = "B0lvCw6Gqih9pLt3Hqltp3fyNQHTd9LX";

	protected String latitude;
	protected String longitude;

	protected String openWeatherURL;
	protected final String OPENWEATHERKEY = "87624a74b5755f3f42a1296f55d359c5";

	protected String currentTemp;   // - Current data
	protected String currentWeatherDescription;
	protected String currentTime;

	protected String[] hourlyHours = new String[12];   // - Hourly data	
	protected String[] hourlyIcons = new String[12];
	protected String[] hourlyTemperatures = new String[12];


	protected String[] dailyDays = new String[7];   // - Daily data
	protected String[] dailyIcons = new String[7];
	protected String[] dailyHighs = new String[7];
	protected String[] dailyLows = new String[7];


	// - Getters
	public String getCity() {
		return city;
	}
	public String getState() {
		return state;
	}
	public String getLatitude() {
		return latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public String getCurrentTemp() {
		return currentTemp;
	}
	public String getCurrentWeatherDescription() {
		return currentWeatherDescription;
	}
	public String getCurrentTime() {
		return currentTime;
	}
	public String[] getHourlyHours() {
		return hourlyHours;
	}
	public String[] getHourlyTemperatures() {
		return hourlyTemperatures;
	}
	public String[] getHourlyIcons() {
		return hourlyIcons;
	}
	public String[] getDailyDays() {
		return dailyDays;
	}
	public String[] getDailyIcons() {
		return dailyIcons;
	}
	public String[] getDailyHighs() {
		return dailyHighs;
	}
	public String[] getDailyLows() {
		return dailyLows;
	}


	// - Use MapQuest API to get latitude and longitude from a given city and state.
	protected String[] getCoordinates(String city, String state) {
		if (state.contentEquals("")) {
			mapQuestURL = "http://www.mapquestapi.com/geocoding/v1/address?key=" + MAPQUESTKEY + "&location=" + city + "&maxResults=1";   // - Remove comma after city if no state/country provided.
		} else {
			mapQuestURL = "http://www.mapquestapi.com/geocoding/v1/address?key=" + MAPQUESTKEY + "&location=" + city + "," + state + "&maxResults=1";   // - Build MapQuest URL.
		}
		return parseMapQuest(connectAndRequest(mapQuestURL).toString());   // - Request data from MapQuest URL and then search through the data to get latitude and longitude.
	}


	// - Navigate through MapQuest data to get latitude and longitude.
	protected String[] parseMapQuest(String responseBody) {
		JSONParser parser = new JSONParser();
		String[] coordinates = new String[2];
		coordinates[0] = "error";
		coordinates[1] = "error";
		try {
			JSONObject data = (JSONObject) parser.parse(responseBody);
			JSONArray results = (JSONArray) data.get("results");
			JSONObject resultsObject = (JSONObject) results.get(0);
			JSONArray locations = (JSONArray) resultsObject.get("locations");
			JSONObject locationsObject = (JSONObject) locations.get(0);
			city = locationsObject.get("adminArea5").toString();
			state = locationsObject.get("adminArea3").toString();
			if (state.contentEquals("")) {
				state = locationsObject.get("adminArea1").toString();
			}

			JSONObject latLng = (JSONObject) locationsObject.get("latLng");
			coordinates[0] = latLng.get("lat").toString();
			coordinates[1] = latLng.get("lng").toString();
			return coordinates;
		} catch (ParseException e) {
			e.printStackTrace();
			return coordinates;
		}
	}


	// - Use OpenWeather API to get weather data at a given latitude and longitude.
	protected boolean getWeatherData(String latitude, String longitude) {
		//		latitude = "38.840081";   // - FIND THESE WITH THE COORDINATES METHOD LATER;
		//		longitude = "-77.423164";
		openWeatherURL = "https://api.openweathermap.org/data/2.5/onecall?lat=" + latitude + "&lon=" + longitude + "&exclude=minutely&units=imperial&appid=" + OPENWEATHERKEY;   // - Build OpenWeather URL
		String responseBody = connectAndRequest(openWeatherURL).toString();
		return parseOpenWeather(responseBody);
	}


	// - Navigate through OpenWeather data to get all the weather data (temperatures, weather, precipitation, etc).
	protected boolean parseOpenWeather(String responseBody) {
		JSONParser parser = new JSONParser();

		try {
			JSONObject data = (JSONObject) parser.parse(responseBody);   // - Navigate through JSON data
			JSONObject current = (JSONObject) data.get("current");
			JSONArray currentWeather = (JSONArray) current.get("weather");
			JSONObject currentWeatherObject = (JSONObject) currentWeather.get(0);

			currentWeatherDescription = capitalizeEveryWord(currentWeatherObject.get("description").toString());   // - Get currentWeatherDescription and capitalize every word.
			currentTemp = current.get("temp").toString();   // - Get currentTemp.
			Double currentTempDouble = Double.parseDouble(currentTemp);
			int currentTempInt = currentTempDouble.intValue();
			currentTemp = " " + currentTempInt;
			String unix = current.get("dt").toString();   // - Get currentTime by converting unix into time of day
			String timeZoneOffset = data.get("timezone_offset").toString();
			LocalDateTime currentDateTime = unixToLocalDateTime(unix, timeZoneOffset);
			DateTimeFormatter currentFormatter = DateTimeFormatter.ofPattern("hh:mm a");   // - Formatter
			currentTime = currentDateTime.format(currentFormatter);

			JSONArray hourly = (JSONArray) data.get("hourly");   // - Hourly data
			for (int i = 0, n = hourlyHours.length; i < n; i++) {
				JSONObject hourObject = (JSONObject) hourly.get(i);   // - Get each hour from 0 to 11.

				String hourlyUnix = hourObject.get("dt").toString();   // - Get the time for the hour, format it, and store it in hourlyHours.
				LocalDateTime hourlyDateTime = unixToLocalDateTime(hourlyUnix, timeZoneOffset);
				DateTimeFormatter hourlyFormatter = DateTimeFormatter.ofPattern("ha");
				String hourlyHour = hourlyDateTime.format(hourlyFormatter);
				hourlyHour = hourlyHour.replace("AM", "am").replace("PM", "pm");
				hourlyHours[i] = hourlyHour;

				JSONArray weather = (JSONArray) hourObject.get("weather");   // - Get the weather description for that hour and store it in hourlyWeatherDescriptions.
				JSONObject weatherObject = (JSONObject) weather.get(0);
				String dailyIcon = weatherObject.get("icon").toString();
				hourlyIcons[i] = dailyIcon;

				String dailyTemp = hourObject.get("temp").toString();   // - Get the temperature for that hour and store it in hourlyTemperatures.
				Double dailyTempDouble = Double.parseDouble(dailyTemp);
				int dailyTempInt = dailyTempDouble.intValue();   // - Round to an int.
				dailyTemp = dailyTempInt + "";
				hourlyTemperatures[i] = dailyTemp;
			}

			JSONArray daily = (JSONArray) data.get("daily");   // - Daily data
			for (int i = 1, n = dailyDays.length + 1; i < n; i++) {
				JSONObject dayObject = (JSONObject) daily.get(i);   // - Get the next 7 days.

				String dailyUnix = dayObject.get("dt").toString();   // - Get the date for the day, format it, and store it in dailyDays.
				LocalDateTime dailyDateTime = unixToLocalDateTime(dailyUnix, timeZoneOffset);
				DateTimeFormatter dailyFormatter = DateTimeFormatter.ofPattern("EEEE");
				String dailyDay = dailyDateTime.format(dailyFormatter);
				dailyDays[i-1] = dailyDay;

				JSONArray weather= (JSONArray) dayObject.get("weather");
				JSONObject weatherObject = (JSONObject) weather.get(0);
				String dailyIcon = weatherObject.get("icon").toString();
				dailyIcons[i-1] = dailyIcon;

				JSONObject temp = (JSONObject) dayObject.get("temp");
				String dailyHigh = temp.get("max").toString();
				Double dailyHighDouble = Double.parseDouble(dailyHigh);
				int dailyHighInt = dailyHighDouble.intValue();   // - Round to an int.
				dailyHigh = dailyHighInt + "";
				dailyHighs[i-1] = dailyHigh;

				String dailyLow = temp.get("min").toString();
				Double dailyLowDouble = Double.parseDouble(dailyLow);
				int dailyLowInt = dailyLowDouble.intValue();   // - Round to an int.
				dailyLow = dailyLowInt + "";
				dailyLows[i-1] = dailyLow;
			}
			return true;
		} catch (ParseException e) {
			return false;
		}
	}


	// - Capitalize every word in a string.
	protected static String capitalizeEveryWord(String input) {
		String ret = "";   // - Loop through input; capitalize every first letter; add the letters to ret.

		for (int i = 0, n = input.length(); i < n; i++) {
			char c = input.charAt(i);
			if (i == 0) {   // - Capitalize the first letter.
				ret += Character.toUpperCase(c);
			} else if (c == ' ') {   // - Capitalize the first letter after a space.
				ret += c;
				ret += Character.toUpperCase(input.charAt(i + 1));
				i++;
			} else {
				ret += c;
			}
		}
		return ret;
	}


	// - Unix to localDateTime.
	protected LocalDateTime unixToLocalDateTime(String unix, String offset) {
		Long unixLong = Long.parseLong(unix);
		Long timezoneOffsetLong = Long.parseLong(offset);
		unixLong += timezoneOffsetLong;   // - Use offset to convert into local time.
		Instant instant = Instant.ofEpochSecond(unixLong);   // - Convert unix into instant.
		LocalDateTime dateTime = LocalDateTime.ofInstant(instant,ZoneOffset.UTC);   // - Convert instant into localDateTime
		return dateTime;
	}


	// - Connect to a given URL and request data
	protected StringBuffer connectAndRequest(String urlString) {
		BufferedReader reader;
		String inputLine;
		StringBuffer response = new StringBuffer();

		try {
			URL url = new URL(urlString);
			connection = (HttpURLConnection) url.openConnection();   // - Connect to the URL.
			connection.setRequestMethod("GET");
			connection.setConnectTimeout(5000);
			connection.setReadTimeout(5000);
			int responseCode = connection.getResponseCode();
			if (responseCode > 299) {
				reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));   // - If the connection/request failed, store the error into response.
				while ((inputLine = reader.readLine()) != null) {
					response.append(inputLine);
				}
				reader.close();
			} else {
				reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));   // - If the connection/request was successful, store the data into response.
				while ((inputLine = reader.readLine()) != null) {
					response.append(inputLine);
				}
				reader.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			connection.disconnect();
		}
		return response;
	}


	public static void main(String[] args) {
	}
}
