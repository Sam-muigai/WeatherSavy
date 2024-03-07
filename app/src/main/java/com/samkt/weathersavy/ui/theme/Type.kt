package com.samkt.weathersavy.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.samkt.weathersavy.R

val poppins =
    FontFamily(
        Font(R.font.poppins_light, FontWeight.Light),
        Font(R.font.poppins_regular, FontWeight.Normal),
        Font(R.font.poppins_medium, FontWeight.Medium),
        Font(R.font.poppins_semibold, FontWeight.SemiBold),
        Font(R.font.poppins_bold, FontWeight.Bold),
    )

val Typography =
    Typography(
        labelLarge =
            TextStyle(
                fontFamily = poppins,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                lineHeight = 16.sp,
            ),
        bodyLarge =
            TextStyle(
                fontFamily = poppins,
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp,
                lineHeight = 16.sp,
            ),
        titleLarge =
            TextStyle(
                fontFamily = poppins,
                fontWeight = FontWeight.Bold,
                fontSize = 40.sp,
                lineHeight = 47.sp,
            ),
        displayLarge =
            TextStyle(
                fontFamily = poppins,
                fontWeight = FontWeight.Medium,
                fontSize = 86.sp,
                lineHeight = 100.sp,
            ),
    )
