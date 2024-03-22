package com.samkt.weathersavy.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun String.getWeatherIcon(): String {
    return "https://openweathermap.org/img/wn/$this.png"
}

fun getTodayDate(): String {
    val format = SimpleDateFormat("MMMM dd", Locale.ENGLISH)
    val date = Date()
    return format.format(date)
}

fun getCurrentFormattedDate(): String {
    val currentTime = System.currentTimeMillis()
    val dateFormat = SimpleDateFormat("M/d/yyyy h:mm a", Locale.US)
    return dateFormat.format(currentTime)
}

@Suppress("ktlint:standard:max-line-length")
fun String.getBackgroundImage(): String {
    return when (this) {
        "Thunderstorm" -> "https://img.freepik.com/free-vector/realistic-vector-background-rainy-clouds-with-lighting-night-dark-sky_528282-219.jpg"
        "Drizzle" -> "https://img.freepik.com/free-photo/rain-effect-nature-background_23-2148099038.jpg?t=st=1709795660~exp=1709799260~hmac=a7093347485b84bbc3841f0c6378852a99c2232589d7281cd655df8884c21324&w=740"
        "Rain" -> "https://www.treehugger.com/thmb/D8p2KPlXorif726DelPJjmhjetw=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/__opt__aboutcom__coeus__resources__content_migration__mnn__images__2017__03__raindrops-plants-smell-57c4545c1bb844c0a2cd7e0252cf6d2f.jpg"
        "Snow" -> "https://img.freepik.com/free-photo/wide-shot-road-fully-covered-by-snow-with-pine-trees-both-sides-car-traces_181624-3616.jpg?t=st=1709795831~exp=1709799431~hmac=1b444aa7fb3e76f75bff5174dfea17bff5108286ff3b8f9ee848ecd15490ca93&w=826"
        "Mist" -> "https://img.freepik.com/free-photo/vertical-shot-smoke-covering-mountain-medvednica-zagreb-croatia_181624-19065.jpg?t=st=1709795874~exp=1709799474~hmac=71a873247c3476102f92a65f5fa91b32efc1e015b19f2febf3bca88c99b5205a&w=360"
        "Smoke" -> "https://img.freepik.com/free-photo/air-polluted-city-monotone-landscape-photography_53876-104831.jpg?t=st=1709795914~exp=1709799514~hmac=8f665ee65156453190b3308b6ad0467b1503ab7be5a7363f9d082ec3ae041bd5&w=740"
        "Haze" -> "https://img.freepik.com/free-photo/green-grass-field-with-trees-fog_395237-21.jpg?t=st=1709795948~exp=1709799548~hmac=6c699dcb4a5c0866ab3f0004a0c7a1ceea7ae441415f8eb68343f0ea7d22e75e&w=740"
        "Dust" -> "https://img.freepik.com/free-photo/namibian-sand-storm_181624-8404.jpg?t=st=1709796107~exp=1709799707~hmac=340c85a8abbb3611f47658a8b70f43b021637d251959d6f62a8180b0f03a5533&w=740"
        "Fog" -> "https://img.freepik.com/free-photo/wooden-bridge-park-covered-by-dense-fog_181624-17891.jpg?t=st=1709796177~exp=1709799777~hmac=dbd5129be08032edfb7ee54398ac5fcec6372734db2b07e00104e4cb1f32415a&w=740"
        "Sand" -> "https://img.freepik.com/free-photo/beautiful-sunset-beach-creating-perfect-scenery-evening-walks-shore_181624-10237.jpg?t=st=1709796214~exp=1709799814~hmac=6d8d7a0d6f95c7d8b9670d6e66cefcd1da6c5ce8eabbb246aabe122f6a2a8395&w=740"
        "Ash" -> "https://img.freepik.com/free-photo/shaft-light-penetrating-through-dark-sky-cityscape-mountain_23-2148182896.jpg?t=st=1709796265~exp=1709799865~hmac=d02fd6a07a1e78cf13f696f41819ff7630d0517d7b08b81fe7eb4efbc82e2076&w=740"
        "Squall" -> "https://img.freepik.com/free-photo/weather-effects-collage-concept_23-2150062081.jpg?t=st=1709796305~exp=1709799905~hmac=e4dde99c68fbaf40f0f5dd79bb3294fcc04a468e5794340d866fe4f63867484a&w=740"
        "Clear" -> "https://coatingspromag.com/uploads/images/2016/05/ThinkstockPhotos-482458470.gif"
        "Clouds" -> "https://nature-mentor.com/wp-content/uploads/2017/11/cumulus-active-growth.jpg"
        "Tornado" -> "https://img.freepik.com/free-photo/weather-effects-composition_23-2149853297.jpg?t=st=1709796444~exp=1709800044~hmac=a51d9e1a449a93d0ccbdebc87998bee30fd99fa013cc5e6dc4abbaa49ac029bc&w=360"
        else -> "https://t4.ftcdn.net/jpg/01/31/15/09/360_F_131150985_qbDIQNH9gjOXFQx5RHrFozFtDoFY0Z1g.jpg"
    }
}
