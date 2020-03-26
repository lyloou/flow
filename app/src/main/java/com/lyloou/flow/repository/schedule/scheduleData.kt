package com.lyloou.flow.repository.schedule

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.lyloou.flow.model.Schedule
import com.lyloou.flow.model.ScheduleHelper

const val TABLE_SCHEDULE = "schedule"
const val COL_SCHEDULE_ID = "id"
const val COL_SCHEDULE_USER_ID = "user_id"
const val COL_SCHEDULE_TITLE = "title"
const val COL_SCHEDULE_CONTENT = "content"
const val COL_SCHEDULE_GMT_CREATE = "gmt_create"

@Entity(tableName = TABLE_SCHEDULE)
data class DbSchedule(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COL_SCHEDULE_ID)
    @SerializedName("id")
    val id: Int,

    @ColumnInfo(name = COL_SCHEDULE_USER_ID)
    @SerializedName("user_id")
    var userId: Long,

    @ColumnInfo(name = COL_SCHEDULE_TITLE)
    @SerializedName("title")
    val title: String,

    @ColumnInfo(name = COL_SCHEDULE_CONTENT)
    @SerializedName("content")
    var content: String?,

    @ColumnInfo(name = COL_SCHEDULE_GMT_CREATE)
    @SerializedName("gmt_create")
    var gmtCreate: String
)


fun DbSchedule.toSchedule(): Schedule {
    val s = ScheduleHelper.fromJson(content)
    return Schedule(title, s.a, s.b, s.c, s.d)
}
