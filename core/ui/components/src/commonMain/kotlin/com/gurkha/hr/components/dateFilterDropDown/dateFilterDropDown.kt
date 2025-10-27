package com.gurkha.hr.components.dateFilterDropDown

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.gurkha.hr.components.ERPButton
import com.gurkha.hr.components.dimens
import com.gurkha.hr.components.textField.DropDownText
import com.gurkha.hr.components.textField.FormValidate
import com.gurkha.hr.date.BSPointer
import com.gurkha.hr.res.SharedRes
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringArrayResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun DateFilterDropdown(
    modifier: Modifier = Modifier,
    selectedMonth: String,
    selectedYear: String,
    monthError: StringResource? = null,
    yearError: StringResource? = null,
    onMonthSelected: (monthName: String, monthIndex: Int) -> Unit,
    onYearSelected: (year: Int) -> Unit,
    onMonthError: (StringResource?) -> Unit,
    onYearError: (StringResource?) -> Unit,
    yearInBS: List<String> = remember { (2070..BSPointer.getLastDay().first).map { it.toString() } },
    months: List<String> = stringArrayResource(SharedRes.Arrays.months),
    onSubmit: () -> Unit
) {

    Box(
        modifier = modifier

    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3)
        ) {
            // Month Dropdown
            DropDownText(
                dropdownIcon = {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = "Calendar Icon"
                    )
                },
                label = SharedRes.Strings.month,
                hint = stringResource(SharedRes.Strings.month),
                selectedValue = selectedMonth,
                error = monthError,
                onError = { message ->
                    onMonthError(message)
                },
                listOfItems = months,
                rules = FormValidate.requiredValidationRules,
                itemClicked = { month ->
                    onMonthSelected(month, months.indexOf(month) + 1)
                }
            )

            // Year Dropdown
            DropDownText(
                dropdownIcon = {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = "Calendar Icon"
                    )
                },
                label = SharedRes.Strings.year,
                hint = stringResource(SharedRes.Strings.year),
                selectedValue = selectedYear,
                error = yearError,
                onError = { message ->
                    onYearError(message)
                },
                listOfItems = yearInBS,
                rules = FormValidate.requiredValidationRules,
                itemClicked = { year ->
                    onYearSelected(year.toInt())
                }
            )

            // Submit Button
            ERPButton(
                onClick = onSubmit,
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(SharedRes.Strings.submit)
            )
        }
    }
}
