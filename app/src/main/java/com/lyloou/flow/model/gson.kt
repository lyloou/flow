package com.lyloou.flow.model

import com.google.gson.Gson
import com.google.gson.GsonBuilder

val gson: Gson = GsonBuilder()
    .setLenient()
    .create()