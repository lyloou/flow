package com.lyloou.flow.ui.income

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.lyloou.flow.R
import com.lyloou.flow.common.BaseCompatActivity
import com.lyloou.flow.common.toast
import com.lyloou.flow.extension.dp2px
import com.lyloou.flow.extension.snackbar
import com.lyloou.flow.model.Income
import com.lyloou.flow.util.Uapp
import com.lyloou.flow.util.Udialog
import com.lyloou.flow.util.Usystem
import com.lyloou.flow.widget.ItemOffsetDecoration
import com.lyloou.flow.widget.ToolbarManager
import kotlinx.android.synthetic.main.activity_income.*

class IncomeActivity : BaseCompatActivity(), ToolbarManager, IncomeAdapter.OnItemListener {

    lateinit var viewModel: IncomeViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_income)
        viewModel = ViewModelProvider(this).get(IncomeViewModel::class.java)

        initView()
        viewModel.data.observe(this, Observer {
            it?.let { data ->
                (rvIncome.adapter as IncomeAdapter).let { adapter ->
                    adapter.list = data.toMutableList()
                    adapter.notifyDataSetChanged()
                }
            }
        })
    }

    override fun onStart() {
        super.onStart()
        viewModel.reload()
    }

    private fun initView() {
        setSupportActionBar(toolbar)
        enableHomeAsUp { onBackPressed() }
        whiteToolbarText()

        collapsing_toolbar_layout.setExpandedTitleColor(Color.TRANSPARENT)
        collapsing_toolbar_layout.setCollapsedTitleTextColor(Color.WHITE)

        rvIncome.layoutManager = LinearLayoutManager(this)
        rvIncome.addItemDecoration(ItemOffsetDecoration(dp2px(16f)))
        rvIncome.adapter = IncomeAdapter().apply { onItemListener = this@IncomeActivity }
    }

    override val toolbar: Toolbar
        get() = myToolbar

    override fun onItemClicked(income: Income) {
        val intent = Intent(context, IncomeDetailActivity::class.java)
        intent.putExtra(IncomeDetailActivity.CODE, income.code)
        startActivity(intent)
    }

    override fun onItemMoreClicked(income: Income) {
        Udialog.AlertMultiItem.builder(this)
            .add("编辑基本信息") { showIncomeBasicEditDialog(income) }
            .add("编辑价格信息") { showIncomePriceEditDialog(income) }
            .add("删除") { deleteIncome(income) }
            .show()
    }


    private fun showIncomeBasicEditDialog(income: Income? = null) {
        Udialog.AlertMultipleInputDialog.builder(this)
            .addInputItem(Udialog.AlertMultipleInputDialog.InputItem().apply {
                this.id = 1
                this.hint = "产品名称"
                this.defaultValue = income?.name
            })
            .addInputItem(Udialog.AlertMultipleInputDialog.InputItem().apply {
                this.id = 2
                this.hint = "产品代码"
                this.defaultValue = income?.code
            })
            .addInputItem(Udialog.AlertMultipleInputDialog.InputItem().apply {
                this.id = 3
                this.hint = "期初观察日"
                this.defaultValue = income?.dateOfBegin
            })
            .addInputItem(Udialog.AlertMultipleInputDialog.InputItem().apply {
                this.id = 4
                this.hint = "优先级"
                this.defaultValue = income?.order?.toString() ?: "0"
                this.type = EditorInfo.TYPE_CLASS_NUMBER
            })
            .addInputItem(Udialog.AlertMultipleInputDialog.InputItem().apply {
                this.id = 5
                this.hint = "标签"
                this.defaultValue = income?.tag
            })
            .addInputItem(Udialog.AlertMultipleInputDialog.InputItem().apply {
                this.id = 6
                this.hint = "描述"
                this.defaultValue = income?.desc
            })
            .positiveConsumer {

                if (it[1].isNullOrEmpty() || it[2].isNullOrEmpty()) {
                    showIncomePriceEditDialog(income)
                    toast("标题和网址不能为空")
                    return@positiveConsumer
                }

                if (income == null) {
                    viewModel.addIncome(
                        Income(
                            name = it[1]!!,
                            code = it[2]!!,
                            dateOfBegin = it[3]!!,
                            order = if (it[4].isNullOrEmpty()) 0 else it[4]!!.toInt(),
                            tag = it[5]!!,
                            desc = it[6]!!
                        )
                    )
                    viewModel.reload()
                    return@positiveConsumer
                }

                income.name = it[1]!!
                income.code = it[2]!!
                income.dateOfBegin = it[3]!!
                income.order = if (it[4].isNullOrEmpty()) 0 else it[4]!!.toInt()
                income.tag = it[5]!!
                income.desc = it[6]!!

                viewModel.updateIncome()
                viewModel.reload()
            }
            .show()
    }

    private fun showIncomePriceEditDialog(income: Income? = null) {
        Udialog.AlertMultipleInputDialog.builder(this)
            .addInputItem(Udialog.AlertMultipleInputDialog.InputItem().apply {
                this.id = 1
                this.hint = "期初观察价格"
                this.defaultValue = income?.priceOfBegin.toString()
            })
            .addInputItem(Udialog.AlertMultipleInputDialog.InputItem().apply {
                this.id = 2
                this.hint = "敲出价格比率"
                this.defaultValue = income?.rateOfPriceOfTapOut.toString()
            })
            .addInputItem(Udialog.AlertMultipleInputDialog.InputItem().apply {
                this.id = 3
                this.hint = "敲出后固定收益"
                this.defaultValue = income?.rateOfTapOut.toString()
            })
            .addInputItem(Udialog.AlertMultipleInputDialog.InputItem().apply {
                this.id = 4
                this.hint = "期末价格区间开始"
                this.defaultValue = income?.priceOfEndIntervalStart.toString()
            })
            .addInputItem(Udialog.AlertMultipleInputDialog.InputItem().apply {
                this.id = 5
                this.hint = "期末价格区间结束"
                this.defaultValue = income?.priceOfEndIntervalEnd.toString()
            })
            .positiveConsumer {

                if (maybeNull(it)) {
                    showIncomePriceEditDialog(income)
                    toast("所有项不能为空")
                    return@positiveConsumer
                }

                if (income == null) {
                    toast("无效的income")
                    return@positiveConsumer
                }

                income.priceOfBegin = it[1]!!.toDouble()
                income.rateOfPriceOfTapOut = it[2]!!.toDouble()
                income.rateOfTapOut = it[3]!!.toDouble()
                income.priceOfEndIntervalStart = it[4]!!.toDouble()
                income.priceOfEndIntervalEnd = it[5]!!.toDouble()

                viewModel.updateIncome()
                viewModel.reload()
            }
            .show()
    }

    private fun maybeNull(map: Map<Int, String>): Boolean {
        for (key in map.keys) {
            if (map[key].isNullOrEmpty()) {
                return true
            }
        }
        return false
    }

    private fun deleteIncome(income: Income) {
        viewModel.deleteIncome(income)
        viewModel.reload()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.income, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun addShortcut() {
        Uapp.addShortCutCompat(
            this,
            IncomeActivity::class.java.canonicalName,
            "income",
            R.mipmap.road,
            resources.getString(R.string.income)
        )
        val snackbar: Snackbar = snackbar("已添加到桌面", Snackbar.LENGTH_LONG)
        snackbar.setAction("添加失败？去授权") {
            Usystem.toAppSetting(this)
        }
        snackbar.show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_shortcut -> {
                addShortcut()
            }
            R.id.add -> {
                showIncomeBasicEditDialog()
            }
            R.id.recover -> {
                recoverIncome()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun recoverIncome() {
        viewModel.recoverIncome()
        viewModel.reload()
    }
}
