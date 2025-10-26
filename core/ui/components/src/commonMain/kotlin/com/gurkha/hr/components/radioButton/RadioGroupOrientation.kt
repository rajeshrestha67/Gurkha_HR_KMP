package com.gurkha.hr.components.radioButton

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment

sealed interface RadioGroupOrientation {
    data class Vertical(
        val arrangement: Arrangement.Vertical = Arrangement.Top,
        val alignment: Alignment.Horizontal = Alignment.Start
    ) : RadioGroupOrientation

    data class Horizontal(
        val arrangement: Arrangement.Horizontal = Arrangement.Start,
        val alignment: Alignment.Vertical = Alignment.Top
    ) : RadioGroupOrientation
    
}