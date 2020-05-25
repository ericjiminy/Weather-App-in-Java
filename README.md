# Weather-App-in-Java
Weather application made in Java. Uses MapQuest API and OpenWeather API.

The GUI takes in a city and state/country and uses the MapQuest API to get the coordinates of the location.
The coordinates are used in the OpenWeather API to get weather data for the location.

The GUI displays current temperature and weather, local time, hourly temperature and weather for the next 12 hours,
and daily temperature and weather for the next 6 days. The background color changes with the current weather, becoming darker
as the weather gets worse.

There are issues with searching for international locations, but most cities in the US are reachable.
