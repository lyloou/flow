package com.lyloou.flow.repository.schedule

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

const val TABLE_SCHEDULE = "schedule"
const val COL_SCHEDULE_ID = "id"
const val COL_SCHEDULE_UUID = "uuid"
const val COL_SCHEDULE_USER_ID = "user_id"
const val COL_SCHEDULE_TITLE = "title"
const val COL_SCHEDULE_CONTENT = "content"
const val COL_SCHEDULE_A = "a"
const val COL_SCHEDULE_B = "b"
const val COL_SCHEDULE_C = "c"
const val COL_SCHEDULE_D = "d"
const val COL_SCHEDULE_SYNC_TIME = "sync_time"
const val COL_SCHEDULE_LOCAL_TIME = "local_time"
const val COL_SCHEDULE_SNAP_TIME = "snap_time"
const val COL_SCHEDULE_IS_DISABLED = "is_disabled"


@Entity(tableName = TABLE_SCHEDULE, indices = [Index(value = ["uuid"], unique = true)])
data class DbSchedule(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COL_SCHEDULE_ID)
    @SerializedName("_id")
    val id: Int,

    @ColumnInfo(name = COL_SCHEDULE_UUID)
    @SerializedName("uuid")
    var uuid: String,

    @ColumnInfo(name = COL_SCHEDULE_USER_ID)
    @SerializedName("user_id")
    var userId: Long,

    @ColumnInfo(name = COL_SCHEDULE_TITLE)
    @SerializedName("title")
    var title: String,

    @ColumnInfo(name = COL_SCHEDULE_CONTENT)
    @SerializedName("content")
    var content: String?,

    @ColumnInfo(name = COL_SCHEDULE_A)
    @SerializedName("a")
    var a: String?,

    @ColumnInfo(name = COL_SCHEDULE_B)
    @SerializedName("b")
    var b: String?,

    @ColumnInfo(name = COL_SCHEDULE_C)
    @SerializedName("c")
    var c: String?,

    @ColumnInfo(name = COL_SCHEDULE_D)
    @SerializedName("d")
    var d: String?,

    // 本地更新时间
    @ColumnInfo(name = COL_SCHEDULE_LOCAL_TIME)
    @SerializedName("local_time")
    var localTime: Long,

    // 快照时间（远程同步到本地时的 sync_time）
    @ColumnInfo(name = COL_SCHEDULE_SNAP_TIME)
    @SerializedName("snap_time")
    var snapTime: Long = 0,

    // 远程时间（当从网上下载时，会赋值此项）
    @ColumnInfo(name = COL_SCHEDULE_SYNC_TIME)
    @SerializedName("sync_time")
    var syncTime: Long = 0,

    @ColumnInfo(name = COL_SCHEDULE_IS_DISABLED)
    @SerializedName("disabled")
    var isDisabled: Boolean = false

) {
    override fun equals(other: Any?): Boolean = if (other is DbSchedule) {
        other.uuid == uuid
    } else {
        false
    }

    override fun hashCode(): Int {
        return uuid.hashCode()
    }
}

fun DbSchedule.backup() {

}