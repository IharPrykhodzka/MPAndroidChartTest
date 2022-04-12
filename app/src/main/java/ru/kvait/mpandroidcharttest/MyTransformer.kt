package ru.kvait.mpandroidcharttest

import android.util.Log
import androidx.core.graphics.values
import com.github.mikephil.charting.utils.MPPointD
import com.github.mikephil.charting.utils.Transformer
import com.github.mikephil.charting.utils.ViewPortHandler

class MyTransformer(viewPortHandler: ViewPortHandler?) : Transformer(viewPortHandler) {

    override fun pixelsToValue(pixels: FloatArray?) {
        val myMatrix = AppController.getInstance().getMyMatrix()
        val dp = AppController.getInstance()
        Log.d("TAG!!!", "1   first barer pixelsToValue, dp.control = ${dp.control}")
        if (myMatrix != mViewPortHandler.matrixTouch || dp.control == 0) {
            dp.control = 1
            //Log.d("TAG!!!", "1 before mViewPortHandler = ${mViewPortHandler.matrixTouch}")
            //mViewPortHandler.matrixTouch.setValues(myMatrix.values())
            Log.e("TAG!!!", "first barer pixelsToValue = dp.setIsRefreshMatrix( TRUE )", Exception())
            dp.setIsRefreshMatrix(true)
            mViewPortHandler.refresh(myMatrix, dp.getCurrentChart(), true)
            return
            //Log.d("TAG!!!", "2 after mViewPortHandler = ${mViewPortHandler.matrixTouch}")
        }
        Log.d("TAG!!!", "2   first barer pixelsToValue")

        val tmp = mPixelToValueMatrixBuffer
        tmp.reset()

//        Log.e("TAG!!!", "_____________pixelsToValue__________________")
//        Log.e("TAG!!!", "mMatrixOffset = $mMatrixOffset")
        // invert all matrixes to convert back to the original value
        mMatrixOffset.invert(tmp)
        tmp.mapPoints(pixels)

//        Log.e("TAG!!!", "mViewPortHandler = ${mViewPortHandler.matrixTouch}")
//        Log.e("TAG!!!", "getMatrix = ${myMatrix}")

        if (myMatrix != mViewPortHandler.matrixTouch) {
            //Log.d("TAG!!!", "1 before mViewPortHandler = ${mViewPortHandler.matrixTouch}")
            //mViewPortHandler.matrixTouch.setValues(myMatrix.values())
            Log.e("TAG!!!", "pixelsToValue = dp.setIsRefreshMatrix( TRUE )")
            dp.setIsRefreshMatrix(true)
            mViewPortHandler.refresh(myMatrix, dp.getCurrentChart(), true)
            //Log.d("TAG!!!", "2 after mViewPortHandler = ${mViewPortHandler.matrixTouch}")
        }else {
            Log.d("TAG!!!", "pixelsToValue = dp.setIsRefreshMatrix( FALSE )")
            dp.setIsRefreshMatrix(false)
            mViewPortHandler.matrixTouch.invert(tmp) }

        tmp.mapPoints(pixels)

        //Log.e("TAG!!!", "mMatrixValueToPx = $mMatrixValueToPx")
        mMatrixValueToPx.invert(tmp)
        tmp.mapPoints(pixels)
    }
}