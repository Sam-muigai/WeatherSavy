package com.samkt.weathersavy.features.weather.data

import com.samkt.weathersavy.core.network.dtos.CloudsDto
import com.samkt.weathersavy.core.network.dtos.CoordDto
import com.samkt.weathersavy.core.network.dtos.CurrentWeatherResponseDto
import com.samkt.weathersavy.core.network.dtos.MainDto
import com.samkt.weathersavy.core.network.dtos.RainDto
import com.samkt.weathersavy.core.network.dtos.SysDto
import com.samkt.weathersavy.core.network.dtos.WeatherDto
import com.samkt.weathersavy.core.network.dtos.WindDto
import com.samkt.weathersavy.features.weather.data.model.Clouds
import com.samkt.weathersavy.features.weather.data.model.Coord
import com.samkt.weathersavy.features.weather.data.model.CurrentWeather
import com.samkt.weathersavy.features.weather.data.model.Main
import com.samkt.weathersavy.features.weather.data.model.Rain
import com.samkt.weathersavy.features.weather.data.model.Sys
import com.samkt.weathersavy.features.weather.data.model.Weather
import com.samkt.weathersavy.features.weather.data.model.Wind

fun CloudsDto.toClouds(): Clouds {
    return Clouds(all)
}

fun CoordDto.toCoord(): Coord {
    return Coord(lat, lon)
}

fun MainDto.toMain(): Main {
    return Main(feelsLike, grndLevel, humidity, pressure, seaLevel, temp, tempMax, tempMin)
}

fun RainDto.toRain(): Rain {
    return Rain(h)
}

fun SysDto.toSys(): Sys {
    return Sys(country, sunrise, sunset)
}

fun WeatherDto.toWeather(): Weather {
    return Weather(description, icon, id, main)
}

fun WindDto.toWind(): Wind {
    return Wind(deg, gust, speed)
}

fun CurrentWeatherResponseDto.toCurrentWeather(): CurrentWeather {
    return CurrentWeather(
        base = base,
        clouds = cloudsDto?.toClouds(),
        cod = cod,
        coord = coordDto?.toCoord(),
        dt = dt,
        id = id,
        main = mainDto?.toMain(),
        name = name,
        rain = rainDto?.toRain(),
        sys = sysDto?.toSys(),
        timezone = timezone,
        visibility = visibility,
        weather = weatherDto.map { it.toWeather() },
        wind = windDto?.toWind(),
    )
}
