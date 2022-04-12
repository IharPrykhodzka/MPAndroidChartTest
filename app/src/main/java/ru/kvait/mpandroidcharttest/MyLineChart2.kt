package ru.kvait.mpandroidcharttest

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.text.TextUtils
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
import com.github.mikephil.charting.utils.Transformer
import com.github.mikephil.charting.utils.Utils

class MyLineChart2 : LineChart {

    constructor(context: Context) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    private var myCustomViewPortEnabled = false
    private val mOffsetsBuffer = RectF()

    private lateinit var myLeftAxisTransformer: MyTransformer
    private lateinit var myRightAxisTransformer: MyTransformer

    override fun init() {
        super.init()

        mAxisLeft = YAxis(AxisDependency.LEFT)
        mAxisRight = YAxis(AxisDependency.RIGHT)

        /** Change to MyTransformer **/
        myLeftAxisTransformer = MyTransformer(mViewPortHandler)
        myRightAxisTransformer = MyTransformer(mViewPortHandler)

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

    override fun calculateOffsets() {
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
    }

    override fun setViewPortOffsets(left: Float, top: Float, right: Float, bottom: Float) {
        super.setViewPortOffsets(left, top, right, bottom)
        myCustomViewPortEnabled = true
    }

    override fun resetViewPortOffsets() {
        super.resetViewPortOffsets()
        myCustomViewPortEnabled = false
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val isEvent = super.onTouchEvent(event)

        /** Save Matrix after scaling and zooming **/
        if (mTouchEnabled) {
            val m = mViewPortHandler.matrixTouch.values()
            AppController.getInstance().setMyMatrix(m)
        }

        return isEvent
    }

    override fun onDraw(canvas: Canvas?) {
        //TODO В трансформере поставить флаг мол маьрица не совпадает,
        //TODO переопределить матрицу и прервать отрисовку.
        //TODO Постораться оставить первичную отрисовку, пропустив последующие.
        //TODO Должно привисти к меньшему дёрганью и загрузки приложения.

        // super.onDraw(canvas);
        if (mData == null) {
            super.onDraw(canvas)
            return
        }

        //if (mData == null) return

        val starttime = System.currentTimeMillis()

        // execute all drawing commands
        drawGridBackground(canvas)

        if (mAutoScaleMinMaxEnabled) {
            autoScale()
        }

        if (mAxisLeft.isEnabled) mAxisRendererLeft.computeAxis(
            mAxisLeft.mAxisMinimum,
            mAxisLeft.mAxisMaximum,
            mAxisLeft.isInverted
        )

        if (AppController.getInstance().isRefreshMatrix()) {
            Log.e("TAG!!!", "onTouchEvent = return")
            AppController.getInstance().setIsRefreshMatrix(false)
            return}

        if (mAxisRight.isEnabled) mAxisRendererRight.computeAxis(
            mAxisRight.mAxisMinimum,
            mAxisRight.mAxisMaximum,
            mAxisRight.isInverted
        )

        if (AppController.getInstance().isRefreshMatrix()) {
            Log.e("TAG!!!", "onTouchEvent = return")
            AppController.getInstance().setIsRefreshMatrix(false)
            return}

        if (mXAxis.isEnabled) mXAxisRenderer.computeAxis(
            mXAxis.mAxisMinimum,
            mXAxis.mAxisMaximum,
            false
        )

        if (AppController.getInstance().isRefreshMatrix()) {
            Log.e("TAG!!!", "onTouchEvent = return")
            AppController.getInstance().setIsRefreshMatrix(false)
            return}

        Log.d("TAG!!!", "onTouchEvent = DRAW")

        mXAxisRenderer.renderAxisLine(canvas)
        mAxisRendererLeft.renderAxisLine(canvas)
        mAxisRendererRight.renderAxisLine(canvas)

        if (mXAxis.isDrawGridLinesBehindDataEnabled) mXAxisRenderer.renderGridLines(canvas)

        if (mAxisLeft.isDrawGridLinesBehindDataEnabled) mAxisRendererLeft.renderGridLines(canvas)

        if (mAxisRight.isDrawGridLinesBehindDataEnabled) mAxisRendererRight.renderGridLines(canvas)

        if (mXAxis.isEnabled && mXAxis.isDrawLimitLinesBehindDataEnabled) mXAxisRenderer.renderLimitLines(
            canvas
        )

        if (mAxisLeft.isEnabled && mAxisLeft.isDrawLimitLinesBehindDataEnabled) mAxisRendererLeft.renderLimitLines(
            canvas
        )

        if (mAxisRight.isEnabled && mAxisRight.isDrawLimitLinesBehindDataEnabled) mAxisRendererRight.renderLimitLines(
            canvas
        )

        // make sure the data cannot be drawn outside the content-rect
        var clipRestoreCount = canvas!!.save()
        canvas!!.clipRect(mViewPortHandler.contentRect)

        mRenderer.drawData(canvas)

        if (!mXAxis.isDrawGridLinesBehindDataEnabled) mXAxisRenderer.renderGridLines(canvas)

        if (!mAxisLeft.isDrawGridLinesBehindDataEnabled) mAxisRendererLeft.renderGridLines(canvas)

        if (!mAxisRight.isDrawGridLinesBehindDataEnabled) mAxisRendererRight.renderGridLines(canvas)

        // if highlighting is enabled
        if (valuesToHighlight()) mRenderer.drawHighlighted(canvas, mIndicesToHighlight)


        // Removes clipping rectangle
        canvas!!.restoreToCount(clipRestoreCount)

        mRenderer.drawExtras(canvas)

        if (mXAxis.isEnabled && !mXAxis.isDrawLimitLinesBehindDataEnabled) mXAxisRenderer.renderLimitLines(
            canvas
        )

        if (mAxisLeft.isEnabled && !mAxisLeft.isDrawLimitLinesBehindDataEnabled) mAxisRendererLeft.renderLimitLines(
            canvas
        )

        if (mAxisRight.isEnabled && !mAxisRight.isDrawLimitLinesBehindDataEnabled) mAxisRendererRight.renderLimitLines(
            canvas
        )

        mXAxisRenderer.renderAxisLabels(canvas)
        mAxisRendererLeft.renderAxisLabels(canvas)
        mAxisRendererRight.renderAxisLabels(canvas)

        if (isClipValuesToContentEnabled) {
            clipRestoreCount = canvas!!.save()
            canvas!!.clipRect(mViewPortHandler.contentRect)
            mRenderer.drawValues(canvas)
            canvas!!.restoreToCount(clipRestoreCount)
        } else {
            mRenderer.drawValues(canvas)
        }

        mLegendRenderer.renderLegend(canvas)

        drawDescription(canvas)

        drawMarkers(canvas)

//        if (mLogEnabled) {
//            val drawtime = System.currentTimeMillis() - starttime
//            totalTime += drawtime
//            drawCycles += 1
//            val average = totalTime / drawCycles
//            Log.i(
//                LOG_TAG, "Drawtime: " + drawtime + " ms, average: " + average + " ms, cycles: "
//                        + drawCycles
//            )
//        }
    }
}