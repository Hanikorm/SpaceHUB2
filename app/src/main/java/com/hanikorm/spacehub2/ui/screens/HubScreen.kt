package com.hanikorm.spacehub2.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HubScreen(
    onNavigateToApod: () -> Unit,
    onNavigateToEarth: () -> Unit, // Имя должно быть таким
    onExit: () -> Unit             // И этот параметр обязателен
) {
    Column(
        modifier = Modifier.fillMaxSize().background(Color.Black).padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("SPACE HUB", color = Color.White, fontSize = 32.sp)
        Spacer(modifier = Modifier.height(48.dp))

        // Используем те же имена внутри кнопок
        MenuButton("ГАЛЕРЕЯ", onClick = onNavigateToApod)
        MenuButton("ЗЕМЛЯ", onClick = onNavigateToEarth)

        Spacer(modifier = Modifier.height(24.dp))

        TextButton(onClick = onExit) {
            Text("ВЫХОД", color = Color.Red.copy(alpha = 0.6f))
        }
    }
}

@Composable
fun MenuButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(64.dp).padding(vertical = 4.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.1f)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(text, color = Color.White)
    }
}