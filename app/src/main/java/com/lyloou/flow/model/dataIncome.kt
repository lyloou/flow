package com.lyloou.flow.model

import com.lyloou.flow.util.Ustr

data class Income(
    // =============> basic
    /**
     * 产品名称
     */
    var name: String,

    /**
     * 产品代码
     */
    var code: String,

    /**
     * 期初观察日
     */
    var dateOfBegin: String,
    /**
     * 优先级
     */
    var order: Int = 0,

    /**
     * 标签
     */
    var tag: String? = "默认",

    /**
     * 描述
     */
    var desc: String? = null,

    // =============> price
    /**
     * 期初观察价格
     */
    var priceOfBegin: Double = 0.0,
    /**
     * 敲出价格比率
     */
    var rateOfPriceOfTapOut: Double = 1.0,

    /**
     * 敲出后固定收益
     */
    var rateOfTapOut: Double = 0.0,

    /**
     * 期末价格开始
     */
    var priceOfEndIntervalStart: Double = 0.0,

    /**
     * 期末价格结束
     */
    var priceOfEndIntervalEnd: Double = 0.0,

    /**
     * 期末收益
     */
    var rateOfEnd: Double? = 0.0,

    /**
    实际收益
     */
    var rateOfActual: Double? = 0.0


) {
    override fun equals(other: Any?): Boolean {
        if (other === this) {
            return true
        }

        val its = other as? Income ?: return false
        return (its.code == this.code)
    }

    override fun hashCode(): Int {
        return code.hashCode()
    }

    fun first(): CharSequence {
        return "$name ($code)"
    }

    fun second(): CharSequence {
        return "期初观察日: $dateOfBegin"
    }

    fun third(): CharSequence {
        return "期初观察价格: ${Ustr.to2Decimal(priceOfBegin)}"
    }

    fun forth(): CharSequence {
        val priceOfTapOut = Ustr.to2Decimal(getPriceOfTapOut())
        return "敲出价格: ${priceOfTapOut} 敲出后固定收益: ${Ustr.toPercentStr(rateOfTapOut)}"
    }

    fun getPriceOfTapOut(): Double {
        return rateOfPriceOfTapOut * priceOfBegin
    }
}

fun incomeComparator(): Comparator<Income> {
    return compareBy({ -it.order }, { it.tag })
}