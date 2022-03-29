package ru.kvait.mpandroidcharttest

import android.R
import android.util.TypedValue
import androidx.annotation.ColorInt
import com.github.mikephil.charting.charts.BarLineChartBase
import com.github.mikephil.charting.data.BarLineScatterCandleBubbleData
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.interfaces.datasets.IBarLineScatterCandleBubbleDataSet

fun <T> BarLineChartBase<T>.setThemeColors() where T: BarLineScatterCandleBubbleData<out IBarLineScatterCandleBubbleDataSet<out Entry>> {
    val typedValue = TypedValue()
    val theme = context.theme
    theme.resolveAttribute(R.attr.textColor, typedValue, true)
    @ColorInt val color = typedValue.data
    data?.setValueTextColor(color)
    xAxis?.textColor = color
    axisLeft?.textColor = color

    legend.textColor = color

    data?.dataSets?.forEach { it.valueTextColor = color }
}