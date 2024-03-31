package com.ikp.transcribe.data.model

data class Item(
    val name : String,
    val quantity : Int,
    val price : Int
)

data class Items(
    val items : List<Item>
)
