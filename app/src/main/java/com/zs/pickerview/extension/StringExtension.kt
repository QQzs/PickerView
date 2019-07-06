package com.zs.pickerview.extension

/**
 * @Author: zs
 * @Date: 2019-05-22 11:53
 * @Description: String 扩展
 */

/**
 * 截取最后一个字符之前的内容
 */
fun String.lastSubstring(): String{
    return if (this.isNullOrEmpty()){
        ""
    }else{
        this.substring(0 , this.length - 1)
    }
}

/**
 * 不够补0
 */
fun String.addZero(): String{
    return if (this.length == 1){
        "0$this"
    }else{
        this
    }
}

/**
 * 删除0
 */
fun String.deleteZero(): String{
    return if (this.startsWith("0")){
        this.substring(1)
    }else{
        this
    }
}
