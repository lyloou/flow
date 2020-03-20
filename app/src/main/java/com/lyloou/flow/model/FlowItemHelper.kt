package com.lyloou.flow.model

import com.google.gson.reflect.TypeToken

object FlowItemHelper {
    private val type = object :
        TypeToken<List<FlowItem?>?>() {}.type

    fun toJsonArray(items: List<FlowItem?>?): String {
        return if (items == null) {
            "[]"
        } else try {
            gson.toJson(items, type)
        } catch (e: Exception) {
            "[]"
        }
    }

    fun fromJsonArray(items: String?): MutableList<FlowItem> {
        return gson.fromJson(items, type)
    }

    fun toPrettyText(items: String?): String {
        return toPrettyText(fromJsonArray(items))
    }

    fun toPrettyText(items: List<FlowItem>?): String {
        if (items == null) {
            return ""
        }
        val sb = StringBuilder()
        for (item in items) {
            sb.append("\n")
                .append(item.timeStart)
                .append(item.timeSep)
                .append(item.timeEnd)
                .append("\t")
                .append("\t")
                .append(item.spend)
                .append("\n")
                .append(item.content)
                .append("\n")
        }
        return sb.toString()
    }
}