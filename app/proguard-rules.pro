# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keep class com.actvn.at170557.storefashion.ui.address.Address { *; }
-keep class com.actvn.at170557.storefashion.ui.login_signup.User { *; }
-keep class com.actvn.at170557.storefashion.ui.main.mycart.CartItem { *; }
-keep class com.actvn.at170557.storefashion.ui.search.Product { *; }
-keep class com.actvn.at170557.storefashion.ui.order.Order { *; }


-keep class com.actvn.at170557.storefashion.ui.address.** { *; }



-keep class * extends android.app.Activity
-keep class * extends android.app.Fragment
-keep class * extends android.app.Service
-keep class * extends android.content.BroadcastReceiver
-keep class * extends android.content.ContentProvider

-keep class com.google.gson.** { *; }
-keepattributes Signature
-keepattributes *Annotation*
-keep class sun.misc.** { *; }

-keep class retrofit2.** { *; }
-dontwarn retrofit2.**
-keepclassmembers class androidx.room.** {
   *;
}
-keep class androidx.room.** { *; }
-dontwarn androidx.room.**


