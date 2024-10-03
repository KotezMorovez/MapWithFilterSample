package com.example.filterpointsonthemap.presentation.feature.map.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.PointF
import android.net.Uri
import android.provider.Settings
import android.view.ViewGroup
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.filterpointsonthemap.R
import com.example.filterpointsonthemap.presentation.common.ClusterView
import com.example.filterpointsonthemap.presentation.common.PlacemarkType
import com.example.filterpointsonthemap.presentation.common.PlacemarkUserData
import com.example.filterpointsonthemap.presentation.feature.dto.MapPointUI
import com.example.filterpointsonthemap.presentation.feature.map.view_model.MapViewModel
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.ClusterListener
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider
import com.yandex.runtime.ui_view.ViewProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    viewModel: MapViewModel = hiltViewModel(),
    onFilterButtonClickListener: () -> Unit
) {
    val points: List<MapPointUI>? by viewModel.pointsListFlow.collectAsState()
    var isRunning by remember { mutableStateOf(false) }
    val activity = LocalContext.current as Activity
    val permissionDialog = remember { mutableStateListOf<NeededPermission>() }
    val multiplePermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            permissions.entries.forEach { entry ->
                if (!entry.value)
                    permissionDialog.add(getNeededPermission(entry.key))
            }
        }
    )

    LaunchedEffect(key1 = true) {
        isRunning = true
        viewModel.getPoints()

        multiplePermissionLauncher.launch(
            arrayOf(
                NeededPermission.COARSE_LOCATION.permission,
                NeededPermission.FINE_LOCATION.permission
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.map_title),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Normal
                    )
                },
                actions = {
                    IconButton(
                        onClick = {
                            onFilterButtonClickListener.invoke()
                        },
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_filter),
                            contentDescription = "Filter"
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
        },
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White
    ) { padding ->
        val scaffoldPadding = PaddingValues(
            top = padding.calculateTopPadding() + 16.dp,
            bottom = 16.dp,
            start = padding.calculateStartPadding(LayoutDirection.Ltr) + 24.dp,
            end = padding.calculateEndPadding(LayoutDirection.Ltr) + 24.dp
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPadding),
        ) {
            YandexMapView(isRunning, points)

            permissionDialog.forEach { permission ->
                PermissionAlertDialog(
                    neededPermission = permission,
                    onDismiss = { permissionDialog.remove(permission) },
                    onOkClick = {
                        permissionDialog.remove(permission)
                        multiplePermissionLauncher.launch(arrayOf(permission.permission))
                    },
                    onGoToAppSettingsClick = {
                        permissionDialog.remove(permission)
                        activity.goToAppSetting()
                    },
                    isPermissionDeclined =
                    !activity.shouldShowRequestPermissionRationale(permission.permission)
                )
            }
        }
    }

    DisposableEffect(key1 = true) {
        onDispose {
            isRunning = false
        }
    }
}

@Composable
fun YandexMapView(
    isRunning: Boolean,
    points: List<MapPointUI>?
) {
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            MapView(context).apply {
                this.layoutParams = ViewGroup.LayoutParams(
                    context.resources.displayMetrics.widthPixels,
                    context.resources.displayMetrics.heightPixels
                )

                map.move(
                    CameraPosition(
                        Point(55.751225, 37.62954),
                        10.0f,
                        150.0f,
                        10.0f
                    )
                )
            }
        },
        update = { mapView ->
            mapView
                .map
                .mapObjects
                .clear()

            val placemarkTypeToImageProvider = mapOf(
                PlacemarkType.GREEN to ImageProvider.fromResource(
                    mapView.context,
                    R.drawable.ic_pin_green
                ),
                PlacemarkType.BLUE to ImageProvider.fromResource(
                    mapView.context,
                    R.drawable.ic_pin_blue
                ),
                PlacemarkType.RED to ImageProvider.fromResource(
                    mapView.context,
                    R.drawable.ic_pin_red
                ),
                PlacemarkType.PURPLE to ImageProvider.fromResource(
                    mapView.context,
                    R.drawable.ic_pin_purple
                )
            )

            if (isRunning) {
                mapView.onStart()
            } else {
                mapView.onStop()
            }

            if (points != null) {
                val clusterListener = ClusterListener { cluster ->
                    val placemarkTypes = cluster.placemarks.map {
                        (it.userData as PlacemarkUserData).type
                    }

                    cluster.appearance.setView(
                        ViewProvider(
                            ClusterView(mapView.context).apply {
                                this.setData(placemarkTypes)
                            }
                        )
                    )
                }

                val clusterizedCollection = mapView
                    .map
                    .mapObjects
                    .addClusterizedPlacemarkCollection(clusterListener)

                for (point in points) {
                    val placemark = clusterizedCollection.addPlacemark()
                    placemark.geometry = Point(point.coordinates.first, point.coordinates.second)

                    val type = when (point.service) {
                        "a" -> {
                            PlacemarkType.RED
                        }

                        "b" -> {
                            PlacemarkType.GREEN
                        }

                        "c" -> {
                            PlacemarkType.BLUE
                        }

                        else -> {
                            PlacemarkType.PURPLE
                        }
                    }

                    val imageProvider = placemarkTypeToImageProvider[type]

                    if (imageProvider != null) {
                        placemark.setIcon(imageProvider, IconStyle().apply {
                            anchor = PointF(0.5f, 1.0f)
                            scale = 0.4f
                        })
                    }

                    placemark.userData = PlacemarkUserData("Data_${point.id}", type)
                }

                clusterizedCollection.clusterPlacemarks(60.0, 20)
            }
        }
    )
}

@Composable
fun PermissionAlertDialog(
    neededPermission: NeededPermission,
    isPermissionDeclined: Boolean,
    onDismiss: () -> Unit,
    onOkClick: () -> Unit,
    onGoToAppSettingsClick: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                HorizontalDivider(color = Color.LightGray)
                Text(
                    text = if (isPermissionDeclined)
                        stringResource(id = R.string.permission_dialog_go_to_app_settings)
                    else
                        stringResource(id = R.string.permission_dialog_confirm),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (isPermissionDeclined)
                                onGoToAppSettingsClick()
                            else
                                onOkClick()

                        }
                        .padding(16.dp)
                )
            }
        },
        dismissButton = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                HorizontalDivider(color = Color.LightGray)
                Text(
                    text = "Cancel",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (isPermissionDeclined)
                                onGoToAppSettingsClick()
                            else
                                onOkClick()
                        }
                        .padding(16.dp)
                )
            }
        },
        title = {
            Text(
                text = stringResource(neededPermission.title),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
            )
        },
        text = {
            Text(
                text = stringResource(
                    neededPermission.permissionTextProvider(isPermissionDeclined)
                ),
            )
        },
    )
}

private fun Activity.goToAppSetting() {
    val intent = Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    )
    startActivity(intent)
}

private fun getNeededPermission(permission: String): NeededPermission {
    return NeededPermission.entries.find { it.permission == permission }
        ?: throw IllegalArgumentException("Permission $permission is not supported")
}

enum class NeededPermission(
    val permission: String,
    @StringRes val title: Int,
    @StringRes val description: Int,
    @StringRes val permanentlyDeniedDescription: Int,
) {
    COARSE_LOCATION(
        permission = Manifest.permission.ACCESS_COARSE_LOCATION,
        title = R.string.coarse_location_permission_title,
        description = R.string.coarse_location_permission_description,
        permanentlyDeniedDescription = R.string.coarse_location_permission_denied_description,
    ),

    FINE_LOCATION(
        permission = Manifest.permission.ACCESS_FINE_LOCATION,
        title = R.string.fine_location_permission_title,
        description = R.string.fine_location_permission_description,
        permanentlyDeniedDescription = R.string.fine_location_permission_denied_description
    );

    fun permissionTextProvider(isPermanentDenied: Boolean): Int {
        return if (isPermanentDenied) this.permanentlyDeniedDescription else this.description
    }
}