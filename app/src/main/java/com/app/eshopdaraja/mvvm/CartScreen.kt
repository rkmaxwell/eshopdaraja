package com.app.eshopdaraja.mvvm

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.app.eshopdaraja.include.BottomNavItem
import com.app.eshopdaraja.include.ConfirmLogoutDialog
import com.app.eshopdaraja.model.CartItem

@Composable
fun CartScreen(
    navController: NavController,
    productViewModel: ProductViewModel,
    cartViewModel: CartViewModel,
    paddingValues: PaddingValues
    ) {
    val cartItems by cartViewModel.cartItems.collectAsState(initial = emptyList())

    var showClearCartDialog by remember { mutableStateOf(false) }

    if(cartItems.isNotEmpty()) {

        LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)
            .padding(paddingValues)) {

            items(cartItems) { item ->
                CartItemRow(
                    item = item,
                    onIncrement = {
                        val newQty = item.quantity + 1
                        cartViewModel.addToCart(
                            item.copy(quantity = item.quantity + 1)
                        )
                    },
                    onDecrement = {
                        val newQty = item.quantity - 1
                        if (newQty > 0) {
                            cartViewModel.removeItem(item)
                            //cartViewModel.updateCart(item.copy(quantity = newQty))
                        } else {
                            cartViewModel.removeItemByProductId(item.productId)
                        }
                    }
                )
                Divider()
            }

            item {
                val total = cartItems.sumOf { it.price * it.quantity }


                Text(
                    text = "Total: KES ${"%.2f".format(total)}",
                    textAlign = TextAlign.End,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = {
                            showClearCartDialog=true

                        },
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.White,
                            containerColor = MaterialTheme.colorScheme.error
                        ),
                        modifier = Modifier
                            //.align(Alignment.End)
                            .padding(16.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Clear Cart")
                    }

                    Button(onClick = {
                        navController.navigate("login"){
                            popUpTo(BottomNavItem.Home.route) {inclusive=false}
                        }
                    },
                        modifier = Modifier
                            .padding(horizontal = 16.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.White,
                            containerColor = Color.Black
                        )
                            ) {

                        Text("Checkout",
                            style = TextStyle(fontSize = 14.sp),
                            )
                    }
                }
            }

        }
    }else{

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {

            Text("Cart is Empty", style = TextStyle(fontWeight = FontWeight.SemiBold,
                fontSize = 22.sp))
        }
    }

    ConfirmLogoutDialog(
        showDialog =
        showClearCartDialog,
        title = "Confirm Clear Cart",
        msg = "Sure to clear cart",
        action = "Clear Cart",
        onConfirm = {

            cartViewModel.clearCart()
        },
        onCancel = {

            showClearCartDialog=false
        })
}


@Composable
fun CartItemRow(
    item: CartItem,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = item.title,
            maxLines = 1,
            modifier = Modifier.weight(1f),
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyLarge)

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onDecrement) {
                Icon(Icons.Default.Delete, contentDescription = "Decrease")
            }

            Text(
                text = item.quantity.toString(),
                modifier = Modifier.padding(horizontal = 8.dp),
                style = MaterialTheme.typography.bodyLarge
            )

            IconButton(onClick = onIncrement) {
                Icon(Icons.Default.Add, contentDescription = "Increase")
            }
        }
    }
}

