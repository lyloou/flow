package com.lyloou.flow.widget

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.lyloou.flow.extension.dp2px

// 原文链接：https://blog.csdn.net/danceinkeyboard/article/details/81742428
class TimeLineItemDecoration : ItemDecoration() {
    private val mCircleSize = 14 //圆圈的半径
    private val mPaint: Paint = Paint() //画笔
    private val mPaintSize = 6 //画笔宽度
    private val mPaintColor = "#B8B8B8" //画笔默认颜色
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val offset: Int = view.context.dp2px(32f)
        val rect = Rect(offset, 0, 0, 0) //使item从outRect中右移64dp
        outRect.set(rect)
    }

    override fun onDraw(
        canvas: Canvas,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.onDraw(canvas, parent, state)
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val left = child.left
            val top = child.top
            val bottom = child.bottom
            val right = child.right
            val params =
                child.layoutParams as RecyclerView.LayoutParams
            val bottomMargin = params.bottomMargin //防止在item根布局中设置marginTop和marginBottom
            val topMargin = params.topMargin
            val leftX = left / 2 //轴线的x轴坐标
            val height = child.height //item的高度，不包含Margin
            if (childCount == 1) {
                canvas.drawLine(
                    leftX.toFloat(),
                    top.toFloat(),
                    leftX.toFloat(),
                    bottom - height / 2.toFloat(),
                    mPaint
                )
            } else {
                if (i == 0) {
                    canvas.drawLine(
                        leftX.toFloat(),
                        top + height / 2.toFloat(),
                        leftX.toFloat(),
                        bottom + bottomMargin.toFloat(),
                        mPaint
                    )
                } else if (i == childCount - 1) {
                    canvas.drawLine(
                        leftX.toFloat(),
                        top - topMargin.toFloat(),
                        leftX.toFloat(),
                        bottom - height / 2.toFloat(),
                        mPaint
                    )
                } else {
                    canvas.drawLine(
                        leftX.toFloat(),
                        top - topMargin.toFloat(),
                        leftX.toFloat(),
                        bottom - height / 2.toFloat(),
                        mPaint
                    )
                    canvas.drawLine(
                        leftX.toFloat(),
                        top + height / 2.toFloat(),
                        leftX.toFloat(),
                        bottom + bottomMargin.toFloat(),
                        mPaint
                    )
                }
            }
            canvas.drawCircle(
                leftX.toFloat(),
                top + height / 2.toFloat(),
                mCircleSize.toFloat(),
                mPaint
            )
        }
    }

    init {
        mPaint.strokeWidth = mPaintSize.toFloat()
        mPaint.color = Color.parseColor(mPaintColor)
    }
}