package com.zs.pickerview.util

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.zs.pickerview.model.CityModel
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * @Author: zs
 * @Date: 2019-07-01 15:18
 *
 * @Description:
 */
class CityHandler{

    companion object {

        private var mCityList: MutableList<CityModel>? = null

        fun getCityList(context: Context): MutableList<CityModel>{

            if (mCityList == null){
                var inputStreamReader: InputStreamReader? = null
                try {
                    inputStreamReader = InputStreamReader(context.assets.open("citys.json"))
                    var cityBuilder = StringBuilder()
                    BufferedReader(inputStreamReader).use {
                        var line: String
                        while (true) {
                            line = it.readLine()?: break
                            cityBuilder.append(line)
                        }
                    }
                    inputStreamReader?.close()
                    mCityList = Gson().fromJson(cityBuilder.toString() , object: TypeToken<MutableList<CityModel>>(){}.type)
                    mCityList?.let{
                        return it
                    }
                } catch (e: Exception) {

                } finally {
                    inputStreamReader?.close()
                }
                return mutableListOf()
            }else{
                return mCityList!!
            }

        }

    }

}