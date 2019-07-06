package com.zs.pickerview.dialog

import android.content.Context
import android.view.View
import com.zs.pickerview.R
import com.zs.pickerview.dialog.base.CommonFragmentDialog
import com.zs.pickerview.extension.addZero
import com.zs.pickerview.extension.deleteZero
import com.zs.pickerview.extension.lastSubstring
import com.zs.pickerview.util.CommonViewUtils
import kotlinx.android.synthetic.main.dialog_picker_layout.view.*
import java.util.*
import kotlin.collections.ArrayList


/**
 * @Author: zs
 * @Date: 2019-06-12 14:08
 *
 * @Description: 选择日期dialog
 */
class DateDialog{

    private var mContext: Context? = null
    private var mMonthDays: Int = 30
    private var mYear: String? = null
    private var mMonth: String? = null
    private var mDay: String? = null

    private lateinit var mView: View

    private lateinit var mYearData:ArrayList<String>
    private lateinit var mMonthData: ArrayList<String>
    private lateinit var mDayData:ArrayList<String>

    constructor(mContext: Context? , builder: Builder) {
        this.mContext = mContext
        initDialog(builder)
    }

    private fun initDialog(builder: Builder){

        mView = CommonViewUtils.newInstance(mContext, R.layout.dialog_picker_layout)

        mYear = mContext?.resources?.getString(R.string.txt_year)
        mMonth = mContext?.resources?.getString(R.string.txt_month)
        mDay = mContext?.resources?.getString(R.string.txt_day)

        var calendar = Calendar.getInstance()
        var currentYear = calendar.get(Calendar.YEAR)
        var currentMonth = calendar.get(Calendar.MONTH) + 1
        var currentDay = calendar.get(Calendar.DAY_OF_MONTH)

        mYearData = ArrayList()
        for (year in builder.startYear..currentYear){
            mYearData.add("$year$mYear")
        }
        mMonthData = ArrayList()
        for (month in 1..12){
            mMonthData.add("$month$mMonth")
        }
        mDayData = ArrayList()
        mMonthDays = getMonthOfDay(builder.defaultYear , builder.defaultMonth)
        for (day in 1..mMonthDays){
            mDayData.add("$day$mDay")
        }

        mView?.apply {

            picker_tv_title?.text = builder?.title

            picker_wheel_1?.setData(mYearData)
            picker_wheel_2?.setData(mMonthData)
            picker_wheel_3?.setData(mDayData)

            var date = builder?.selectDate?.split("-")
            if (date == null || date.size < 3){
                picker_wheel_1?.setDefaultString("${builder.defaultYear}$mYear")
                picker_wheel_2?.setDefault(builder.defaultMonth - 1)
                picker_wheel_3?.setDefault(builder.defaultDay - 1)
            }else{
                picker_wheel_1?.setDefaultString(date[0] + mYear)
                picker_wheel_2?.setDefaultString(date[1].deleteZero() + mMonth)
                picker_wheel_3?.setDefaultString(date[2].deleteZero() + mDay)

                updateDay(picker_wheel_1.selectedText , picker_wheel_2.selectedText)
            }

            var dialog = CommonFragmentDialog.Builder(mContext)
                    .setContentView(mView)
                    .setCancelable(true)
                    .setDialogBgResId(R.drawable.shape_transparent_bg)
                    .build()

            dialog?.setShowAtBottom()?.showMatchWidth()
            picker_wheel_1?.setOnSelectListener { _, text ->

                var monthString = picker_wheel_2.selectedText
                updateDay(text , monthString)

            }

            picker_wheel_2?.setOnSelectListener { _, text ->

                var yearString = picker_wheel_1.selectedText
                updateDay(yearString , text)

            }

            picker_tv_cancel?.setOnClickListener {
                dialog.dismiss()
            }

            picker_tv_complete?.setOnClickListener {

                var yearString = picker_wheel_1.selectedText
                var monthString = picker_wheel_2.selectedText
                var dayString = picker_wheel_3.selectedText

                var date = yearString.lastSubstring() + "-" + monthString.lastSubstring().addZero() + "-" + dayString.lastSubstring().addZero()
                builder?.listener?.onDialogDateBack(date)
                dialog.dismiss()
            }
        }

    }

    /**
     * 更新天数
     */
    private fun updateDay(yearString: String , monthString: String){
        var year = yearString.lastSubstring().toInt()
        var month = monthString.lastSubstring().toInt()
        mMonthDays = getMonthOfDay(year , month)
        mDayData.clear()
        for (day in 1..mMonthDays){
            mDayData.add("$day$mDay")
        }
        var index = mView?.picker_wheel_3.selected
        mView?.picker_wheel_3.resetData(mDayData)
        if(index > mDayData.size - 1){
            mView?.picker_wheel_3.setDefault(mDayData.size - 1)
        }else{
            mView?.picker_wheel_3.setDefault(index)
        }
    }


    /**
     * 获取月的天数
     */
    private fun getMonthOfDay(year: Int, month: Int): Int {
        return when (month) {
            1, 3, 5, 7, 8, 10, 12 -> 31
            4, 6, 9, 11 -> 30
            2 -> if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
                29
            } else {
                28
            }
            else -> 30
        }
    }

    interface DialogDateListener{
        fun onDialogDateBack(date: String)
    }

    class Builder(val context: Context) {

        var title: String? = null

        var startYear: Int = 1970

        var defaultYear: Int = 1970

        var defaultMonth: Int = 1

        var defaultDay: Int = 1

        var selectDate: String? = null

        var listener: DialogDateListener? = null

        fun setTitle(title: String?): Builder{
            this.title = title
            return this
        }

        fun setStartYear(year: Int): Builder{
            this.startYear = year
            return this
        }

        fun setDefaultDate(year: Int , month: Int , day: Int): Builder{
            this.defaultYear = year
            this.defaultMonth = month
            this.defaultDay = day
            return this
        }

        fun setSelectDate(date: String?): Builder{
            this.selectDate = date
            return this
        }

        fun setDateListener(listener: DialogDateListener?): Builder{
            this.listener = listener
            return this
        }

        fun build(): DateDialog {
            return DateDialog(context , this)
        }

    }


}