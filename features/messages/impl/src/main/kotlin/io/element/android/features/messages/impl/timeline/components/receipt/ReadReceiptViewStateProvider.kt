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

package io.element.android.features.messages.impl.timeline.components.receipt

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import io.element.android.features.messages.impl.timeline.model.ReadReceiptData
import io.element.android.libraries.designsystem.components.avatar.AvatarData
import io.element.android.libraries.designsystem.components.avatar.AvatarSize
import io.element.android.libraries.designsystem.components.avatar.anAvatarData
import io.element.android.libraries.matrix.api.core.EventId
import io.element.android.libraries.matrix.api.timeline.item.event.LocalEventSendState
import kotlinx.collections.immutable.toImmutableList

class ReadReceiptViewStateProvider : PreviewParameterProvider<ReadReceiptViewState> {
    override val values: Sequence<ReadReceiptViewState>
        get() = sequenceOf(
            aReadReceiptViewState(),
            aReadReceiptViewState(sendState = LocalEventSendState.Sending),
            aReadReceiptViewState(sendState = LocalEventSendState.Sent(EventId("\$eventId"))),
            aReadReceiptViewState(
                sendState = LocalEventSendState.Sent(EventId("\$eventId")),
                receipts = List(1) { aReadReceiptData(it) },
            ),
            aReadReceiptViewState(
                sendState = LocalEventSendState.Sent(EventId("\$eventId")),
                receipts = List(2) { aReadReceiptData(it) },
            ),
            aReadReceiptViewState(
                sendState = LocalEventSendState.Sent(EventId("\$eventId")),
                receipts = List(3) { aReadReceiptData(it) },
            ),
            aReadReceiptViewState(
                sendState = LocalEventSendState.Sent(EventId("\$eventId")),
                receipts = List(4) { aReadReceiptData(it) },
            ),
            aReadReceiptViewState(
                sendState = LocalEventSendState.Sent(EventId("\$eventId")),
                receipts = List(5) { aReadReceiptData(it) },
            ),
        )
}

internal fun aReadReceiptViewState(
    sendState: LocalEventSendState? = null,
    isLastOutgoingMessage: Boolean = true,
    receipts: List<ReadReceiptData> = emptyList(),
) = ReadReceiptViewState(
    sendState = sendState,
    isLastOutgoingMessage = isLastOutgoingMessage,
    receipts = receipts.toImmutableList(),
)

internal fun aReadReceiptData(
    index: Int,
    avatarData: AvatarData = anAvatarData(
        id = "$index",
        size = AvatarSize.TimelineReadReceipt
    ),
    formattedDate: String = "12:34",
) = ReadReceiptData(
    avatarData = avatarData,
    formattedDate = formattedDate,
)
