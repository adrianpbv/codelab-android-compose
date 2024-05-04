/*
 * Copyright 2021 The Android Open Source Project
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

package androidx.compose.samples.crane.base

import androidx.annotation.DrawableRes
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.samples.crane.ui.captionTextStyle
import androidx.compose.ui.graphics.SolidColor

@Composable
fun CraneEditableUserInput(
    state: EditableUserInputState = rememberEditableUserInputState(""),
    caption: String? = null,
    @DrawableRes vectorImageId: Int? = null
) {
    // TODO 7 Codelab: Encapsulate this state in a State holder
    //var textState by remember { mutableStateOf(hint) }
   // val isHint = { textState == hint }

    CraneBaseUserInput(
        caption = caption,
        tintIcon = { !state.isHint },
        showCaption = { !state.isHint },
        vectorImageId = vectorImageId
    ) {
        BasicTextField(
            value = state.text,
            onValueChange = {
                state.updateText(it)
            },
            textStyle = if (state.isHint) {
                captionTextStyle.copy(color = LocalContentColor.current)
            } else {
                MaterialTheme.typography.body1.copy(color = LocalContentColor.current)
            },
            cursorBrush = SolidColor(LocalContentColor.current)
        )
    }
}

class EditableUserInputState(private val hint: String, initialText: String){
    var text by mutableStateOf(initialText) // Compose tracks changes to the value and recomposes when changes happen.
        private set

    // expose an event updateText to modify it, which makes the class the single source of truth.
    fun updateText(newText: String){
        text = newText
    }

    // property that performs the check on-demand.
    val isHint: Boolean
        get() = text == hint

    companion object {
        // to indicate rememberSaveable how to save and restore an instance of this class use a Saver.
        val Saver: Saver<EditableUserInputState, *> = listSaver(
            save = { listOf(it.hint, it.text) },
            restore = {
                EditableUserInputState(
                    hint = it[0],
                    initialText = it[1],
                )
            }
        )
    }
}

// State holders always need to be remembered in order to keep them in the Composition and not create a new one every time
@Composable
fun rememberEditableUserInputState(hint: String): EditableUserInputState =
    // survives activity and process recreation.
    rememberSaveable(hint, saver = EditableUserInputState.Saver) {
        EditableUserInputState(hint, hint)
    }