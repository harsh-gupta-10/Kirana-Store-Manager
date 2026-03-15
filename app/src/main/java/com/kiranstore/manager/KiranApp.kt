package com.kiranstore.manager

import android.app.Application
import com.kiranstore.manager.data.database.KiranDatabase
import com.kiranstore.manager.data.database.SeedData
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class KiranApp : Application() {

    @Inject
    lateinit var database: KiranDatabase

    override fun onCreate() {
        super.onCreate()

        // ⚠️ Enable the line below ONLY for first-run demo/testing
        // Remove before production release
        // CoroutineScope(Dispatchers.IO).launch { SeedData.populateSeedData(database) }
    }
}
