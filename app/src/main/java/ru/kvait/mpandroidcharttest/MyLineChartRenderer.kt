package ru.kvait.mpandroidcharttest

import android.graphics.Canvas
import com.github.mikephil.charting.animation.ChartAnimator
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider
import com.github.mikephil.charting.renderer.LineChartRenderer
import com.github.mikephil.charting.utils.MPPointF
import com.github.mikephil.charting.utils.Utils
import com.github.mikephil.charting.utils.ViewPortHandler
import java.lang.NullPointerException

class MyLineChartRenderer(
    chart: LineDataProvider?,
    animator: ChartAnimator?,
    viewPortHandler: ViewPortHandler?
) : LineChartRenderer(chart, animator, viewPortHandler) {

    override fun drawValues(c: Canvas?) {
        try {
            if (isDrawingValuesAllowed(mChart)) {
                val dataSets = mChart.lineData.dataSets
                for (i in dataSets.indices) {
                    val dataSet = dataSets[i]
                    if (!shouldDrawValues(dataSet) || dataSet.entryCount < 1) continue

                    // apply the text-styling defined by the DataSet
                    applyValueTextStyle(dataSet)
                    val trans = mChart.getTransformer(dataSet.axisDependency)


                    // make sure the values do not interfear with the circles
                    var valOffset = (dataSet.circleRadius * 1.75f).toInt()
                    if (!dataSet.isDrawCirclesEnabled) valOffset = valOffset / 2
                    mXBounds[mChart] = dataSet
                    val initPositions = trans.generateTransformedValuesLine(
                        dataSet, mAnimator.phaseX, mAnimator
                            .phaseY, mXBounds.min, mXBounds.max
                    )
                    var positions = mutableListOf<Float>()
                    positions.add(initPositions[0])
                    positions.add(initPositions[1])

                    initPositions.forEach {
                        val indexValueSideBySide = initPositions.indexOfFirst { v -> v == it } - 2
                        if (indexValueSideBySide >= 0 && indexValueSideBySide % 2 == 0) {
                            val difference = it - initPositions[indexValueSideBySide]
                            val lastY = positions.last()
                            positions.add(it)
                            if (difference < 45) {
                                positions.add(lastY - 20)
                            } else {
                                positions.add(initPositions[indexValueSideBySide + 1])
                            }
                        }
                    }

                    val formatter = dataSet.valueFormatter
                    val iconsOffset = MPPointF.getInstance(dataSet.iconsOffset)
                    iconsOffset.x = Utils.convertDpToPixel(iconsOffset.x)
                    iconsOffset.y = Utils.convertDpToPixel(iconsOffset.y)

                    var j = 0
                    while (j < positions.size) {
                        val x = positions[j]
                        var y = positions[j + 1]
                        if (!mViewPortHandler.isInBoundsRight(x)) break
                        if (!mViewPortHandler.isInBoundsLeft(x) || !mViewPortHandler.isInBoundsY(y)) {
                            j += 2
                            continue
                        }
                        val entry = dataSet.getEntryForIndex(j / 2 + mXBounds.min)

                        if (dataSet.isDrawValuesEnabled) {
                            drawValue(
                                c,
                                formatter.getPointLabel(entry),
                                x,
                                y - valOffset,
                                dataSet.getValueTextColor(j / 2)
                            )
                        }
                        if (entry.icon != null && dataSet.isDrawIconsEnabled) {
                            val icon = entry.icon
                            Utils.drawImage(
                                c,
                                icon,
                                (x + iconsOffset.x).toInt(),
                                (y + iconsOffset.y).toInt(),
                                icon.intrinsicWidth,
                                icon.intrinsicHeight
                            )
                        }
                        j += 2
                    }
                    MPPointF.recycleInstance(iconsOffset)
                }
            }
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
    }

    override fun drawData(c: Canvas?) {
        try {
            super.drawData(c)
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
    }
}