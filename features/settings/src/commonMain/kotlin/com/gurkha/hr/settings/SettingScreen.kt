package com.gurkha.hr.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gurkha.hr.components.ColumnItemRow
import com.gurkha.hr.components.biometric.rememberBiometricPromptLauncher
import com.gurkha.hr.components.dimens
import com.gurkha.hr.components.erpColors
import com.gurkha.hr.res.SharedRes
import com.gurkha.hr.res.theme.EPRLanguage
import com.gurkha.hr.res.theme.ThemeMode
import com.gurkha.hr.settings.model.settings.SettingList
import com.gurkha.hr.settings.model.settings.SettingsScreenAction
import com.gurkha.hr.settings.model.settings.SettingsScreenState
import com.gurkha.model.biometric.BiometricAuthResult
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun SettingScreen(
    onBackPressed: () -> Unit,
    navigateToChangePassword: () -> Unit
) {
    val settingsViewModel = koinViewModel<SettingsViewModel>()

    val state by settingsViewModel.state.collectAsStateWithLifecycle()

    SettingScreenContainer(
        onBackPressed = onBackPressed,
        state = state,
        navigateToChangePassword = navigateToChangePassword,
        onAction = settingsViewModel::onAction
    )


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreenContainer(
    onBackPressed: () -> Unit,
    navigateToChangePassword: () -> Unit,
    state: SettingsScreenState,
    onAction: (SettingsScreenAction) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(),
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(),
                title = { Text(stringResource(SharedRes.Strings.setting)) },
                navigationIcon = {
                    IconButton(
                        onClick = onBackPressed
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }

            )
        }
    ) { paddingValues ->
        SettingScreenContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            navigateToChangePassword = navigateToChangePassword,
            state = state,
            onAction = onAction
        )
    }
}

@Composable
fun SettingScreenContent(
    modifier: Modifier = Modifier,
    navigateToChangePassword: () -> Unit,
    state: SettingsScreenState,
    onAction: (SettingsScreenAction) -> Unit
) {
    val launcher = rememberBiometricPromptLauncher(
        onResult = { result ->
            val authStatus = when (result) {
                BiometricAuthResult.Success -> {
                    onAction(SettingsScreenAction.OnBiometricStatusChange(true))
                }

                is BiometricAuthResult.Error, is BiometricAuthResult.Failure, is BiometricAuthResult.NotAvailable -> {
                    onAction(SettingsScreenAction.OnBiometricStatusChange(false))
                }
            }
        }
    )
    val title = stringResource(SharedRes.Strings.login_verification)
    val subTitle = stringResource(SharedRes.Strings.auth_using_biometric)
    val negativeText = stringResource(SharedRes.Strings.cancel)

    LaunchedEffect(Unit) {
        onAction(SettingsScreenAction.OnIsAvailableCheck(launcher.isAvailable))
    }

    var showThemeBottomSheet by remember { mutableStateOf(false) }
    var showLanguageBottomSheet by remember { mutableStateOf(false) }

    Box(
        modifier = modifier,
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                horizontal = MaterialTheme.dimens.small3,
                vertical = MaterialTheme.dimens.small1
            )
        ) {
            settingListItems(state.items) { item ->
                ColumnItemRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            when (item) {
                                SettingList.ChangePassword -> {
                                    navigateToChangePassword()
                                }

                                SettingList.AppAppearance -> {
                                    showThemeBottomSheet = true
                                }

                                SettingList.Language -> {
                                    showLanguageBottomSheet = true
                                }

                                SettingList.Biometric -> {

                                }
                            }
                        }
                        .padding(vertical = if (item != SettingList.Biometric) MaterialTheme.dimens.small3 else MaterialTheme.dimens.small1),
                    text = stringResource(item.title),
                    endIndicator = {
                        if (item == SettingList.Biometric) {
                            Switch(
                                checked = state.biometricEnabled,
                                onCheckedChange = { isEnable ->
                                    if (isEnable) {
                                        launcher.launch(
                                            title,
                                            subTitle,
                                            negativeText
                                        )
                                    } else {
                                        onAction(
                                            SettingsScreenAction.OnBiometricStatusChange(
                                                isEnable
                                            )
                                        )
                                    }
                                }
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Filled.ChevronRight,
                                contentDescription = "Arrow Right"
                            )
                        }

                    }
                )
            }
        }
        ThemeBottomSheet(
            showThemeBottomSheet = showThemeBottomSheet,
            themes = state.themes,
            onThemeSelected = {
                showThemeBottomSheet = false
                onAction(SettingsScreenAction.OnThemeSelected(it))
            },
            onDismiss = { showThemeBottomSheet = false }
        )
        LanguageBottomSheet(
            showLanguageBottomSheet = showLanguageBottomSheet,
            languages = state.languages,
            onLanguageSelected = {
                onAction(SettingsScreenAction.OnLanguageSelected(it))
                showLanguageBottomSheet = false
            },
            onDismiss = {
                showLanguageBottomSheet = false
            }
        )
    }
}


private fun LazyListScope.settingListItems(
    list: List<SettingList>,
    itemContent: @Composable LazyItemScope.(item: SettingList) -> Unit
) {
    items(
        items = list, key = { it.title.key },
        itemContent = itemContent
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageBottomSheet(
    showLanguageBottomSheet: Boolean,
    languages: List<EPRLanguage>,
    onDismiss: () -> Unit,
    onLanguageSelected: (EPRLanguage) -> Unit
) {

    if (!showLanguageBottomSheet) {
        return
    }
    ModalBottomSheet(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                modifier = Modifier.padding(
                    vertical = MaterialTheme.dimens.small2,
                    horizontal = MaterialTheme.dimens.small3
                ),
                text = stringResource(SharedRes.Strings.language),
                style = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.primary
                )
            )
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(
                    horizontal = MaterialTheme.dimens.small3
                )
            ) {

                items(items = languages, key = { it.langCode }) { theme ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onLanguageSelected(theme)
                            }

                    ) {
                        Text(
                            modifier = Modifier.padding(vertical = MaterialTheme.dimens.small2),
                            text = stringResource(theme.displayName),
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = MaterialTheme.erpColors.primaryTextColor
                            )
                        )
                        HorizontalDivider(
                            modifier = Modifier.fillMaxWidth(),
                            thickness = 0.5.dp,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }

                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeBottomSheet(
    showThemeBottomSheet: Boolean,
    themes: List<ThemeMode>,
    onDismiss: () -> Unit,
    onThemeSelected: (ThemeMode) -> Unit
) {

    if (!showThemeBottomSheet) {
        return
    }
    ModalBottomSheet(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                modifier = Modifier.padding(
                    vertical = MaterialTheme.dimens.small2,
                    horizontal = MaterialTheme.dimens.small3
                ),
                text = stringResource(SharedRes.Strings.appAppearance),
                style = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.primary
                )
            )
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(
                    horizontal = MaterialTheme.dimens.small3
                )
            ) {

                items(items = themes, key = { it.value }) { theme ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onThemeSelected(theme)
                            }

                    ) {
                        Text(
                            modifier = Modifier.padding(vertical = MaterialTheme.dimens.small2),
                            text = stringResource(theme.title),
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = MaterialTheme.erpColors.primaryTextColor
                            )
                        )
                        HorizontalDivider(
                            modifier = Modifier.fillMaxWidth(),
                            thickness = 0.5.dp,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }

                }
            }
        }
    }
}

