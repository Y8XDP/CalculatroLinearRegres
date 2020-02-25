package com.example.ashit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_add_xy.view.*
import kotlinx.android.synthetic.main.item_xy.view.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Data.dataList.add(ItemXY(652.870f, 357.191f))
        //Data.dataList.add(ItemXY(601.893f, 356.533f))
        //Data.dataList.add(ItemXY(590.792f, 376.951f))

        Data.dataList.add(ItemXY(102f, 200f))
        Data.dataList.add(ItemXY(220f, 330f))
        Data.dataList.add(ItemXY(320f, 450f))

        println(Data.dataList.size)
       /* Data.dataList.add(ItemXY())
        Data.dataList.add(ItemXY())
        Data.dataList.add(ItemXY())
        Data.dataList.add(ItemXY())
        Data.dataList.add(ItemXY())
        Data.dataList.add(ItemXY())
        Data.dataList.add(ItemXY())*/
    }

    fun solve(view: View){
        startActivity(Intent(this, ResultActivity::class.java))
    }

    fun add(view: View){
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_xy, null)

        val builder = AlertDialog.Builder(this)
            .setTitle("Новая строка")
            .setView(dialogView)
            .setPositiveButton("Добавить")  { dialog, _ ->
                if (dialogView.xEdit.text.isNotEmpty() && dialogView.yEdit.text.isNotEmpty()){
                    val x = dialogView.xEdit.text.toString()
                    val y = dialogView.yEdit.text.toString()

                    val item = ItemXY(x.toFloat(), y.toFloat())

                    Data.dataList.add(item)
                    addToVarList(item)
                    dialog.dismiss()
                }
            }

        val dialog = builder.create()
        dialog.show()
    }

    private fun addToVarList(item: ItemXY){
        val view = layoutInflater.inflate(R.layout.item_xy, null)

        view.xText.text = item.x.toString()
        view.yText.text = item.y.toString()

        container.addView(view)
    }
}
