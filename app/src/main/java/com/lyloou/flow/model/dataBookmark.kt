package com.lyloou.flow.model

data class Bookmark(
    /**
     * 标题
     */
    var title: String,
    /**
     * 网址
     */
    var url: String,
    /**
     * 优先级
     */
    var order: Int = 0,
    /**
     * 标签
     */
    var tag: String? = null,
    /**
     * 描述
     */
    var desc: String? = null
)

fun Bookmark.comparator(): Comparator<Bookmark> {
    return compareBy({ it.order }, { it.tag })
}