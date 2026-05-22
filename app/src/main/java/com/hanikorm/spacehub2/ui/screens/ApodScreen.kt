package com.hanikorm.spacehub2.ui.screens

import android.annotation.SuppressLint
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
// ВАЖНО: Эти импорты чинят ошибку "Cannot infer argument for type parameter T"
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.hanikorm.spacehub2.domain.model.ApodEntry
import com.hanikorm.spacehub2.ui.viewmodel.ApodViewModel
import androidx.compose.runtime.collectAsState

@Composable
fun ApodScreen(viewModel: ApodViewModel) {
    // Используем collectAsState для StateFlow из ViewModel
    val apods: List<ApodEntry> by viewModel.items.collectAsState()

    Column(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        Text(
            text = "ГАЛЕРЕЯ NASA",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )

        if (apods.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Список пуст. Проверьте файл apod_final.json", color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(), // Добавил fillMaxSize для корректного скролла
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                // Явно указываем тип внутри лямбды, если импорт всё еще капризничает
                items(
                    items = apods,
                    key = { it.id }
                ) { apod: ApodEntry ->
                    ApodItem(apod)
                }
            }
        }
    }
}

@SuppressLint("LocalContextResourcesRead")
@Composable
fun ApodItem(apod: ApodEntry) {
    val context = LocalContext.current
    var isExpanded by remember { mutableStateOf(false) }
    var showFullScreen by remember { mutableStateOf(false) }

    val imageResId = remember(apod.imageResName) {
        context.resources.getIdentifier(apod.imageResName, "drawable", context.packageName)
    }

    if (showFullScreen) {
        FullScreenImageDialog(resId = imageResId, title = apod.title, onDismiss = { showFullScreen = false })
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(Color(0xFF1E1E1E), RoundedCornerShape(16.dp))
            .clickable { isExpanded = !isExpanded }
            .animateContentSize()
    ) {
        AsyncImage(
            model = imageResId,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(if (isExpanded) 400.dp else 220.dp)
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                .clickable { showFullScreen = true },
            contentScale = ContentScale.Crop
        )

        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = apod.title,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 22.sp
            )

            if (isExpanded) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = apod.explanation,
                    color = Color(0xFFB0B0B0),
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
            } else {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = apod.explanation,
                    color = Color(0xFFB0B0B0),
                    fontSize = 14.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun FullScreenImageDialog(resId: Int, title: String, onDismiss: () -> Unit) {

    if (resId == 0) {
        onDismiss() // Если картинки нет, сразу закрываем
        return
    }

    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    Dialog(
        onDismissRequest = onDismiss,
        // Позволяет диалогу занять весь экран без стандартных отступов системы
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            // Картинка с зумом
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTransformGestures { _, pan, zoom, _ ->
                            scale = (scale * zoom).coerceIn(1f, 4f)
                            if (scale > 1f) {
                                offset += pan * scale
                            } else {
                                offset = Offset.Zero
                            }
                        }
                    }
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        if (scale > 1f) {
                            scale = 1f
                            offset = Offset.Zero
                        } else {
                            onDismiss()
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = resId),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer(
                            scaleX = scale,
                            scaleY = scale,
                            translationX = offset.x,
                            translationY = offset.y
                        ),
                    contentScale = ContentScale.Fit
                )
            }

            // Заголовок сверху
            Text(
                text = title,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(16.dp)
                    .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                    .padding(8.dp)
            )

            // Кнопка скачать поверх картинки снизу
            Button(
                onClick = { /* Логика сохранения */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF333333)),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 32.dp)
            ) {
                Text("СКАЧАТЬ", color = Color.White)
            }
        }
    }
}