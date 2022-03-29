package ru.kvait.mpandroidcharttest

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var chart: LineChart
    lateinit var btnStart: Button
    lateinit var btnStop: Button
    lateinit var txt: TextView
    var number = 1
    val data = listOf(0, 1, -1, 1, -1, 1, -1, 1, -1, 1, -1, 1, -1,
        1, -1, 1, -1, 1, -1, 1, -1, 1, -1, 1, -1, 1, -1, 1, -1, 1, -1,
        1, -1, 1, -1, 1, -1, 1, -1, 1, -1, 1, -1, 1, -1, 1, -1, 1, -
        1, 1, -1, 1, -1, 1, -1, 1, -1, 1, -1, 1, -1, 1, -1, 1, -1, 1, -1, 1,
        -1, 1, -1, 1, -1, 1, -1, 1, -1, 1, -1, 1, -1, 1, -1, 1, -1,
        1, -1, 1, -1, 1, -1, 1, -1, 1, -1, 1, -1, 1, -1, 1, -1, 1, -1, 1, -1, 1, -
        1, 1, -1, 1, -1, 1, -1, 1, -1, 1, -1, 1, -1, 1, -1, 1, -1, 1,
        -1, 1, -1, 1, -1, 1, -1, 1, -1, 1, -1, 1, -1, 1, -1, 1, -1, 1, -1, 1, -1,
        1, -1, 1, -1, 1, -1, 1, -1, 1, -1, 1, -1, 1, -1, 1, -1, 1, -
        1, 1, -1, 1, -1, 1, -1, 1, -1, 1, -1, 1, -1, 1, -1, 1, -1, 1, -1, 1, -1,
        1, -1, 1, -1, 1, -1, 1, -1, 1, -1, 1, -1, 1, -1, 1, -1, 1, -1
        , 1, -1, 1, -1, 1, -1, 1, -1, 1, -1, 1, -1, 1, -1, 1, -1, 1, -1, 1, -1,
        1, -1, 1, -1, 1, -1, 1, -1, 1, -1, 1, -1, 1, -1, 1, -1, 1, -1
        , 1, -1, 1, -1, 1, -1, 1, -1, 1, -1, 1, -1, 1, -1, 1, -1, 1, -1, 1, -1
        , 1, -1, 1, -1, 1, -1, 1, -1, 1, -1, 1, -1, 1, -1, 1, -1, 1, -
        1, 1, -1, 1, -1, 1, -1, 1, -1, 1, -1, 1, -1, 1, -1, 1, -1, 1, -1, 1,
        -1, 1, -1, 1, -1, 1, -1, 1, -1, 1, -1, 1, -1, 1
        , -1, 1, -1, 1, -1, 1, -1, 1, -1, 1, -1, 1, -1, 1, -1, 1, -1, 1
        , -1, 1, -1, 1, -1, 1, -1, 1, -1, 1, -1, 1)
    var timer = Timer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        chart = findViewById(R.id.chart)
        btnStart = findViewById(R.id.start)
        btnStop = findViewById(R.id.stop)
        txt = findViewById(R.id.text)
        btnStop.isEnabled = false

        updateRawDataChart(data, this, chart, -420f, 420f)

        btnStart.setOnClickListener {
            btnStop.isEnabled = true
            Toast.makeText(this@MainActivity, "Start", Toast.LENGTH_SHORT).show()
            timer.schedule(object : TimerTask(){
                override fun run() {
                    updateRawDataChart(data, this@MainActivity, chart, -420f, 420f)

                    runOnUiThread {
                        txt.text = number++.toString()
                    }
                }
            }, 100, 1500)
            btnStart.isEnabled = false
        }

        btnStop.setOnClickListener {
            Toast.makeText(this@MainActivity, "Stop", Toast.LENGTH_SHORT).show()
            timer.cancel()
            timer = Timer()
            number = 1
            btnStart.isEnabled = true
            btnStop.isEnabled = false
        }
    }

    private fun updateRawDataChart(
        data: List<Int>,
        c: Context,
        chart: LineChart,
        yMin: Float,
        yMax: Float
    ) {
        chart.let { l ->
            l.renderer = MyLineChartRenderer(l, l.animator, l.viewPortHandler)
            l.xAxis?.apply {
                axisMinimum = 0f
                axisMaximum = data.size.toFloat()
            }

            l.axisLeft?.apply {
                axisMinimum = yMin
                axisMaximum = yMax
            }

            l.apply {
                setMaxVisibleValueCount((data.size * 1.1f).toInt())
                setVisibleXRangeMinimum(100f)
                setVisibleXRangeMaximum((data.size * 1.1f).toFloat())
                moveViewToX((data.size * 1.1f).toFloat())
            }

            val oldHlX = l.highlighted?.let { if (it.isNotEmpty()) it.get(0).x else null }

            if (l.data == null) {
                initRawDataChart(l)
            }

            l.data =
                null // chart library isn't separate init process of datasets and fetch the data values
            if (l.data == null) {
                var entries = listOf(Entry(0f, 0f))
                if (data != null) {
                    var startX = 0f
                    entries = data.mapIndexed { i, y ->
                        Entry(startX++, y.toFloat())
                    }
                }
                val ds = LineDataSet(entries, "test")
                ds.setDrawCircles(false)
                ds.setDrawValues(false)
                ds.color = ContextCompat.getColor(c, R.color.purple_700)

                l.data = LineData(ds)
                oldHlX?.let { hl ->
                    l.highlightValue(Highlight(hl, 0, 0))
                }
            }
        }
        chart.invalidate()
    }

    private fun initRawDataChart(
        chart: LineChart
    ) {
        chart.description.text = "test"
        chart.legend.isEnabled = true
        chart.axisRight.isEnabled = false
        chart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            labelCount = 8
            setDrawAxisLine(true)
            setDrawGridLines(true)
        }
        chart.axisLeft.apply {
            labelCount = 8
            setDrawTopYLabelEntry(true)
            setDrawZeroLine(true)
            setDrawGridLines(true)
        }
        var capturedDescription = chart.description.text
        chart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                chart.description.text =
                    "$capturedDescription ${e?.y!!}"
            }

            override fun onNothingSelected() {
                chart.description.text = capturedDescription
            }

        })
    }
}

//            var dataValues: List<Float>? =
//                data?.values?.map { (it.siAc / hrw?.rankMult!!).toFloat() }

//            var startX: Float? = null
//            var dx = 0f
//            if (drawOnPeriodStart && dataValues != null) { //todo: it is spike! use real math
//                data?.let { data ->
//                    val df = data.discretizationFrequence
//                    val dataValuesArr = dataValues!!.map { it.toDouble() }.toDoubleArray()
//                    val periods1 =
//                        TRCUtils2.periodStartIndex(dataValuesArr, df.toDouble(), null)
//                    val ps = periods1.second.map {
//                        TRCUtils2.Companion.SignalPeriod(it.first, it.second, it.third)
//                    }
//                    if (periods1.second.isNotEmpty()) {
//                        val p1 = periods1.second[0]
//                        val sp =
//                            TRCUtils2.Companion.SignalPeriod(p1.first, p1.second, p1.third)
//                        try {
//                            val realStart =
//                                sp.realStart(
//                                    periods1.first,
//                                    TRCUtils2.Companion.SignalPeriod.symmetry(
//                                        ps,
//                                        periods1.first
//                                    )
//                                )
//                            val sliced =
//                                periods1.first.slice(
//                                    IntRange(
//                                        p1.first,
//                                        dataValues!!.size - 1
//                                    )
//                                )
//                                    .map { it.toFloat() }.toTypedArray()
//                            dataValues =
//                                arrayOf(realStart.second.toFloat(), *sliced).toList()
//                            startX = realStart.first.toFloat() - p1.first + 1
//                            dx = 0f - startX!!
//                        } catch (e: Exception) {
//                            Log.e(TAG, "realStart error ${e.message}")
//                        }
//                    }
//                }
//            }