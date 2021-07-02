package com.example.mrfarmergrocer.models


import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Product (
    val title: String = "",
    val price: String = "",
    val description: String = "",
    val stock_amount: String = "",
    val image: String = "",
    var product_id: String ="",
    val category: String =""

): Parcelable