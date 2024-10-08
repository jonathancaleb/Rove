package com.example.rove.utils

import android.os.Build

object Versioning {

    @JvmStatic
    fun isQ() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

    @JvmStatic
    fun isMarshmallow() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M

    @JvmStatic
    fun isTiramisu() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
}
