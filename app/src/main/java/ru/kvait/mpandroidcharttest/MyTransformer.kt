package ru.kvait.mpandroidcharttest

import android.util.Log
import androidx.core.graphics.values
import com.github.mikephil.charting.utils.MPPointD
import com.github.mikephil.charting.utils.Transformer
import com.github.mikephil.charting.utils.ViewPortHandler

class MyTransformer(viewPortHandler: ViewPortHandler?) : Transformer(viewPortHandler) {

    private var first = Pair(0.0, 0.0)
    private var second = Pair(0.0, 0.0)
    private var bufferListPoint = mutableListOf<Pair<Double, Double>>()
    private var reverse = false

    override fun getValuesByTouchPoint(x: Float, y: Float): MPPointD {
        val result = MPPointD.getInstance(0.0, 0.0)
        getValuesByTouchPoint(x, y, result)
        bufferListPoint.add(Pair(result.x, result.y))

//        if (bufferListPoint.size == 1000) {
//            val clearList = mutableListOf<Pair<Double, Double>>()
//            for ( i in 990..999) {
//                clearList.add(bufferListPoint[i])
//            }
//            bufferListPoint.clear()
//            bufferListPoint.addAll(clearList)
//        }

        if (bufferListPoint.size > 7) {
            val index1 = bufferListPoint[bufferListPoint.size - 1]
            val index2 = bufferListPoint[bufferListPoint.size - 2]
            val index3 = bufferListPoint[bufferListPoint.size - 3]
            val index4 = bufferListPoint[bufferListPoint.size - 4]
            val index5 = bufferListPoint[bufferListPoint.size - 5]
            val index6 = bufferListPoint[bufferListPoint.size - 6]
        }

        if (result.y == 0.0 && bufferListPoint.size > 10) {
            val index = bufferListPoint.size - 3
            reverse = true
            val myResult =
                MPPointD.getInstance(bufferListPoint[index].first, bufferListPoint[index].second)
            return myResult
        }

        if (reverse) {
            val index = bufferListPoint.size - 3
            reverse = false
            val myResult =
                MPPointD.getInstance(bufferListPoint[index].first, bufferListPoint[index].second)
            return myResult
        }

        return result
    }

    override fun pixelsToValue(pixels: FloatArray?) {
        val myMatrix = AppController.getInstance().getMyMatrix()

        val tmp = mPixelToValueMatrixBuffer
        tmp.reset()

        Log.e("TAG!!!", "_____________pixelsToValue__________________")
        Log.e("TAG!!!", "mMatrixOffset = $mMatrixOffset")
        // invert all matrixes to convert back to the original value
        mMatrixOffset.invert(tmp)
        tmp.mapPoints(pixels)

        Log.e("TAG!!!", "mViewPortHandler = ${mViewPortHandler.matrixTouch}")
        Log.e("TAG!!!", "getMatrix = ${myMatrix}")

        if (myMatrix != mViewPortHandler.matrixTouch) {
            Log.d("TAG!!!", "1 before mViewPortHandler = ${mViewPortHandler.matrixTouch}")
            //mViewPortHandler.matrixTouch.setValues(myMatrix.values())
            mViewPortHandler.refresh(myMatrix, AppController.getInstance().getCurrentChart(), true)
            Log.d("TAG!!!", "2 after mViewPortHandler = ${mViewPortHandler.matrixTouch}")
        }else mViewPortHandler.matrixTouch.invert(tmp)

        tmp.mapPoints(pixels)

        Log.e("TAG!!!", "mMatrixValueToPx = $mMatrixValueToPx")
        mMatrixValueToPx.invert(tmp)
        tmp.mapPoints(pixels)

    }
}