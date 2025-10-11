package com.gurkha.hr.components.prompts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.gurkha.hr.components.ERPButton
import com.gurkha.hr.res.SharedRes
import com.gurkha.hr.res.theme.dimens
import com.gurkha.hr.res.theme.primaryTextColor
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PromptModalBottomSheet(
    text : String,
    promptType: PromptType = PromptType.SUCCESS,
    buttonText: StringResource = SharedRes.Strings.ok,
    onBackClicked: () -> Unit
){
    val sheet = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { newValue ->
            newValue != SheetValue.Hidden
        }
    )

    ModalBottomSheet(
        sheetState = sheet,
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding(),
        onDismissRequest = {
        },
        containerColor = MaterialTheme.colorScheme.background,
    ){
        Column(
            modifier = Modifier.fillMaxWidth().padding(
                MaterialTheme.dimens.small3
            ),
            verticalArrangement = Arrangement.spacedBy(
                MaterialTheme.dimens.small2,
                alignment = Alignment.CenterVertically
            ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val bgColor = when(promptType){
                PromptType.SUCCESS -> {
                    MaterialTheme.colorScheme.primary
                }
                PromptType.FAILED -> {
                    MaterialTheme.colorScheme.error
                }
            }
            Box(
                modifier = Modifier
                    .size(MaterialTheme.dimens.promptDialogSize)
                    .aspectRatio(1f)
                    .background(
                        color = bgColor.copy(alpha = 0.5f),
                        shape = CircleShape
                    )
                    .padding(MaterialTheme.dimens.medium1)
                    .background(
                        color = bgColor,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
               when(promptType){
                   PromptType.SUCCESS -> {
                       Icon(
                           imageVector =  Icons.Filled.Check,
                           contentDescription = "Success",
                           tint = MaterialTheme.colorScheme.onPrimary,
                           modifier = Modifier.size(MaterialTheme.dimens.medium3)
                       )
                   }

                   PromptType.FAILED -> {
                       Icon(
                           imageVector =  Icons.Filled.Close,
                           contentDescription = "Error",
                           tint = MaterialTheme.colorScheme.onError,
                           modifier = Modifier.size(MaterialTheme.dimens.medium3)
                       )
                   }
               }
            }
            Text(
                modifier = Modifier.padding(top = MaterialTheme.dimens.small2),
                text = text,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.primaryTextColor
                ),
                textAlign = TextAlign.Center
            )

            ERPButton(
                modifier = Modifier.fillMaxWidth().padding(top = MaterialTheme.dimens.medium1),
                text = stringResource(buttonText),
                onClick = onBackClicked
            )

        }
    }
}

enum class PromptType {
    SUCCESS,
    FAILED
}