package ru.kvait.mpandroidcharttest

import android.app.Application
import android.graphics.Matrix

class AppController : Application() {

    private lateinit var myMatrix: Matrix
    private lateinit var currentChart: MyLineChart2
    private var isRefreshMatrix: Boolean = false

    var control: Int = 1
        get() = field % 2
        set(value) {
            field += value
        }

    fun setMyMatrix(m: FloatArray) {
        myMatrix.setValues(m)
    }

    fun getMyMatrix(): Matrix {
        return myMatrix
    }

    fun setCurrentChart(chart: MyLineChart2) {
        currentChart = chart
    }

    fun getCurrentChart() = currentChart

    fun setIsRefreshMatrix(isRefresh: Boolean) {
        isRefreshMatrix = isRefresh
    }

    fun isRefreshMatrix() = isRefreshMatrix

    override fun onCreate() {
        super.onCreate()
        instance = this
        myMatrix = Matrix()
    }

    companion object {
        @get:Synchronized
        private lateinit var instance: AppController

        fun getInstance() = instance
    }
}