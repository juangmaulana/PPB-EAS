package com.example.eas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.eas.ui.navigation.AppNavGraph
import com.example.eas.ui.theme.CoffeeBlissTheme
import com.example.eas.ui.viewmodel.CoffeeShopViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val app = application as CoffeeBlissApp
        val viewModelFactory = CoffeeShopViewModel.factory(app.coffeeShopRepository)
        setContent {
            CoffeeBlissTheme {
                val coffeeShopViewModel: CoffeeShopViewModel = viewModel(factory = viewModelFactory)
                AppNavGraph(viewModel = coffeeShopViewModel)
            }
        }
    }
}
