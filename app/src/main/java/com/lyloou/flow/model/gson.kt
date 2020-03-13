package com.lyloou.flow.model

import com.google.gson.GsonBuilder

val gson = GsonBuilder()
    .setLenient()
    .create()