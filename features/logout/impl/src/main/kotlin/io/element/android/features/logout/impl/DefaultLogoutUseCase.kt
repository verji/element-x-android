/*
 * Copyright (c) 2024 New Vector Ltd
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

package io.element.android.features.logout.impl

import com.squareup.anvil.annotations.ContributesBinding
import io.element.android.features.logout.api.LogoutUseCase
import io.element.android.libraries.di.AppScope
import io.element.android.libraries.matrix.api.MatrixClientProvider
import io.element.android.libraries.matrix.api.auth.MatrixAuthenticationService
import javax.inject.Inject

@ContributesBinding(AppScope::class)
class DefaultLogoutUseCase @Inject constructor(
    private val authenticationService: MatrixAuthenticationService,
    private val matrixClientProvider: MatrixClientProvider,
) : LogoutUseCase {
    override suspend fun logout(ignoreSdkError: Boolean): String? {
        val currentSession = authenticationService.getLatestSessionId()
        return if (currentSession != null) {
            matrixClientProvider.getOrRestore(currentSession)
                .getOrThrow()
                .logout(userInitiated = true, ignoreSdkError = true)
        } else {
            error("No session to sign out")
        }
    }
}
