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

package io.element.android.features.licenses.impl.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import io.element.android.features.licenses.impl.model.DependencyLicenseItem
import io.element.android.libraries.architecture.AsyncData
import io.element.android.libraries.designsystem.components.button.BackButton
import io.element.android.libraries.designsystem.preview.ElementPreview
import io.element.android.libraries.designsystem.preview.PreviewsDayNight
import io.element.android.libraries.designsystem.theme.components.CircularProgressIndicator
import io.element.android.libraries.designsystem.theme.components.ListItem
import io.element.android.libraries.designsystem.theme.components.Scaffold
import io.element.android.libraries.designsystem.theme.components.Text
import io.element.android.libraries.designsystem.theme.components.TopAppBar
import io.element.android.libraries.ui.strings.CommonStrings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DependencyLicensesListView(
    state: DependencyLicensesListState,
    onBackClick: () -> Unit,
    onOpenLicense: (DependencyLicenseItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(CommonStrings.common_open_source_licenses)) },
                navigationIcon = { BackButton(onClick = onBackClick) },
            )
        },
    ) { contentPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(contentPadding)
                .padding(horizontal = 16.dp)
        ) {
            when (state.licenses) {
                is AsyncData.Failure -> item {
                    Text(
                        text = stringResource(CommonStrings.common_error),
                        modifier = Modifier.padding(16.dp)
                    )
                }
                AsyncData.Uninitialized,
                is AsyncData.Loading -> item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 64.dp)
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
                is AsyncData.Success -> items(state.licenses.data) { license ->
                    ListItem(
                        headlineContent = { Text(license.safeName) },
                        supportingContent = {
                            Text(
                                buildString {
                                    append(license.groupId)
                                    append(":")
                                    append(license.artifactId)
                                    append(":")
                                    append(license.version)
                                }
                            )
                        },
                        onClick = {
                            onOpenLicense(license)
                        }
                    )
                }
            }
        }
    }
}

@PreviewsDayNight
@Composable
internal fun DependencyLicensesListViewPreview(
    @PreviewParameter(DependencyLicensesListStateProvider::class) state: DependencyLicensesListState
) = ElementPreview {
    DependencyLicensesListView(
        state = state,
        onBackClick = {},
        onOpenLicense = {},
    )
}
