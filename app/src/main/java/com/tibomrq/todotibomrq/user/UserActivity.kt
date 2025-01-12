package com.tibomrq.todotibomrq.user

import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.viewModelScope
import coil3.compose.AsyncImage
import com.google.android.material.snackbar.Snackbar

import com.tibomrq.todotibomrq.R
import com.tibomrq.todotibomrq.data.Api
import kotlinx.coroutines.launch

import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class UserActivity : AppCompatActivity() {


    private val viewModel : UserViewModel by viewModels()

    private val captureUri by lazy {
        contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, ContentValues())
    }

    private fun pickPhotoWithPermission() {
        val storagePermission = android.Manifest.permission.READ_EXTERNAL_STORAGE
        val permissionStatus = checkSelfPermission(storagePermission)
        val isAlreadyAccepted = permissionStatus == PackageManager.PERMISSION_GRANTED
        val isExplanationNeeded = shouldShowRequestPermissionRationale(storagePermission)
        when {
            isAlreadyAccepted -> {pickPicture.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            Log.e("bjr", "arv")}
            isExplanationNeeded -> showMessage("L'autorisation est nécessaire pour sélectionner une image depuis les fichiers")// afficher une explication
            else -> {
               pickPictureWithPerm.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }

        }// lancer la demande de permission et afficher une explication en cas de refus
    }


    private fun showMessage(message: String) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show()
    }

    lateinit var pickPicture: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>
    lateinit var pickPictureWithPerm: ManagedActivityResultLauncher<String, Boolean>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_user)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        setContent {

            val userWebService = Api.userWebService

            val bitmap: Bitmap? by remember { mutableStateOf(null) }
            var uri: Uri? by remember { mutableStateOf(null) }
            val composeScope = rememberCoroutineScope()

            val takePicture = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
                if (success) uri = captureUri
                composeScope.launch {
                    uri?.let { viewModel.updateAvatar(it.toRequestBody()) } }
            }
            pickPicture = rememberLauncherForActivityResult(
                ActivityResultContracts.PickVisualMedia()) {
                uri = it
                composeScope.launch {
                    uri?.let { it1 -> viewModel.updateAvatar(it1.toRequestBody()) }

                }

            }



            pickPictureWithPerm = rememberLauncherForActivityResult(
                ActivityResultContracts.RequestPermission()) { isGranted ->
                    if (isGranted) {
                        pickPicture.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    }
                    else {
                        showMessage("L'autorisation est nécessaire")
                    }
            }

            viewModel.getUsername()


            // Collect the username from the ViewModel
            val usernameState = viewModel.usernameStateFlow.collectAsState(initial = "")

            // Local state for the TextField
            var name by remember { mutableStateOf(usernameState.value) }

            // Synchronize the local state with the ViewModel's state
            LaunchedEffect(usernameState.value) {
                name = usernameState.value // Update local state when the ViewModel's state changes
            }

            Column {
                AsyncImage(
                    modifier = Modifier.fillMaxHeight(.2f),
                    model = bitmap ?: uri,
                    contentDescription = null
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it;
                        viewModel.updateName(name, name)

                    }
                )

                Button(
                    onClick = { captureUri?.let { takePicture.launch(it) } },
                    content = { Text("Take picture") }
                )
                Button(
                    onClick = {
                        if (Build.VERSION.SDK_INT >= 29) {
                            pickPicture.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))}
                        else {
                            pickPhotoWithPermission()
                }}
                    ,
                    content = { Text("Pick photo") }
                )
            }
        }

    }
    private fun Bitmap.toRequestBody(): MultipartBody.Part {
        val tmpFile = File.createTempFile("avatar", "jpg")
        tmpFile.outputStream().use { // *use*: open et close automatiquement
            this.compress(Bitmap.CompressFormat.JPEG, 100, it) // *this* est le bitmap ici
        }
        return MultipartBody.Part.createFormData(
            name = "avatar",
            filename = "avatar.jpg",
            body = tmpFile.readBytes().toRequestBody()
        )
    }

    private fun Uri.toRequestBody(): MultipartBody.Part {
        val fileInputStream = contentResolver.openInputStream(this)!!
        val fileBody = fileInputStream.readBytes().toRequestBody()
        return MultipartBody.Part.createFormData(
            name = "avatar",
            filename = "avatar.jpg",
            body = fileBody
        )
    }

}