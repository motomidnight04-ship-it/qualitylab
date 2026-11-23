# Задание 1: в release-сборке отладочные логи удаляются полностью
-assumenosideeffects class android.util.Log {
    public static int v(...);
    public static int d(...);
    public static int i(...);
}

# Модель для Gson (поля читаются через рефлексию)
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.example.qualitylab.net.Post { *; }
