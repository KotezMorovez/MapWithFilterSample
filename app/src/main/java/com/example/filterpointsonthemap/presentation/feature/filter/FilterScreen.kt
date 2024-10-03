package com.example.filterpointsonthemap.presentation.feature.filter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.filterpointsonthemap.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterScreen(
    onBackClickListener: () -> Unit,
    onApplyClickListener: () -> Unit,
    viewModel: FilterScreenViewModel = hiltViewModel()
) {
    val services by viewModel.serviceListFlow.collectAsState()

    LaunchedEffect(key1 = true) {
        viewModel.getServices()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.filter_title),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Normal
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        onBackClickListener.invoke()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Back"
                        )
                    }
                },
                windowInsets = TopAppBarDefaults.windowInsets,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    navigationIconContentColor = Color.Black,
                    titleContentColor = Color.Black,
                    actionIconContentColor = Color.Black
                ),
            )
        }, modifier = Modifier.fillMaxSize(), containerColor = Color.White
    ) { padding ->
        val scaffoldPadding = PaddingValues(
            top = padding.calculateTopPadding() + 16.dp,
            bottom = 16.dp,
            start = padding.calculateStartPadding(LayoutDirection.Ltr) + 24.dp,
            end = padding.calculateEndPadding(LayoutDirection.Ltr) + 24.dp
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                userScrollEnabled = true
            ) {
                items(items = services, key = { item ->
                    item.random()
                }, contentType = { null }, itemContent = { it ->
                    ServiceItemView(
                        viewModel = viewModel,
                        service = it,
                        rowPaddings = Pair(0, 16),
                        isChecked = viewModel.isThisServiceUse(it)
                    )
                })
            }

            Button(
                onClick = {
                    viewModel.saveServices()
                    onApplyClickListener.invoke()
                }
            ) {
                Text(
                    text = stringResource(id = R.string.filter_apply),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}

@Composable
fun ServiceItemView(
    viewModel: FilterScreenViewModel,
    service: String,
    isChecked: Boolean,
    rowPaddings: Pair<Int, Int>
) {
    val checkedState = remember { mutableStateOf(isChecked) }
    Row(
        modifier = Modifier.padding(
            top = rowPaddings.second.dp,
            start = rowPaddings.first.dp,
            end = rowPaddings.first.dp
        )
    ) {
        Text(
            modifier = Modifier,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            text = service,
            fontSize = 16.sp,
            color = Color.Black,
        )

        Spacer(modifier = Modifier.weight(1f))

        Checkbox(
            checked = checkedState.value,
            onCheckedChange = {
                checkedState.value = it
                viewModel.updateCheckedServicesList(service, checkedState.value)
            }
        )
    }
}