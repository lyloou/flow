package com.lyloou.flow.repository.schedule

import androidx.room.ColumnInfo
import androidx.room.Entity
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
const val COL_SCHEDULE_RSYNC_TIME = "rsync_time"
const val COL_SCHEDULE_IS_DISABLED = "is_disabled"


@Entity(tableName = TABLE_SCHEDULE)
data class DbSchedule(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COL_SCHEDULE_ID)
    @SerializedName("id")
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

    @ColumnInfo(name = COL_SCHEDULE_SYNC_TIME)
    @SerializedName("sync_time")
    var syncTime: Long,

    @ColumnInfo(name = COL_SCHEDULE_RSYNC_TIME)
    @SerializedName("rsync_time")
    var rsyncTime: Long = 0,

    @ColumnInfo(name = COL_SCHEDULE_IS_DISABLED)
    @SerializedName("is_disabled")
    var isDisabled: Boolean = false

)
