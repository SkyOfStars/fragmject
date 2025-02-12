package com.example.fragment.library.base.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fragment.library.base.R

@Composable
fun Empty(
    onClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(R.drawable.empty),
                contentDescription = null,
            )
            Spacer(Modifier.size(10.dp))
            Button(
                onClick = onClick,
                shape = RoundedCornerShape(10),
                border = BorderStroke(1.dp, colorResource(R.color.theme)),
                colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(R.color.background)),
            ) {
                Text(
                    text = "重试",
                    fontSize = 14.sp,
                    color = colorResource(R.color.theme)
                )
            }
        }
    }
}
