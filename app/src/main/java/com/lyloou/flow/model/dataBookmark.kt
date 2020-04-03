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
    val iconUrl: String? = null,
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
    var desc: String? = null
) {
    override fun equals(other: Any?): Boolean {
        if (other === this) {
            return true
        }

        if (other !is Bookmark) {
            return false
        }

        return (other.title == this.title && other.url == this.url)
    }

    override fun hashCode(): Int {
        return title.hashCode() + url.hashCode()
    }
}

fun bookmarkComparator(): Comparator<Bookmark> {
    return compareBy({ -it.order }, { it.tag })
}