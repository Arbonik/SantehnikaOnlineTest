package com.arbonik.santehnikaonlinetest

import android.app.Application
import com.yandex.mapkit.MapKitFactory

class SantehnikaApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey("6eb25f36-0b14-42d2-8f9b-d50cc9e2242b")

    }
}