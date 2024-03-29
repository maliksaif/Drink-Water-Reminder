package com.sudoware.aqua.reminder.utils

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter

class ChartXValueFormatter(private val dateArray: ArrayList<String>) : ValueFormatter() {
    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        return dateArray.getOrNull(value.toInt()) ?: ""
    }
}