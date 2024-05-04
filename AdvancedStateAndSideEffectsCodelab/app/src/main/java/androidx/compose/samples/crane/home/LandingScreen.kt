/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package androidx.compose.samples.crane.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.samples.crane.R
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import kotlinx.coroutines.delay

private const val SplashWaitTime: Long = 2000

@Composable
fun LandingScreen(onTimeout: () -> Unit, modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        // A side-effect in Compose is a change to the state of the app that happens outside the scope of a composable function

        // TODO 3: LaunchedEffect and rememberUpdatedState step
        // This will always refer to the latest onTimeout function that LandingScreen was recomposed with
        val currentOnTimeout by rememberUpdatedState(onTimeout)

        // To call suspend functions safely from inside a composable, use the LaunchedEffect API, which triggers a coroutine-scoped side-effect in Compose.
        // Create an effect that matches the lifecycle of LandingScreen, the coroutine will be canceled if LaunchedEffect leaves the composition
        // If LandingScreen recomposes or onTimeout changes, the delay shouldn't start again.
        LaunchedEffect(Unit) {
            delay(SplashWaitTime)
            currentOnTimeout()
        } // LaunchedEffect guarantees that the side-effect will be executed when the call to LandingScreen makes it into the Composition.

        // TODO: Make LandingScreen disappear after loading data
        Image(painterResource(id = R.drawable.ic_crane_drawer), contentDescription = null)
    }
}
