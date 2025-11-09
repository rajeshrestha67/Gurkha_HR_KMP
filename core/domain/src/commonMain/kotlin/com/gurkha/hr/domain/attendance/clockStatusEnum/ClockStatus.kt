package com.gurkha.hr.domain.attendance.clockStatusEnum

enum class ClockStatus(val value: String){
    CLOCK_IN("clock In"),
    CLOCK_OUT("clock Out");

    companion object{
        val list : List<ClockStatus>
            get() = entries.toList().map { it }
    }

}