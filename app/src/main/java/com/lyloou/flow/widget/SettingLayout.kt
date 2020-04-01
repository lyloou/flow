package com.lyloou.flow.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.util.AttributeSet
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Space
import android.widget.TextView
import com.lyloou.flow.R
import com.lyloou.flow.util.Uscreen

class SettingLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attrs, defStyle) {

    private val items: SparseArray<Item> = SparseArray()
    private val itemViews: SparseArray<View> = SparseArray()

    fun addItem(item: Item): SettingLayout {
        require(itemViews[item.titleStrId] == null) { "此Item项已存在，请不要重复添加" }

        // 从布局中加载得到视图；
        @SuppressLint("InflateParams")
        val v = LayoutInflater.from(context).inflate(R.layout.view_setting_layout, null)

        addView(v, LayoutParams(LayoutParams.MATCH_PARENT, Uscreen.dp2Px(context, 48f)))

        // 添加到SparseArray中，便于后期进行其他操作；
        itemViews.put(item.titleStrId, v)
        items.put(item.titleStrId, item)
        refreshItem(item)
        return this
    }

    fun refreshItem(item: Item): SettingLayout {
        val v = getItemView(item) ?: throw IllegalArgumentException("尚未添加此Item, 请先通过addItem方法添加")

        // 根据数据初始化视图
        if (item.iconResId != 0) {
            val ivIcon = v.findViewById<ImageView>(R.id.iv_set_icon)
            ivIcon.visibility = View.VISIBLE
            ivIcon.setImageResource(item.iconResId)
        }

        val tvTitle = v.findViewById<TextView>(R.id.tv_set_title)
        tvTitle.setText(item.titleStrId)
        if (!TextUtils.isEmpty(item.contentStr)) {
            val tvContent = v.findViewById<TextView>(R.id.tv_set_content)
            tvContent.visibility = View.VISIBLE
            tvContent.text = item.contentStr
        }

        if (!TextUtils.isEmpty(item.unitStr)) {
            val tvUnit = v.findViewById<TextView>(R.id.tv_set_unit)
            tvUnit.visibility = View.VISIBLE
            tvUnit.text = item.unitStr
        }

        if (item.hasToRight) {
            val ivToRight = v.findViewById<ImageView>(R.id.iv_set_to_right)
            ivToRight.visibility = View.VISIBLE
        }

        val vDivider = v.findViewById<View>(R.id.v_set_divider)
        if (item.sep == SEP.FILL) {
            vDivider.visibility = View.VISIBLE
        } else if (item.sep == SEP.AFTERICON) {
            vDivider.visibility = View.VISIBLE
            val mp = vDivider.layoutParams as MarginLayoutParams
            mp.leftMargin = Uscreen.dp2Px(context, 48f)
        }

        if (item.listener != null) {
            v.setOnClickListener { item.listener!!.invoke(item) }
        }
        return this
    }

    fun addCustomView(view: View): SettingLayout {
        addView(view)
        return this
    }

    fun addSpace(spaceHeight: Int): SettingLayout {
        val space = Space(context)
        space.layoutParams = ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, spaceHeight)
        addView(space)
        return this
    }

    fun addHeadTips(strId: Int): SettingLayout {
        return addHeadTips(context.resources.getString(strId))
    }

    fun addHeadTips(tips: String?): SettingLayout {
        val tvTips = TextView(context)
        val params = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        params.leftMargin = Uscreen.dp2Px(context, 16f)
        params.bottomMargin = Uscreen.dp2Px(context, 8f)
        tvTips.layoutParams = params
        tvTips.text = tips
        tvTips.setTextColor(Color.DKGRAY)
        tvTips.textSize = 12f
        addView(tvTips)
        return this
    }

    fun getItem(strId: Int): Item {
        return items[strId]
    }

    fun getItemView(item: Item): View? {
        return getItemView(item.titleStrId)
    }

    /**
     * 通过Title的字符串资源ID来获取对应的View
     *
     * @param titleStrId 字符串资源ID
     * @return 对应的Item视图
     */
    fun getItemView(titleStrId: Int): View? {
        return itemViews[titleStrId]
    }

    interface IClickListener {
        operator fun invoke(item: Item)
    }

    /**
     * NO 表示不显示底部分割线
     * FILL 表示占满整个底部的分割线
     * AFTERICON 表示紧随图像的分割线
     */
    enum class SEP {
        NO, FILL, AFTERICON
    }

    data class Item(
        var titleStrId: Int,
        var iconResId: Int = 0,
        var contentStr: String? = null,
        var unitStr: String? = null,
        var hasToRight: Boolean = true,
        var sep: SEP = SEP.FILL,
        var listener: IClickListener? = null
    )

}