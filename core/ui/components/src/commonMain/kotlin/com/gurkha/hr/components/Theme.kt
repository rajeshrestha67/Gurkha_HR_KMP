package com.gurkha.hr.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import com.gurkha.hr.components.locale.LocalAppLocale
import com.gurkha.hr.components.locale.erpAppLocale
import com.gurkha.hr.res.theme.AppTypography
import com.gurkha.hr.res.theme.CompactDimens
import com.gurkha.hr.res.theme.ThemeMode
import com.gurkha.hr.res.theme.backgroundDark
import com.gurkha.hr.res.theme.backgroundLight
import com.gurkha.hr.res.theme.errorContainerDark
import com.gurkha.hr.res.theme.errorContainerLight
import com.gurkha.hr.res.theme.errorDark
import com.gurkha.hr.res.theme.errorLight
import com.gurkha.hr.res.theme.inverseOnSurfaceDark
import com.gurkha.hr.res.theme.inverseOnSurfaceLight
import com.gurkha.hr.res.theme.inversePrimaryDark
import com.gurkha.hr.res.theme.inversePrimaryLight
import com.gurkha.hr.res.theme.inverseSurfaceDark
import com.gurkha.hr.res.theme.inverseSurfaceLight
import com.gurkha.hr.res.theme.onBackgroundDark
import com.gurkha.hr.res.theme.onBackgroundLight
import com.gurkha.hr.res.theme.onErrorContainerDark
import com.gurkha.hr.res.theme.onErrorContainerLight
import com.gurkha.hr.res.theme.onErrorDark
import com.gurkha.hr.res.theme.onErrorLight
import com.gurkha.hr.res.theme.onPrimaryContainerDark
import com.gurkha.hr.res.theme.onPrimaryContainerLight
import com.gurkha.hr.res.theme.onPrimaryDark
import com.gurkha.hr.res.theme.onPrimaryLight
import com.gurkha.hr.res.theme.onSecondaryContainerDark
import com.gurkha.hr.res.theme.onSecondaryContainerLight
import com.gurkha.hr.res.theme.onSecondaryDark
import com.gurkha.hr.res.theme.onSecondaryLight
import com.gurkha.hr.res.theme.onSurfaceDark
import com.gurkha.hr.res.theme.onSurfaceLight
import com.gurkha.hr.res.theme.onSurfaceVariantDark
import com.gurkha.hr.res.theme.onSurfaceVariantLight
import com.gurkha.hr.res.theme.onTertiaryContainerDark
import com.gurkha.hr.res.theme.onTertiaryContainerLight
import com.gurkha.hr.res.theme.onTertiaryDark
import com.gurkha.hr.res.theme.onTertiaryLight
import com.gurkha.hr.res.theme.outlineDark
import com.gurkha.hr.res.theme.outlineLight
import com.gurkha.hr.res.theme.outlineVariantDark
import com.gurkha.hr.res.theme.outlineVariantLight
import com.gurkha.hr.res.theme.primaryContainerDark
import com.gurkha.hr.res.theme.primaryContainerLight
import com.gurkha.hr.res.theme.primaryDark
import com.gurkha.hr.res.theme.primaryLight
import com.gurkha.hr.res.theme.scrimDark
import com.gurkha.hr.res.theme.scrimLight
import com.gurkha.hr.res.theme.secondaryContainerDark
import com.gurkha.hr.res.theme.secondaryContainerLight
import com.gurkha.hr.res.theme.secondaryDark
import com.gurkha.hr.res.theme.secondaryLight
import com.gurkha.hr.res.theme.surfaceBrightDark
import com.gurkha.hr.res.theme.surfaceBrightLight
import com.gurkha.hr.res.theme.surfaceContainerDark
import com.gurkha.hr.res.theme.surfaceContainerHighDark
import com.gurkha.hr.res.theme.surfaceContainerHighLight
import com.gurkha.hr.res.theme.surfaceContainerHighestDark
import com.gurkha.hr.res.theme.surfaceContainerHighestLight
import com.gurkha.hr.res.theme.surfaceContainerLight
import com.gurkha.hr.res.theme.surfaceContainerLowDark
import com.gurkha.hr.res.theme.surfaceContainerLowLight
import com.gurkha.hr.res.theme.surfaceContainerLowestDark
import com.gurkha.hr.res.theme.surfaceContainerLowestLight
import com.gurkha.hr.res.theme.surfaceDark
import com.gurkha.hr.res.theme.surfaceDimDark
import com.gurkha.hr.res.theme.surfaceDimLight
import com.gurkha.hr.res.theme.surfaceLight
import com.gurkha.hr.res.theme.surfaceVariantDark
import com.gurkha.hr.res.theme.surfaceVariantLight
import com.gurkha.hr.res.theme.tertiaryContainerDark
import com.gurkha.hr.res.theme.tertiaryContainerLight
import com.gurkha.hr.res.theme.tertiaryDark
import com.gurkha.hr.res.theme.tertiaryLight


private val lightScheme = lightColorScheme(
    primary = primaryLight,
    onPrimary = onPrimaryLight,
    primaryContainer = primaryContainerLight,
    onPrimaryContainer = onPrimaryContainerLight,
    secondary = secondaryLight,
    onSecondary = onSecondaryLight,
    secondaryContainer = secondaryContainerLight,
    onSecondaryContainer = onSecondaryContainerLight,
    tertiary = tertiaryLight,
    onTertiary = onTertiaryLight,
    tertiaryContainer = tertiaryContainerLight,
    onTertiaryContainer = onTertiaryContainerLight,
    error = errorLight,
    onError = onErrorLight,
    errorContainer = errorContainerLight,
    onErrorContainer = onErrorContainerLight,
    background = backgroundLight,
    onBackground = onBackgroundLight,
    surface = surfaceLight,
    onSurface = onSurfaceLight,
    surfaceVariant = surfaceVariantLight,
    onSurfaceVariant = onSurfaceVariantLight,
    outline = outlineLight,
    outlineVariant = outlineVariantLight,
    scrim = scrimLight,
    inverseSurface = inverseSurfaceLight,
    inverseOnSurface = inverseOnSurfaceLight,
    inversePrimary = inversePrimaryLight,
    surfaceDim = surfaceDimLight,
    surfaceBright = surfaceBrightLight,
    surfaceContainerLowest = surfaceContainerLowestLight,
    surfaceContainerLow = surfaceContainerLowLight,
    surfaceContainer = surfaceContainerLight,
    surfaceContainerHigh = surfaceContainerHighLight,
    surfaceContainerHighest = surfaceContainerHighestLight,
)

private val darkScheme = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,
    secondary = secondaryDark,
    onSecondary = onSecondaryDark,
    secondaryContainer = secondaryContainerDark,
    onSecondaryContainer = onSecondaryContainerDark,
    tertiary = tertiaryDark,
    onTertiary = onTertiaryDark,
    tertiaryContainer = tertiaryContainerDark,
    onTertiaryContainer = onTertiaryContainerDark,
    error = errorDark,
    onError = onErrorDark,
    errorContainer = errorContainerDark,
    onErrorContainer = onErrorContainerDark,
    background = backgroundDark,
    onBackground = onBackgroundDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    surfaceVariant = surfaceVariantDark,
    onSurfaceVariant = onSurfaceVariantDark,
    outline = outlineDark,
    outlineVariant = outlineVariantDark,
    scrim = scrimDark,
    inverseSurface = inverseSurfaceDark,
    inverseOnSurface = inverseOnSurfaceDark,
    inversePrimary = inversePrimaryDark,
    surfaceDim = surfaceDimDark,
    surfaceBright = surfaceBrightDark,
    surfaceContainerLowest = surfaceContainerLowestDark,
    surfaceContainerLow = surfaceContainerLowDark,
    surfaceContainer = surfaceContainerDark,
    surfaceContainerHigh = surfaceContainerHighDark,
    surfaceContainerHighest = surfaceContainerHighestDark,

    )

@Composable
fun AppTheme(
    selectedThemeMode: ThemeMode = ThemeMode.SYSTEM,
    content: @Composable() () -> Unit
) {
    val systemInDarkTheme = isSystemInDarkTheme()

    val darkTheme = when (selectedThemeMode) {
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.SYSTEM -> systemInDarkTheme
    }

    val colorScheme = if (darkTheme) darkScheme else lightScheme
    println("erpAppLocale $erpAppLocale,selectedThemeMode $selectedThemeMode")
    ChangeStatusBarColor(
        darkIcons = !darkTheme
    )

    CompositionLocalProvider(
        LocalAppDimens provides CompactDimens,
        LocalAppLocale provides erpAppLocale,
    ) {

        MaterialTheme(
            colorScheme = colorScheme,
            typography = AppTypography,
            content = content
        )
    }
}

val MaterialTheme.dimens
    @Composable get() = LocalAppDimens.current

val LocalAppDimens = compositionLocalOf {
    CompactDimens
}