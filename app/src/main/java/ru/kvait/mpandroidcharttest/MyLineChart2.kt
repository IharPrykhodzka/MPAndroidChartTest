package ru.kvait.mpandroidcharttest

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import androidx.core.graphics.values
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.components.YAxis.AxisDependency
import com.github.mikephil.charting.highlight.ChartHighlighter
import com.github.mikephil.charting.listener.BarLineChartTouchListener
import com.github.mikephil.charting.renderer.LineChartRenderer
import com.github.mikephil.charting.renderer.XAxisRenderer
import com.github.mikephil.charting.renderer.YAxisRenderer
import com.github.mikephil.charting.utils.MPPointD
import com.github.mikephil.charting.utils.Transformer
import com.github.mikephil.charting.utils.Utils

const val SAVE_MATRIX = "matrix"

class MyLineChart2 : LineChart {

    lateinit var sp : SharedPreferences

    constructor(context: Context) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        sp = context?.getSharedPreferences(SAVE_MATRIX, Context.MODE_PRIVATE)!!
    }

    //computeAxis
    private var totalTime: Long = 0
    private var drawCycles: Long = 0

    private var myCustomViewPortEnabled = false
    private val mOffsetsBuffer = RectF()

    private lateinit var myLeftAxisTransformer: MyTransformer
    private lateinit var myRightAxisTransformer: MyTransformer

    override fun init() {
        super.init()

        mAxisLeft = YAxis(AxisDependency.LEFT)
        mAxisRight = YAxis(AxisDependency.RIGHT)

        myLeftAxisTransformer = MyTransformer(mViewPortHandler)
        myRightAxisTransformer = MyTransformer(mViewPortHandler)

//        mLeftAxisTransformer = myLeftAxisTransformer
//        mRightAxisTransformer = myRightAxisTransformer

        mAxisRendererLeft = YAxisRenderer(mViewPortHandler, mAxisLeft, myLeftAxisTransformer)
        mAxisRendererRight = YAxisRenderer(mViewPortHandler, mAxisRight, myRightAxisTransformer)

        mXAxisRenderer = XAxisRenderer(mViewPortHandler, mXAxis, myLeftAxisTransformer)

        setHighlighter(ChartHighlighter(this))

        mChartTouchListener = BarLineChartTouchListener(this, mViewPortHandler.matrixTouch, 3f)

        mGridBackgroundPaint = Paint()
        mGridBackgroundPaint.style = Paint.Style.FILL

        // mGridBackgroundPaint.setColor(Color.WHITE);
        mGridBackgroundPaint.color = Color.rgb(240, 240, 240) // light

        // grey
        mBorderPaint = Paint()
        mBorderPaint.style = Paint.Style.STROKE
        mBorderPaint.color = Color.BLACK
        mBorderPaint.strokeWidth = Utils.convertDpToPixel(1f)

        mRenderer = LineChartRenderer(this, mAnimator, mViewPortHandler)
    }

    override fun prepareValuePxMatrix() {

        if (mLogEnabled) Log.i(
            LOG_TAG, "Preparing Value-Px Matrix, xmin: " + mXAxis.mAxisMinimum + ", xmax: "
                    + mXAxis.mAxisMaximum + ", xdelta: " + mXAxis.mAxisRange
        )

        myRightAxisTransformer.prepareMatrixValuePx(
            mXAxis.mAxisMinimum,
            mXAxis.mAxisRange,
            mAxisRight.mAxisRange,
            mAxisRight.mAxisMinimum
        )
        myLeftAxisTransformer.prepareMatrixValuePx(
            mXAxis.mAxisMinimum,
            mXAxis.mAxisRange,
            mAxisLeft.mAxisRange,
            mAxisLeft.mAxisMinimum
        )
    }

    override fun prepareOffsetMatrix() {
        myRightAxisTransformer.prepareMatrixOffset(mAxisRight.isInverted)
        myLeftAxisTransformer.prepareMatrixOffset(mAxisLeft.isInverted)
    }

    override fun getTransformer(which: AxisDependency?): Transformer {
        return if (which == AxisDependency.LEFT) myLeftAxisTransformer else myRightAxisTransformer
    }



    override fun calcMinMax() {

        Log.d("TAG!!!", "calcMinMax")

        val mTrans =  getTransformer(YAxis.AxisDependency.LEFT)


        val p1: MPPointD = mTrans.getValuesByTouchPoint(
            mViewPortHandler.contentLeft(),
            mViewPortHandler.contentTop()
        )
        val p2: MPPointD = mTrans.getValuesByTouchPoint(
            mViewPortHandler.contentLeft(),
            mViewPortHandler.contentBottom()
        )
        Log.d("TAG!!!", "calcMinMax p1 = $p1")
        Log.d("TAG!!!", "calcMinMax p2 = $p2")
        Log.d("TAG!!!", "calcMinMax mViewPortHandler.contentLeft() = ${mViewPortHandler.contentLeft()}")
        Log.d("TAG!!!", "calcMinMax mViewPortHandler.contentTop() = ${mViewPortHandler.contentTop()}")
        Log.d("TAG!!!", "calcMinMax mViewPortHandler.contentBottom() = ${mViewPortHandler.contentBottom()}")

        mXAxis.calculate(mData.xMin, mData.xMax)

        // calculate axis range (min / max) according to provided data
        mAxisLeft.calculate(mData.getYMin(YAxis.AxisDependency.LEFT), mData.getYMax(YAxis.AxisDependency.LEFT))
        mAxisRight.calculate(
            mData.getYMin(YAxis.AxisDependency.RIGHT),
            mData.getYMax(YAxis.AxisDependency.RIGHT)
        )
    }

    override fun calculateOffsets() {
        Log.d("TAG!!!", "calculateOffsets")
        if (!myCustomViewPortEnabled) {
            var offsetLeft = 0f
            var offsetRight = 0f
            var offsetTop = 0f
            var offsetBottom = 0f
            calculateLegendOffsets(mOffsetsBuffer)
            offsetLeft += mOffsetsBuffer.left
            offsetTop += mOffsetsBuffer.top
            offsetRight += mOffsetsBuffer.right
            offsetBottom += mOffsetsBuffer.bottom

            // offsets for y-labels
            if (mAxisLeft.needsOffset()) {
                offsetLeft += mAxisLeft.getRequiredWidthSpace(
                    mAxisRendererLeft
                        .paintAxisLabels
                )
            }
            if (mAxisRight.needsOffset()) {
                offsetRight += mAxisRight.getRequiredWidthSpace(
                    mAxisRendererRight
                        .paintAxisLabels
                )
            }
            if (mXAxis.isEnabled && mXAxis.isDrawLabelsEnabled) {
                val xLabelHeight = mXAxis.mLabelRotatedHeight + mXAxis.yOffset

                // offsets for x-labels
                if (mXAxis.position == XAxis.XAxisPosition.BOTTOM) {
                    offsetBottom += xLabelHeight
                } else if (mXAxis.position == XAxis.XAxisPosition.TOP) {
                    offsetTop += xLabelHeight
                } else if (mXAxis.position == XAxis.XAxisPosition.BOTH_SIDED) {
                    offsetBottom += xLabelHeight
                    offsetTop += xLabelHeight
                }
            }
            offsetTop += extraTopOffset
            offsetRight += extraRightOffset
            offsetBottom += extraBottomOffset
            offsetLeft += extraLeftOffset
            val minOffset = Utils.convertDpToPixel(mMinOffset)
            mViewPortHandler.restrainViewPort(
                Math.max(minOffset, offsetLeft),
                Math.max(minOffset, offsetTop),
                Math.max(minOffset, offsetRight),
                Math.max(minOffset, offsetBottom)
            )
            if (mLogEnabled) {
                Log.i(
                    LOG_TAG, "offsetLeft: " + offsetLeft + ", offsetTop: " + offsetTop
                            + ", offsetRight: " + offsetRight + ", offsetBottom: " + offsetBottom
                )
                Log.i(LOG_TAG, "Content: " + mViewPortHandler.contentRect.toString())
            }
        }

        prepareOffsetMatrix()
        prepareValuePxMatrix()
        Log.d("TAG!!!", "2 calcMinMax")

        val mTrans =  getTransformer(YAxis.AxisDependency.LEFT)


        val p1: MPPointD = mTrans.getValuesByTouchPoint(
            mViewPortHandler.contentLeft(),
            mViewPortHandler.contentTop()
        )
        val p2: MPPointD = mTrans.getValuesByTouchPoint(
            mViewPortHandler.contentLeft(),
            mViewPortHandler.contentBottom()
        )
        Log.d("TAG!!!", "2 calcMinMax p1 = $p1")
        Log.d("TAG!!!", "2 calcMinMax p2 = $p2")
        Log.d("TAG!!!", "2 calcMinMax mViewPortHandler.contentLeft() = ${mViewPortHandler.contentLeft()}")
        Log.d("TAG!!!", "2 calcMinMax mViewPortHandler.contentTop() = ${mViewPortHandler.contentTop()}")
        Log.d("TAG!!!", "2 calcMinMax mViewPortHandler.contentBottom() = ${mViewPortHandler.contentBottom()}")
    }

    override fun setViewPortOffsets(left: Float, top: Float, right: Float, bottom: Float) {
        super.setViewPortOffsets(left, top, right, bottom)
        myCustomViewPortEnabled = true
    }

    override fun resetViewPortOffsets() {
        super.resetViewPortOffsets()
        myCustomViewPortEnabled = false
    }

    override fun notifyDataSetChanged() {
        if (mData == null) {
            if (mLogEnabled) Log.i(LOG_TAG, "Preparing... DATA NOT SET.")
            return
        } else {
            if (mLogEnabled) Log.i(LOG_TAG, "Preparing...")
        }

        if (mRenderer != null) mRenderer.initBuffers()

        calcMinMax()

        mAxisRendererLeft.computeAxis(
            mAxisLeft.mAxisMinimum,
            mAxisLeft.mAxisMaximum,
            mAxisLeft.isInverted
        )
        mAxisRendererRight.computeAxis(
            mAxisRight.mAxisMinimum,
            mAxisRight.mAxisMaximum,
            mAxisRight.isInverted
        )
        mXAxisRenderer.computeAxis(mXAxis.mAxisMinimum, mXAxis.mAxisMaximum, false)

        if (mLegend != null) mLegendRenderer.computeLegend(mData)
        calculateOffsets()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.d("TAG!!!", "1 AppController.getInstance().setMyMatrix = ${mViewPortHandler.matrixTouch}")
        val isEvent = super.onTouchEvent(event)

        if (mTouchEnabled) {
            Log.d("TAG!!!", "2 AppController.getInstance().setMyMatrix = ${mViewPortHandler.matrixTouch.values()}")
            val m = mViewPortHandler.matrixTouch.values()
            AppController.getInstance().setMyMatrix(m)
        }

        return isEvent
    }
}