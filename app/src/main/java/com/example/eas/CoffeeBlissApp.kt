package com.example.eas

import android.app.Application
import com.example.eas.data.AppDatabase
import com.example.eas.data.repository.CoffeeShopRepository

class CoffeeBlissApp : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
    val coffeeShopRepository by lazy { CoffeeShopRepository(database) }
}
