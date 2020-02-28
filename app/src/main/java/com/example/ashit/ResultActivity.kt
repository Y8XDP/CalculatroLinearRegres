package com.example.ashit

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import kotlinx.android.synthetic.main.activity_result.*
import kotlin.math.abs
import kotlin.math.sqrt

class ResultActivity : AppCompatActivity() {

    // _ в начале = сумма деленная на количество

    var _x = 0f
    var _y = 0f
    var _xy = 0f

    var sumX = 0f
    var sumY = 0f
    var sumXY = 0f

    //sqrt на конце = 2 степени

    var sumXsqrt = 0f
    var sumYsqrt = 0f

    var dispXsqrt = 0f
    var dispYsqrt = 0f

    var corelliation = 0f


    // для построения графика используется формула y = a + b * x
    // коэффиценты нужно будет высчитывать по формулам ниже
    var aCof = 0f
    var bCof = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        //dataList находится в классе Data, в нем объекты с x и y
        //перебором всех значений вычисляю суммы

        Data.dataList.forEach {
            sumX += it.x
            sumY += it.y

            sumXY += it.x * it.y

            //сумма квадратов
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


        // если корелляция > 0 -> if
        // если корелляция < 0 -> else
        var corellParamText: String = if (corelliation > 0){
            "Функция положительна\n"
        }else{
            "Функция отрицательна\n"
        }

        corellParamText += if (abs(corelliation) < 0.5){
            "|r| < 0.5 -> слабая теснота"
        }else if (abs(corelliation) > 0.5 && abs(corelliation) < 0.8){
            "0.5 < |r| < 0.8 -> средняя теснота"
        }else{
            "0.8 < |r| -> высокая теснота"
        }

        corellParam.text = corellParamText

        corell.text = corelliation.toString()

        // Фишер в процентах, закоменченную формулу не юзай, но оставь на всякий случай.
        // Я нашел 2 источника с разной формулой
        fisher.text = ((corelliation * corelliation) * 100).toString() + "%" //(dispXsqrt / dispYsqrt)

        initChart()
    }

    // этот метод бесполезен для изучения
    private fun initChart(){
        chart.description.isEnabled = false
        chart.setTouchEnabled(true)
        chart.isDragEnabled = true
        chart.setScaleEnabled(true)

        chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        chart.legend.isEnabled = false

        setData()
    }


    // Тут есть формулы рассчета коэффицентов

    // Сначала на графике просто расставляешь точки и потом отдельно прямую рисуешь по тем формулам
    private fun setData() {
        try{
            // Лучше сделать сортировку по x по возрастанию, иначе график может сказать, что ты ему дичь втираешь
            Data.dataList.sortBy {it.x}

            val dataSets: ArrayList<ILineDataSet> = ArrayList()

            val values1 = ArrayList<Entry>()

            for (i in 0 until Data.dataList.size) {
                val set = LineDataSet(values1, i.toString())
                set.values = arrayListOf(Entry(Data.dataList[i].x, Data.dataList[i].y))
                set.axisDependency = YAxis.AxisDependency.LEFT
                set.setCircleColor(Color.rgb(100, 255, 150))
                set.setDrawCircles(true)
                set.circleRadius = 3f
                set.setDrawCircleHole(true)

                dataSets.add(set)
            }

            for (i in 0 until Data.dataList.size){

                // Формулы тут
                val y = aCof + Data.dataList[i].x * bCof

                // x = просто x
                values1.add(Entry(Data.dataList[i].x, y))
            }

            val set1 = LineDataSet(values1, "DataSet 1")
            set1.color = Color.rgb(255, 50, 50)
            set1.lineWidth = 2f

            set1.setDrawCircles(false)

            dataSets.add(set1)

            val data = LineData(dataSets)
            data.setDrawValues(false)

            chart.data = data
        }catch (e: Exception){
            println(e.toString())
        }
    }
}
