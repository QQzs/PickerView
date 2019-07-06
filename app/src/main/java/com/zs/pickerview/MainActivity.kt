package com.zs.pickerview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.zs.pickerview.dialog.CityDialog
import com.zs.pickerview.dialog.DateDialog
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun showDate(view: View){

        DateDialog.Builder(this)
                .setTitle("生日选择")
                .setStartYear(1990)
                .setDefaultDate(2000 , 1, 1)
                .setSelectDate("2008-08-08")
                .setDateListener(object : DateDialog.DialogDateListener{
                    override fun onDialogDateBack(date: String) {
                        toast(date)
                    }

                }).build()

    }

    fun showCity(view: View){

        CityDialog.Builder(this)
                .setTitle("选择城市")
                .setSelect("北京"  , "朝阳区")
                .setCityListener(object : CityDialog.DialogCityListener{
                    override fun onDialogCityBack(province: String, city: String) {
                        toast(province + city)
                    }

                }).build()

    }

}
