package com.example.project.cartoons.presentation.profile.screen

import android.Manifest
import android.app.AlertDialog
import android.content.ContentValues
import android.content.pm.PackageManager
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.project.cartoons.presentation.profile.viewModel.EditProfileViewModel
import com.github.terrakok.modo.Screen
import com.github.terrakok.modo.ScreenKey
import com.github.terrakok.modo.generateScreenKey
import com.github.terrakok.modo.stack.LocalStackNavigation
import com.github.terrakok.modo.stack.back
import kotlinx.parcelize.Parcelize
import org.koin.androidx.compose.koinViewModel
import java.io.File
import java.util.Date
import com.example.project.R
import com.example.project.navigation.Route
import com.example.project.navigation.TopLevelBackStack
import org.koin.java.KoinJavaComponent.inject

@Parcelize
class EditProfileScreen(
    override val screenKey: ScreenKey = generateScreenKey(),
) : Screen {

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
    @Composable
    override fun Content(modifier: Modifier) {

        val context = LocalContext.current
        val viewModel = koinViewModel<EditProfileViewModel>()
        val state = viewModel.viewState

        LaunchedEffect(Unit) {
            viewModel.navigationEvent.collect { destination ->
                when (destination) {
                    "back" -> {
                        val topLevelBackStack by inject<TopLevelBackStack<Route>>(clazz = TopLevelBackStack::class.java)
                        topLevelBackStack.removeLast()
                    }
                }
            }
        }

        var imageUri by remember { mutableStateOf<Uri?>(null) }

        val pickMedia: ActivityResultLauncher<PickVisualMediaRequest> =
            rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                viewModel.onImageSelected(uri)
            }

        val requestPermissionLauncher =
            rememberLauncherForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (!isGranted) {
                    val dialog = AlertDialog.Builder(context)
                        .setMessage("Очень жаль...")
                        .setCancelable(false)
                        .setPositiveButton("OK") { _, _ ->
                        }
                    dialog.show()
                }
                viewModel.onPermissionClosed()
            }

        val mGetContent = rememberLauncherForActivityResult<Uri, Boolean>(
            ActivityResultContracts.TakePicture()
        ) { success: Boolean ->
            if (success) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                    imageUri?.path?.let { filePath ->
                        MediaScannerConnection.scanFile(
                            context,
                            arrayOf(filePath),
                            arrayOf("image/jpeg"),
                            null
                        )
                    }
                }
                viewModel.onImageSelected(imageUri)
            }
        }

        Scaffold(
            contentWindowInsets = WindowInsets(0.dp),
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = stringResource(R.string.edit_profile))
                    },
                    navigationIcon = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            Modifier
                                .padding(end = 8.dp)
                                .clickable {
                                    viewModel.onBackClicked()
                                }
                        )
                    },
                    actions = {
                        Icon(
                            imageVector = Icons.Default.Done,
                            contentDescription = null,
                            Modifier
                                .padding(end = 8.dp)
                                .clickable {
                                    viewModel.onDoneClicked()
                                }
                        )
                    },
                    modifier = Modifier.shadow(elevation = 1.dp)
                )
            }) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
            ) {
                GlideImage(
                    model = state.photoUri,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .size(128.dp)
                        .clip(CircleShape)
                        .clickable { viewModel.onAvatarClicked() },
                ) { request ->
                    request
                        .placeholder(R.drawable.mobile_phone)
                        .error(R.drawable.mobile_phone)
                }
                TextField(
                    value = state.name,
                    onValueChange = { viewModel.onNameChanged(it) },
                    label = { Text(stringResource(R.string.name)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                )
                TextField(
                    value = state.url,
                    onValueChange = { viewModel.onUrlChanged(it) },
                    label = { Text(stringResource(R.string.link)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                )
            }
        }

        if (state.isNeedToShowPermission) {
            LaunchedEffect(Unit) {
                if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.Q &&
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermissionLauncher.launch(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                }
            }
        }

        fun onCameraSelected() {
            val newImageUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val contentValues = ContentValues().apply {
                    put(MediaStore.Images.Media.DISPLAY_NAME, "profile_${System.currentTimeMillis()}.jpg")
                    put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                    put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/MyApp")
                }

                context.contentResolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    contentValues
                )
            } else {
                val picturesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                val appDir = File(picturesDir, "MyApp")
                if (!appDir.exists()) {
                    appDir.mkdirs()
                }

                val pictureFile = File(appDir, "profile_${System.currentTimeMillis()}.jpg")
                FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.provider",
                    pictureFile
                )
            }

            newImageUri?.let { uri ->
                imageUri = uri
                mGetContent.launch(uri)
            }
        }

        if (state.isNeedToShowSelect) {
            Dialog(onDismissRequest = { viewModel.onSelectDismiss() }) {
                Surface(
                    modifier = Modifier.fillMaxWidth(0.8f),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = stringResource(R.string.camera),
                            Modifier.clickable {
                                onCameraSelected()
                                viewModel.onSelectDismiss()
                            }
                        )
                        Text(text = stringResource(R.string.gallery),
                            Modifier.clickable {
                                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                                viewModel.onSelectDismiss()
                            })
                    }
                }
            }
        }
    }
}