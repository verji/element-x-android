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

package io.element.android.libraries.pushproviders.unifiedpush

import com.squareup.anvil.annotations.ContributesMultibinding
import io.element.android.libraries.di.AppScope
import io.element.android.libraries.matrix.api.MatrixClient
import io.element.android.libraries.pushproviders.api.CurrentUserPushConfig
import io.element.android.libraries.pushproviders.api.Distributor
import io.element.android.libraries.pushproviders.api.PushProvider
import io.element.android.libraries.pushstore.api.clientsecret.PushClientSecret
import javax.inject.Inject

@ContributesMultibinding(AppScope::class)
class UnifiedPushProvider @Inject constructor(
    private val unifiedPushDistributorProvider: UnifiedPushDistributorProvider,
    private val registerUnifiedPushUseCase: RegisterUnifiedPushUseCase,
    private val unRegisterUnifiedPushUseCase: UnregisterUnifiedPushUseCase,
    private val pushClientSecret: PushClientSecret,
    private val unifiedPushStore: UnifiedPushStore,
    private val unifiedPushCurrentUserPushConfigProvider: UnifiedPushCurrentUserPushConfigProvider,
) : PushProvider {
    override val index = UnifiedPushConfig.INDEX
    override val name = UnifiedPushConfig.NAME

    override fun getDistributors(): List<Distributor> {
        return unifiedPushDistributorProvider.getDistributors()
    }

    override suspend fun registerWith(matrixClient: MatrixClient, distributor: Distributor): Result<Unit> {
        val clientSecret = pushClientSecret.getSecretForUser(matrixClient.sessionId)
        return registerUnifiedPushUseCase.execute(distributor, clientSecret)
            .onSuccess {
                unifiedPushStore.setDistributorValue(matrixClient.sessionId, distributor.value)
            }
    }

    override suspend fun getCurrentDistributor(matrixClient: MatrixClient): Distributor? {
        val distributorValue = unifiedPushStore.getDistributorValue(matrixClient.sessionId)
        return getDistributors().find { it.value == distributorValue }
    }

    override suspend fun unregister(matrixClient: MatrixClient): Result<Unit> {
        val clientSecret = pushClientSecret.getSecretForUser(matrixClient.sessionId)
        return unRegisterUnifiedPushUseCase.execute(matrixClient, clientSecret)
    }

    override suspend fun getCurrentUserPushConfig(): CurrentUserPushConfig? {
        return unifiedPushCurrentUserPushConfigProvider.provide()
    }
}
