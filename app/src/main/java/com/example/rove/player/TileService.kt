package com.example.rove.player

import android.content.Intent
import android.os.Build
import android.service.quicksettings.TileService
import androidx.annotation.RequiresApi
import com.example.rove.GoConstants
import com.example.rove.ui.MainActivity

@RequiresApi(Build.VERSION_CODES.N)
class PlayerTileService : TileService() {

    override fun onClick() {
        super.onClick()
        with(Intent(this, MainActivity::class.java)) {
            putExtra(GoConstants.LAUNCHED_BY_TILE, GoConstants.LAUNCHED_BY_TILE)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivityAndCollapse(this)
        }
    }
}
