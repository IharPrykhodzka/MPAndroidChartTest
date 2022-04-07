package ru.kvait.mpandroidcharttest

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import ru.kvait.mpandroidcharttest.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    var number = 1
    val data = listOf(
        0,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1,
        1,
        -1
    )
    val data2 = listOf(
        0,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10,
        10,
        -10
    )
    var timer = Timer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.stop.isEnabled = false

        updateRawDataChart(data, this, binding.chart)
        AppController.getInstance().setCurrentChart(binding.chart)
        var currentData = listOf(0)
        binding.start.setOnClickListener {
//            currentData = if (currentData != data) data
//            else data2
//            updateRawDataChart(currentData, this@MainActivity, binding.chart)
//            binding.text.text = number++.toString()

            binding.stop.isEnabled = true
            Toast.makeText(this@MainActivity, "Start", Toast.LENGTH_SHORT).show()


            timer.schedule(object : TimerTask(){
                override fun run() {
                    currentData = if (currentData != data) data
                    else data2
                    updateRawDataChart(currentData, this@MainActivity, binding.chart)

                    runOnUiThread {
                        binding.text.text = number++.toString()
                    }
                }
            }, 100, 1500)
            binding.start.isEnabled = false
        }

        binding.stop.setOnClickListener {
            Toast.makeText(this@MainActivity, "Stop", Toast.LENGTH_SHORT).show()
            timer.cancel()
            timer = Timer()
            number = 1
            binding.start.isEnabled = true
            binding.stop.isEnabled = false
        }
    }

    private fun updateRawDataChart(
        data: List<Int>,
        c: Context,
        chart: MyLineChart2,
        yMin: Float = -30f,
        yMax: Float = 30f
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
//                setMaxVisibleValueCount((data.size * 1.1f).toInt())
//                setVisibleXRangeMinimum(100f)
//                setVisibleXRangeMaximum((data.size * 1.1f).toFloat())
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
                ds.color = ContextCompat.getColor(c, R.color.teal_200)

                l.data = LineData(ds)
                oldHlX?.let { hl ->
                    l.highlightValue(Highlight(hl, 0, 0))
                }
            }
        }
        chart.invalidate()
        var list = listOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f).toFloatArray()
        chart.viewPortHandler.contentCenter
        chart.viewPortHandler.matrixTouch.getValues(list)
        Log.d("TAG!!!", "list ")
        Log.d("TAG!!!", "list x 0 = ${list[0]}")
        Log.d("TAG!!!", "list x 1 = ${list[1]}")
        Log.d("TAG!!!", "list x 2 = ${list[2]}")
        Log.d("TAG!!!", "list y 3 = ${list[3]}")
        Log.d("TAG!!!", "list y 4 = ${list[4]}")
        Log.d("TAG!!!", "list y 5 = ${list[5]}")
        Log.d("TAG!!!", "list = ${list[6]}")
        Log.d("TAG!!!", "list = ${list[7]}")
        Log.d("TAG!!!", "list = ${list[8]}")
//        chart.moveViewTo(list[2], list[5] / 2, YAxis.AxisDependency.LEFT)
//        chart.centerViewToY(0f, YAxis.AxisDependency.LEFT)
//        chart.centerViewTo(list[2], 0f, YAxis.AxisDependency.LEFT)


    }

    private fun initRawDataChart(
        chart: MyLineChart2
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

    fun moveChartViewToY() {
        binding.chart.centerViewToY(0f, YAxis.AxisDependency.LEFT)
    }
}