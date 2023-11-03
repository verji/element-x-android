/*
 * Copyright (c) 2023 New Vector Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.element.android.libraries.designsystem.components.async

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import io.element.android.libraries.architecture.Async
import io.element.android.libraries.designsystem.components.ProgressDialog
import io.element.android.libraries.designsystem.components.dialogs.ErrorDialog
import io.element.android.libraries.designsystem.components.dialogs.RetryDialog
import io.element.android.libraries.designsystem.preview.ElementPreview
import io.element.android.libraries.designsystem.preview.PreviewsDayNight

/**
 * Render an Async object.
 * - If Success, invoke the callback [onSuccess], only once.
 * - If Failure, display a dialog with the error, which can be transformed, using [errorTransform]. When
 * closed, [onErrorDismiss] will be invoked. If [onRetry] is not null, a retry button will be displayed.
 * - When loading, display a loading dialog, if [showProgressDialog] is true, with on optional [progressText].
 */
@Composable
fun <T> AsyncView(
    async: Async<T>,
    onSuccess: (T) -> Unit,
    onErrorDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    showProgressDialog: Boolean = true,
    progressText: String? = null,
    errorTransform: (Throwable) -> String = { it.message ?: it.toString() },
    onRetry: (() -> Unit)? = null,
) {
    when (async) {
        Async.Uninitialized -> Unit
        is Async.Loading -> {
            if (showProgressDialog) {
                ProgressDialog(
                    modifier = modifier,
                    text = progressText,
                )
            }
        }
        is Async.Failure -> {
            if (onRetry == null) {
                ErrorDialog(
                    modifier = modifier,
                    content = errorTransform(async.error),
                    onDismiss = onErrorDismiss
                )
            } else {
                RetryDialog(
                    modifier = modifier,
                    content = errorTransform(async.error),
                    onDismiss = onErrorDismiss,
                    onRetry = onRetry,
                )
            }
        }
        is Async.Success -> {
            LaunchedEffect(async) {
                onSuccess(async.data)
            }
        }
    }
}

@PreviewsDayNight
@Composable
internal fun AsyncViewPreview(
    @PreviewParameter(AsyncProvider::class) async: Async<Unit>,
) = ElementPreview {
    AsyncView(
        async = async,
        onSuccess = {},
        onErrorDismiss = {},
    )
}
