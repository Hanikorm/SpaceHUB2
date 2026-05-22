package com.hanikorm.spacehub2.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import gov.nasa.worldwind.WorldWindow
import gov.nasa.worldwind.geom.Position
import gov.nasa.worldwind.layer.BackgroundLayer
import gov.nasa.worldwind.layer.BlueMarbleLandsatLayer
import gov.nasa.worldwind.layer.RenderableLayer
import gov.nasa.worldwind.render.Color as WwColor
import gov.nasa.worldwind.shape.Placemark
import gov.nasa.worldwind.shape.PlacemarkAttributes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

@Composable
fun MapScreen() {
    // 1. Координаты МКС
    var issPosition by remember { mutableStateOf(Position.fromDegrees(0.0, 0.0, 400000.0)) }

    // 2. Обновление координат в фоне
    LaunchedEffect(Unit) {
        while (true) {
            try {
                val response = withContext(Dispatchers.IO) {
                    URL("https://api.wheretheiss.at/v1/satellites/25544").readText()
                }
                val json = JSONObject(response)
                val lat = json.getDouble("latitude")
                val lon = json.getDouble("longitude")
                val alt = json.getDouble("altitude") * 1000.0

                issPosition = Position.fromDegrees(lat, lon, alt)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            delay(5000)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                WorldWindow(context).apply {
                    layers.addLayer(BackgroundLayer())
                    layers.addLayer(BlueMarbleLandsatLayer())

                    val issLayer = RenderableLayer().apply { displayName = "ISS_LAYER" }
                    layers.addLayer(issLayer)

                    navigator.tilt = 0.0
                    onResume()
                }
            },
            update = { wwd ->
                // Ищем слой вручную
                var issLayer: RenderableLayer? = null
                val layers = wwd.layers
                for (i in 0 until layers.count()) {
                    val layer = layers.getLayer(i)
                    if (layer is RenderableLayer && layer.displayName == "ISS_LAYER") {
                        issLayer = layer
                        break
                    }
                }

                issLayer?.clearRenderables()

                // Оранжевая точка
                val placemarkAttrs = PlacemarkAttributes().apply {
                    imageColor = WwColor(1f, 0.5f, 0f, 1f)
                    imageScale = 15.0
                }

                val issPoint = Placemark(issPosition, placemarkAttrs).apply {
                    altitudeMode = gov.nasa.worldwind.WorldWind.ABSOLUTE
                }

                issLayer?.addRenderable(issPoint)
                wwd.requestRedraw()
            },
            onRelease = { wwd ->
                wwd.onPause()
            }
        )
    }
}
