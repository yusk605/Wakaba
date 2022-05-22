package com.example.wakaba.room

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
@Parcelize
@Entity
data    class Wakaba(
    @PrimaryKey(autoGenerate = true)        val wakabaNo:Long,
    @ColumnInfo(name = "wakaba_title")      val wakabaTitle:String,
    @ColumnInfo(name = "wakaba_content")    val wakabaContent:String,
    @ColumnInfo(name = "wakaba_date")       val wakabaDate:String,
    @ColumnInfo(name = "wakaba_rating")     val wakabaRating:Int,
    @ColumnInfo(name = "check_flag")        var checkFlag:Int,
    @ColumnInfo(name = "stamp")             val timeStamp:String
):Parcelable