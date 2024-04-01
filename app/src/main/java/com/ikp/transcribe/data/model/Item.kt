package com.ikp.transcribe.data.model

import com.squareup.moshi.Json

data class Item(
    val name : String,
    @Json(name = "qty")
    val quantity : Int,
    val price : Double
)

data class Items(
    val items : List<Item>
)
