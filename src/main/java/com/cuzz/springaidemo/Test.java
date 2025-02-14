package com.cuzz.springaidemo;

import java.util.HashMap;
import java.util.Map;

public class Test {


        public Map<String, String> getWeather(String location) {
            // 模拟天气数据
            Map<String, String> weatherData = new HashMap<>();
            weatherData.put("location", location);
            weatherData.put("temperature", "25");
            weatherData.put("condition", "大晴天呀");
            return weatherData;
        }

}
