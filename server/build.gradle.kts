/*
 * Copyright 2024 pyamsoft
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

plugins {
  id("com.android.library")
  id("com.google.devtools.ksp")
}

android {
  namespace = "com.pyamsoft.tetherfi.server"

  compileSdk = rootProject.extra["compileSdk"] as Int

  defaultConfig {
    minSdk = rootProject.extra["minSdk"] as Int

    // Android Testing
    // https://developer.android.com/training/testing/instrumented-tests
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17

    // Flag to enable support for the new language APIs
    isCoreLibraryDesugaringEnabled = true
  }

  kotlinOptions { jvmTarget = JavaVersion.VERSION_17.majorVersion }

  buildFeatures { buildConfig = false }
}

dependencies {
  coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:${rootProject.extra["desugar"]}")

  kapt("com.google.dagger:dagger-compiler:${rootProject.extra["dagger"]}")

  // Compose runtime for annotations
  implementation("androidx.compose.runtime:runtime:${rootProject.extra["compose"]}")

  implementation("io.ktor:ktor-network:2.3.11")

  // PYDroid
  implementation("com.github.pyamsoft.pydroid:bus:${rootProject.extra["pydroid"]}")
  implementation("com.github.pyamsoft.pydroid:util:${rootProject.extra["pydroid"]}")

  testImplementation("org.jetbrains.kotlin:kotlin-test:${rootProject.extra["kotlin"]}")
  testImplementation(
      "org.jetbrains.kotlinx:kotlinx-coroutines-test:${rootProject.extra["coroutines"]}")

  androidTestImplementation("androidx.test:runner:${rootProject.extra["testRunner"]}")
  androidTestImplementation("org.jetbrains.kotlin:kotlin-test:${rootProject.extra["kotlin"]}")
  androidTestImplementation(
      "org.jetbrains.kotlinx:kotlinx-coroutines-test:${rootProject.extra["coroutines"]}")

  implementation(project(":core"))
}
