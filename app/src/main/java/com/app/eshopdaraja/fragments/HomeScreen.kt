package com.app.eshopdaraja.fragments

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.viewModels
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
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.app.composedemo.utlis.ViewState
import com.app.eshopdaraja.R
import com.app.eshopdaraja.include.SimpleBottomBar
import com.app.eshopdaraja.include.SimpleLightTopAppBar
import com.app.eshopdaraja.model.Products
import com.app.eshopdaraja.mvvm.ProductViewModel
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.app.eshopdaraja.include.BottomNavItem
import com.app.eshopdaraja.model.CartItem
import com.app.eshopdaraja.mvvm.CartViewModel

@ExperimentalMaterial3Api
@ExperimentalLayoutApi
@Composable
fun HomeScreen(navController: NavController,
               viewModel: ProductViewModel = hiltViewModel(),
               paddingValues: PaddingValues
               ) {

        val isConnected by viewModel.isConnected.collectAsState()
        val productsState by viewModel.products.collectAsState()
        val cartViewModel: CartViewModel = hiltViewModel()


    if (isConnected) {
            // Render UI for when connected
            when (productsState) {
                is ViewState.Success -> {
                    // Handle success state
                    val productList = (productsState as ViewState.Success).data
                    LazyVerticalGrid(modifier = Modifier
                        .padding(paddingValues)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                        columns = GridCells.Adaptive(140.dp),
                        content = {
                            items(productList.size) { index: Int ->
                                ProductCards(productList[index],cartViewModel,navController)
                            }
                        })
                }

                is ViewState.Error -> {
                    // Handle error state
                    val errorMessage = (productsState as ViewState.Error).message
                    // Show an error message to the user
                    ShowToast(errorMessage, paddingValues)
                }

                is ViewState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background)
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

            }

        } else {
            ShowToast("Not Connected to Internet", paddingValues)
        }
    }

@ExperimentalMaterial3Api
@ExperimentalLayoutApi
@Composable
fun ProductCards(
    product: Products,
    cartViewModel: CartViewModel,
    navController: NavController
) {

    val cartQty by cartViewModel.cartQuantity(product.id).collectAsState()
    var quantity by remember { mutableStateOf(if(cartQty > 0) cartQty else 1) }
    val ctx = LocalContext.current

    Card(
        modifier = Modifier
            .height(290.dp)
            .width(213.dp)
            .padding(6.dp)
            .clickable {

                navController.navigate("product/${product.id}"){
                    popUpTo(BottomNavItem.Home.route) {inclusive=false}
                }

            },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(8.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 15.dp, vertical = 20.dp)
        ) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)) {
                Image(
                    painter = rememberAsyncImagePainter(product.image),
                    contentDescription = "Product",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(100.dp)
                        .clip(RoundedCornerShape(8.dp))
                )

                // Floating qty card
                if (cartQty > 0) {
                    Card(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF2F2F2)),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(vertical = 6.dp, horizontal = 4.dp)
                        ) {
                            IconButton(onClick = {
                                quantity++
                                cartViewModel.addToCart(
                                    CartItem(
                                        productId = product.id,
                                        title = product.title,
                                        price = product.price,
                                        imageUrl = product.image,
                                        quantity = quantity
                                    )
                                )
                            }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.plus),
                                    contentDescription = "Add"
                                )
                            }

                            Text(cartQty.toString())

                            IconButton(onClick = {
                                if (quantity > 1) {
                                    quantity--
                                    cartViewModel.updateCart(
                                        CartItem(
                                            productId = product.id,
                                            title = product.title,
                                            price = product.price,
                                            imageUrl = product.image,
                                            quantity = quantity
                                        )
                                    )
                                }else
                                {
                                    quantity =0
                                    cartViewModel.removeItemByProductId(product.id)
                                }
                            }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.minus),
                                    contentDescription = "Remove"
                                )
                            }
                        }
                    }
                }
            }

            Text(product.title, fontSize = 15.sp, fontWeight = FontWeight.Medium, maxLines = 2)
            Text(product.description, fontSize = 12.sp, maxLines = 1)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Ksh${product.price}", fontSize = 18.sp, fontWeight = FontWeight.Medium)

            }



            if(cartQty < 1) {
                Box(
                    modifier = Modifier
                        .height(26.dp)
                        .width(80.dp)
                        .clip(RoundedCornerShape(12.dp))
                        //.padding(vertical = 16.dp)
                        .background(Color.Black)
                        .align(Alignment.CenterHorizontally)
                        .clickable {
                            cartViewModel.addToCart(
                                CartItem(
                                    productId = product.id,
                                    title = product.title,
                                    price = product.price,
                                    imageUrl = product.image,
                                    quantity = quantity
                                )
                            )

                            //ShowToast("msg", PaddingValues(10.dp))

                        },
                    contentAlignment = Alignment.Center
                ) {
//                    Icon(
//                        painter = painterResource(id = R.drawable.ic_cart),
//                        contentDescription = "Cart",
//                        tint = Color.White
//                    )
                    Text("Add Cart", fontSize = 12.sp, color = Color.White)
                }
            }
        }

    }
}

@Composable
private fun ShowToast(message: String, paddingValues: PaddingValues) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)

            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.connection_error),
                contentDescription = "contentDescription",
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center,
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp)
            )
            Text(message)
        }

    }

}