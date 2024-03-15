package com.ikp.transcribe.data.table

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Transaction (
    @PrimaryKey(autoGenerate = true) var id : Int? = null,
    @ColumnInfo(name = "email") var email : String?,
    @ColumnInfo(name = "judul") var judul : String?,
    @ColumnInfo(name = "kategori") var kategori : String?,
    @ColumnInfo(name = "nominal") var nominal : Int? = 0,
    @ColumnInfo(name = "lokasi") var lokasi : String?
)