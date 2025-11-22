package com.app.eshopdaraja

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.app.eshopdaraja.include.SimpleLightTopAppBar
import com.app.eshopdaraja.model.Products
import com.app.eshopdaraja.mvvm.ProductViewModel
import com.app.composedemo.utlis.ViewState
import com.app.eshopdaraja.auth.Login
import com.app.eshopdaraja.auth.Register
import com.app.eshopdaraja.fragments.HomeScreen
import com.app.eshopdaraja.fragments.ProfileScreen
import com.app.eshopdaraja.include.BottomNavItem
import com.app.eshopdaraja.include.SimpleBottomBar
import com.app.eshopdaraja.mvvm.CartScreen
import com.app.eshopdaraja.mvvm.CartViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: ProductViewModel by viewModels()
    private val cartViewModel: CartViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    HomePageUI(viewModel,cartViewModel)
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
    @Composable
    private fun HomePageUI(viewModel: ProductViewModel,
                           cartViewModel: CartViewModel) {
        val navController =  rememberNavController()

        Scaffold(topBar = {
            SimpleLightTopAppBar("eShop Daraja")
        },

        bottomBar = {
            SimpleBottomBar(navController)
        }
        ) { paddingValues ->

            NavHost(
                navController = navController,
                startDestination =  BottomNavItem.Home.route,
                //modifier = Modifier.padding(paddingValues)
            ){
                composable(BottomNavItem.Home.route) { HomeScreen(navController,viewModel,paddingValues) }
                composable(BottomNavItem.Cart.route) { CartScreen(navController,viewModel,cartViewModel, paddingValues) }
                composable(BottomNavItem.Notifications.route) { HomeScreen(navController,viewModel,paddingValues) }
                composable(BottomNavItem.Profile.route) { ProfileScreen(navController, firstName = "John","Victor",paddingValues=paddingValues) }

                composable("login") { Login(navController) {email,password -> } }
                composable("register") { Register(navController) {firstName,lastName, email,password -> } }
                composable("product/{id}") { backStackEntry ->

                    val id = backStackEntry.arguments?.getString("id")?.toInt() ?: 0
                    ProductDetailScreen(id,navController=navController)
                }


            }


        }

    }


}


