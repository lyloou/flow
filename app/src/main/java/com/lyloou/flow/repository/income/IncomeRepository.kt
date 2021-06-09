package com.lyloou.flow.repository.income

import com.google.gson.reflect.TypeToken
import com.lyloou.flow.App
import com.lyloou.flow.model.Income
import com.lyloou.flow.model.gson
import com.lyloou.flow.model.toJsonString
import java.io.File
import java.io.FileReader
import java.io.FileWriter

object IncomeRepository {
    private const val FILE_NAME = "income.json"
    private val file = File(App.instance.filesDir, FILE_NAME)
    private val defaultIncomeList = listOf(
        Income(
            name = "收益宝4号",
            code = "GIUIYT",
            dateOfBegin = "2021年5月27日",
            priceOfBegin = 6685.41,
            priceOfEndIntervalStart = 6600.00,
            priceOfEndIntervalEnd = 6900.00,
            rateOfTapOut = 0.054,
            rateOfPriceOfTapOut = 1.1
        )
    )

    fun getIncomeList(): List<Income> {
        val type = object : TypeToken<List<Income>>() {}.type
        if (file.exists()) {
            FileReader(file).use {
                return gson.fromJson(it, type) ?: defaultIncomeList
            }
        }
        return defaultIncomeList
    }

    fun recoverDefault() {
        saveIncomeList(defaultIncomeList)
    }

    fun saveIncomeList(list: List<Income>) {
        if (!file.exists()) {
            file.createNewFile()
        }

        FileWriter(file).use {
            it.write(list.toJsonString())
            it.flush()
        }
    }
}