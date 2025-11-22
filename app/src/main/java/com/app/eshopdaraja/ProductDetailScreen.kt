package com.app.eshopdaraja

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.app.eshopdaraja.model.Products
import com.app.eshopdaraja.mvvm.CartViewModel
import com.app.eshopdaraja.mvvm.ProductViewModel

@Composable
fun ProductDetailScreen (
    productId:Int,
    productViewModel: ProductViewModel = hiltViewModel(),
    cartViewModel: CartViewModel = hiltViewModel(),
    navController: NavController
) {

    val cartItems by cartViewModel.cartItems.collectAsState()
    val product = productViewModel.getProductById(productId)

    product?.let {
        ProductDetail(it,cartViewModel,navController)
    }

}

    @Composable
    fun ProductDetail(products: Products?, cartViewModel: CartViewModel,navController: NavController) {
        val context = LocalContext.current
        val act = LocalContext.current as Activity

        val cartItems by cartViewModel.cartItems.collectAsState()

        val cartItem = cartItems.find { it.productId == products!!.id }
        var quantity by remember { mutableStateOf(cartItem?.quantity ?: 1) }


        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, end = 15.dp, top = 15.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        navController.popBackStack()
                    },
                    modifier = Modifier
                        .background(color = Color(0x8DE7E1E1), shape = CircleShape)
                        .clip(CircleShape)

                ) {

                    Image(
                        imageVector = Icons.Default.ArrowBack, contentDescription = null
                    )
                }
                Row(
                    modifier = Modifier
                        .width(70.dp)
                        .background(color = Color(0x8DE7E1E1), shape = RoundedCornerShape(8.dp))
                        .padding(3.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    horizontalArrangement = Arrangement.spacedBy(
                        4.dp, Alignment.CenterHorizontally
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = products!!.rating.rate.toString(),
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Image(
                        imageVector = Icons.Default.Star, contentDescription = null
                    )
                }
            }

            Image(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(250.dp), painter = rememberAsyncImagePainter(
                    model = products!!.image, contentScale = ContentScale.Crop
                ), contentDescription = "Coffee"
            )
            Spacer(
                modifier = Modifier
                    .height(50.dp)
                    .background(Color(0x8DE7E1E1))
            )
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth()
                    .background(
                        Color.White, shape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp)
                    )
                //   .padding(15.dp)
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0x8DE7E1E1)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(15.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = products!!.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                            IconButton(onClick = { /*TODO*/ }) {
                                Image(
                                    imageVector = Icons.Default.Favorite,
                                    contentDescription = null,
                                    colorFilter = ColorFilter.tint(Color.Red),
                                    modifier = Modifier
                                        .size(40.dp)
                                        .background(
                                            Color(0x75F44336), shape = RoundedCornerShape(
                                                topStart = 20.dp, bottomStart = 20.dp
                                            )
                                        )
                                        .padding(10.dp)
                                        .weight(1f)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = products.description, fontSize = 16.sp, color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(20.dp))

                    }
                }

                /*button*/
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .background(
                            Color.White,
                            shape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp)
                        )
                        .clip(RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp)),
                    contentAlignment = Alignment.Center
                ) {


                    if(cartItem != null){

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = {
                                if (quantity > 1) {
                                    quantity--
                                    cartViewModel.updateCart(
                                        cartItem.copy(quantity = quantity)
                                    )
                                }
                            }) {
                                Icon(painterResource(id = R.drawable.minus), contentDescription = "Minus")
                            }

                            Text(
                                quantity.toString(),
                                fontSize = 20.sp,
                                modifier = Modifier.padding(horizontal = 12.dp)
                            )

                            IconButton(onClick = {
                                quantity++
                                cartViewModel.updateCart(
                                    cartItem.copy(quantity = quantity)
                                )
                            }) {
                                Icon(painterResource(id = R.drawable.plus), contentDescription = "Plus")
                            }
                        }
                    } else {

                        Button(
                            colors = ButtonDefaults.buttonColors(

                                contentColor = Color.White
                            ),
                            modifier = Modifier
                                .width(200.dp)
                                .padding(top = 30.dp, bottom = 30.dp)
                                .height(60.dp)
                                .clip(RoundedCornerShape(15.dp)),
                            onClick = {
                                Toast.makeText(
                                    context, "Successfully added to cart", Toast.LENGTH_SHORT
                                ).show()

                            },
                        ) {
                            Text(text = "Add to Cart", fontSize = 16.sp)
                        }
                    }


            }
            }
            }

    }