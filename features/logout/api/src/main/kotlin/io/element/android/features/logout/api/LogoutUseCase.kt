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

package io.element.android.features.logout.api

/**
 * Used to trigger a log out of the current user from any part of the app.
 */
interface LogoutUseCase {
    /**
     * Log out the current user and then perform any needed cleanup tasks.
     * @param ignoreSdkError if true, the SDK error will be ignored and the user will be logged out anyway.
     * @return an optional URL. When the URL is there, it should be presented to the user after logout for
     * Relying Party (RP) initiated logout on their account page.
     */
    suspend fun logout(ignoreSdkError: Boolean): String?

    interface Factory {
        fun create(sessionId: String): LogoutUseCase
    }
}
