package ru.kvait.mpandroidcharttest

import android.app.Application
import android.graphics.Matrix

class AppController : Application() {

    private lateinit var myMatrix: Matrix
    private lateinit var currentChart: MyLineChart2

    fun setMyMatrix(m: FloatArray) {
        myMatrix.setValues(m)
    }
    fun getMyMatrix() : Matrix {
       return myMatrix
    }

    fun setCurrentChart(chart: MyLineChart2) {
        currentChart = chart
    }
    fun getCurrentChart() = currentChart

    override fun onCreate() {
        super.onCreate()
        instance = this
        myMatrix = Matrix()
    }


    companion object {
        @get:Synchronized
        private lateinit var instance : AppController

        fun getInstance() = instance
    }
}