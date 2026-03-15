# Kiran Store Manager - ProGuard Rules

# ── Keep Application Classes ───────────────────────────
-keep class com.kiranstore.manager.** { *; }

# ── Hilt / Dagger ──────────────────────────────────────
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-dontwarn dagger.hilt.**

# ── Room Database ──────────────────────────────────────
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-dontwarn androidx.room.**

# ── Kotlin Coroutines ──────────────────────────────────
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}

# ── Retrofit / OkHttp ──────────────────────────────────
-dontwarn okhttp3.**
-dontwarn okio.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

# ── Gson ───────────────────────────────────────────────
-keep class com.google.gson.** { *; }
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer
-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}

# ── VoiceAction data class (Gemini JSON parsing) ───────
-keep class com.kiranstore.manager.services.ai.VoiceAction { *; }

# ── Supabase / Ktor ────────────────────────────────────
-dontwarn io.github.jan.tennert.**
-dontwarn io.ktor.**
-keep class io.ktor.** { *; }

# ── Compose ────────────────────────────────────────────
-keep class androidx.compose.** { *; }
-dontwarn androidx.compose.**

# ── Remove logging in release ──────────────────────────
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
}
