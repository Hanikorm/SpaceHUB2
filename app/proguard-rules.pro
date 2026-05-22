# --- ТВОИ МОДЕЛИ ДАННЫХ ---
# Запрещаем переименовывать поля в моделях, иначе Gson не найдет их в JSON
-keep class com.hanikorm.spacehub2.domain.model.** { *; }

# --- GSON ---
# Gson использует рефлексию, эти правила обязательны для его работы
-keepattributes Signature, *Annotation*, EnclosingMethod
-keep class com.google.gson.** { *; }
-keep class sun.misc.Unsafe { *; }

# --- COIL (Загрузка изображений) ---
# Чтобы Coil мог корректно обрабатывать картинки в релизе
-keep class coil.** { *; }
-keep class okhttp3.** { *; }
-keep class okio.** { *; }

# --- NASA WORLDWIND (AAR библиотека) ---
# Если используешь функции NASA WorldWind, защищаем их от вырезания
-keep class gov.nasa.worldwind.** { *; }
-keep interface gov.nasa.worldwind.** { *; }

# --- ANDROID & COMPOSE ---
# Защищаем стандартные компоненты и жесты
-keep class androidx.compose.runtime.** { *; }
-keep class androidx.compose.animation.** { *; }
-keep class androidx.compose.foundation.gestures.** { *; }
-keep class androidx.compose.ui.graphics.** { *; }

# Предотвращаем удаление ресурсов, на которые мы ссылаемся динамически
-keepclassmembers class **.R$* {
    public static <fields>;
}

# Если приложение все еще вылетает из-за отсутствия классов
-dontwarn com.google.firebase.crashlytics.**
-dontwarn gov.nasa.worldwind.**