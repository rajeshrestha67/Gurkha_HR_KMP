package com.gurkha.hr.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import com.gurkha.hr.ui.SharedRes
import org.jetbrains.compose.resources.Font

val poppinsFamily
    @Composable get() = FontFamily(
        Font(
            resource = SharedRes.Fonts.poppinsMedium,
        ),
        Font(
            resource = SharedRes.Fonts.poppinsBold,
        ),
        Font(
            resource = SharedRes.Fonts.poppinsItalic,
        )
    )
val baseline = Typography()

val AppTypography: Typography
    @Composable get() = Typography(
        displayLarge = baseline.displayLarge.copy(fontFamily = poppinsFamily),
        displayMedium = baseline.displayMedium.copy(fontFamily = poppinsFamily),
        displaySmall = baseline.displaySmall.copy(fontFamily = poppinsFamily),
        headlineLarge = baseline.headlineLarge.copy(fontFamily = poppinsFamily),
        headlineMedium = baseline.headlineMedium.copy(fontFamily = poppinsFamily),
        headlineSmall = baseline.headlineSmall.copy(fontFamily = poppinsFamily),
        titleLarge = baseline.titleLarge.copy(fontFamily = poppinsFamily),
        titleMedium = baseline.titleMedium.copy(fontFamily = poppinsFamily),
        titleSmall = baseline.titleSmall.copy(fontFamily = poppinsFamily),
        bodyLarge = baseline.bodyLarge.copy(fontFamily = poppinsFamily),
        bodyMedium = baseline.bodyMedium.copy(fontFamily = poppinsFamily),
        bodySmall = baseline.bodySmall.copy(fontFamily = poppinsFamily),
        labelLarge = baseline.labelLarge.copy(fontFamily = poppinsFamily),
        labelMedium = baseline.labelMedium.copy(fontFamily = poppinsFamily),
        labelSmall = baseline.labelSmall.copy(fontFamily = poppinsFamily),
    )