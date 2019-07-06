package com.zs.pickerview.dialog

import android.content.Context
import android.view.View
import com.zs.pickerview.R
import com.zs.pickerview.dialog.base.CommonFragmentDialog
import com.zs.pickerview.model.CityModel
import com.zs.pickerview.util.CityHandler
import com.zs.pickerview.util.CommonViewUtils
import kotlinx.android.synthetic.main.dialog_picker_layout.view.*
import java.util.*


/**
 * @Author: zs
 * @Date: 2019-06-12 14:08
 *
 * @Description: 选择省市 dialog
 */
class CityDialog{

    private var mContext: Context? = null
    private lateinit var mView: View
    private lateinit var mCityData: MutableList<CityModel>

    private lateinit var mProvince: ArrayList<String>
    private lateinit var mCity: ArrayList<String>

    constructor(context: Context? , builder: Builder) {
        this.mContext = context
        initDialog(builder)
    }

    private fun initDialog(builder: Builder){

        mView = CommonViewUtils.newInstance(mContext, R.layout.dialog_picker_layout)

        mContext?.let {
            mCityData = CityHandler.getCityList(it)
        }?: return

        mProvince = ArrayList()
        var provinceIndex = 0
        for ((index , province) in mCityData.withIndex()){
            province.name?.let {
                mProvince.add(it)
                if (builder.selectProvince == province.name){
                    provinceIndex = index
                }
            }
        }
        mCity = ArrayList()
        updateCity(provinceIndex)

        mView?.apply {

            picker_tv_title?.text = builder.title

            picker_wheel_1?.setData(mProvince)
            picker_wheel_1?.setDefault(provinceIndex)
            picker_wheel_2?.setData(mCity)
            picker_wheel_2?.setDefaultString(builder.selectCity)
            picker_wheel_3?.visibility = View.GONE

            var dialog = CommonFragmentDialog.Builder(mContext)
                    .setContentView(mView)
                    .setCancelable(true)
                    .setDialogBgResId(R.drawable.shape_transparent_bg)
                    .build()

            dialog?.setShowAtBottom()?.showMatchWidth()

            picker_wheel_1?.setOnSelectListener { id, text ->
                updateCity(id)
            }

            picker_tv_cancel?.setOnClickListener {
                dialog.dismiss()
            }

            picker_tv_complete?.setOnClickListener {

                builder.listener?.onDialogCityBack(picker_wheel_1.selectedText , picker_wheel_2.selectedText)
                dialog.dismiss()
            }
        }

    }

    private fun updateCity(index: Int){
        mCity.clear()
        var citys = mCityData[index]
        for (city in citys.child){
            city.name?.let {
                mCity.add(it)
            }
        }
        mView?.picker_wheel_2.resetData(mCity)
        mView?.picker_wheel_2.setDefault(0)
    }

    interface DialogCityListener{
        fun onDialogCityBack(province: String, city: String)
    }

    class Builder(val context: Context) {

        var title: String? = null

        var selectProvince: String? = null

        var selectCity: String? = null

        var listener: CityDialog.DialogCityListener? = null

        fun setTitle(title: String?): Builder{
            this.title = title
            return this
        }

        fun setSelect(province: String? , city: String?): Builder{
            this.selectProvince = province
            this.selectCity = city
            return this
        }

        fun setCityListener(listener: CityDialog.DialogCityListener?): Builder{
            this.listener = listener
            return this
        }

        fun build(): CityDialog {
            return CityDialog(context , this)
        }

    }


}