package ru.kvait.mpandroidcharttest

import android.util.Log
import androidx.core.graphics.values
import com.github.mikephil.charting.utils.MPPointD
import com.github.mikephil.charting.utils.Transformer
import com.github.mikephil.charting.utils.ViewPortHandler

class MyTransformer(viewPortHandler: ViewPortHandler?) : Transformer(viewPortHandler) {

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