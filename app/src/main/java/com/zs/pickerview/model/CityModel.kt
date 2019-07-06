package com.zs.pickerview.model

/**
 * @Author: zs
 * @Date: 2019-07-01 15:47
 *
 * @Description:
 */
class CityModel{

    var name: String? = ""
    var child = mutableListOf<City>()

    inner class City{
        var name: String? = ""
    }

}
