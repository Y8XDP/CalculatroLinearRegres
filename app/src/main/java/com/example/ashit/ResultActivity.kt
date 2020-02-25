package com.example.ashit

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import kotlinx.android.synthetic.main.activity_result.*
import kotlin.math.sqrt

class ResultActivity : AppCompatActivity() {

    var _x = 0f
    var _y = 0f
    var _xy = 0f

    var sumX = 0f
    var sumY = 0f
    var sumXY = 0f

    var sumXsqrt = 0f
    var sumYsqrt = 0f

    var dispXsqrt = 0f
    var dispYsqrt = 0f

    var corelliation = 0f

    var aCof = 0f
    var bCof = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        Data.dataList.forEach {
            sumX += it.x
            sumY += it.y
            sumXY += it.x * it.y

            sumXsqrt += it.x * it.x
            sumYsqrt += it.y * it.y
        }

        _x = sumX / Data.dataList.size
        _y = sumY / Data.dataList.size
        _xy = sumXY / Data.dataList.size

        dispXsqrt = sumXsqrt / Data.dataList.size
        dispYsqrt = sumYsqrt / Data.dataList.size

        dispXsqrt -= _x * _x
        dispYsqrt -= _y * _y

        corelliation = (_xy - _x * _y) / (sqrt(dispXsqrt) * sqrt(dispYsqrt))

        bCof = ((Data.dataList.size * sumXY) - (sumX * sumY)) / ((Data.dataList.size * sumXsqrt) - (sumX * sumX))
        aCof = (sumY - (bCof * sumX)) / Data.dataList.size

        println("a = " + aCof)
        println("b = " + bCof)

        corell.text = corelliation.toString()
        fisher.text = (dispXsqrt / dispYsqrt).toString()

        initChart()
    }

    private fun initChart(){
        chart.description.isEnabled = false
        chart.setTouchEnabled(true)
        //chart.setOnChartValueSelectedListener(this)
        chart.setDrawGridBackground(false)
        chart.isDragEnabled = true
        chart.setScaleEnabled(true)
        chart.setPinchZoom(true)

        chart.legend.isEnabled = false

        setData()
    }

    private fun setData() {
        try{
            val dataSets: ArrayList<ILineDataSet> = ArrayList()

            val values1 = ArrayList<Entry>()

            for (i in 0 until Data.dataList.size) {
                val set = LineDataSet(values1, i.toString())
                set.values = arrayListOf(Entry(Data.dataList[i].x, Data.dataList[i].y))
                set.axisDependency = YAxis.AxisDependency.LEFT
                set.color = Color.rgb(255, 241, 46)
                set.setDrawCircles(true)
                set.circleRadius = 3f
                set.setDrawCircleHole(true)

                dataSets.add(set)
            }

            for (i in 0 until Data.dataList.size){
                val y = aCof + Data.dataList[i].x * bCof
                values1.add(Entry(Data.dataList[i].x, y))
            }

            val set1 = LineDataSet(values1, "DataSet 1")
             set1.color = Color.rgb(255, 241, 46)
             set1.setDrawCircles(false)
             set1.lineWidth = 2f
             set1.circleRadius = 3f
             set1.setDrawFilled(true)
             set1.fillColor = Color.WHITE
             set1.highLightColor = Color.rgb(244, 117, 117)
             set1.setDrawCircleHole(false)
             set1.fillFormatter =
                 IFillFormatter { dataSet, dataProvider ->
                     // change the return value here to better understand the effect
                     // return 0;
                     chart.axisLeft.axisMinimum
                 }


            dataSets.add(set1)

            val data = LineData(dataSets)
            data.setDrawValues(false)

            chart.data = data

        }catch (e: Exception){
            println(e.toString())
        }
    }
}
