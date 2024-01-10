@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidApplication)
    id("com.google.gms.google-services")

}

android {
    namespace = "com.DatingApp.tinder101"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.DatingApp.tinder101"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation("androidx.cardview:cardview:1.0.0")

    implementation("com.squareup.picasso:picasso:2.71828")

    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))
    // TODO: Add the dependencies for Firebase products you want to use
    // When using the BoM, don't specify versions in Firebase dependencies
    implementation("com.google.firebase:firebase-analytics")

    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")

    implementation("com.google.firebase:firebase-auth")

    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.firebase:firebase-storage:20.3.0")
    implementation("com.google.firebase:firebase-database")


    implementation("androidx.fragment:fragment:1.6.2")

//    implementation(platform("org.jetbrains.kotlin:kotlin-bom:1.8.0"))


    implementation("com.facebook.shimmer:shimmer:0.5.0")

    implementation("com.intuit.sdp:sdp-android:1.1.0")
    implementation("com.intuit.ssp:ssp-android:1.1.0")


    implementation("com.makeramen:roundedimageview:2.3.0")

    implementation("com.google.code.gson:gson:2.10.1")
    implementation("androidx.multidex:multidex:2.0.1")

    implementation("me.relex:circleindicator:2.1.6")

    implementation("org.slf4j:slf4j-simple:1.7.25")

    implementation("com.squareup.okhttp3:okhttp:4.11.0")

    implementation("com.google.maps:google-maps-services:2.2.0")
    implementation("com.google.android.libraries.places:places:3.3.0")

    implementation("com.google.maps.android:android-maps-utils:3.8.2")
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation("androidx.viewpager2:viewpager2:1.1.0-beta02")
    implementation("com.cloudinary:cloudinary-android:2.6.1")
    implementation("com.lorentzos.swipecards:library:1.0.9")
    implementation("com.google.android.flexbox:flexbox:3.0.0")


}