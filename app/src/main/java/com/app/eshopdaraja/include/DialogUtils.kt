package com.app.eshopdaraja.include

import android.app.AlertDialog
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun ConfirmLogoutDialog(
    showDialog: Boolean,
    onConfirm:() -> Unit,
    onCancel:() -> Unit,
    title:String="",
    msg:String="",
    action:String=""
) {

    if(showDialog){

        AlertDialog(
            onDismissRequest = {onCancel()},
            shape = RoundedCornerShape(12.dp),
            title = {
                Text(
                text = "${title}",
                style = MaterialTheme.typography.titleMedium
            )},
            text = {
                Text(
                    text = "$msg",
                    style = MaterialTheme.typography.titleSmall
                )
            },

            confirmButton =
            {
                TextButton(onClick = {
                    onConfirm()
                    onCancel()
                }) {
                    Text("$action", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    onCancel()
                }) {
                    Text("Cancel")
                }
            }

        )
    }

}