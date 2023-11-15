package com.ahmetocak.shoppingapp.presentation.profile

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ahmetocak.shoppingapp.R
import com.ahmetocak.shoppingapp.common.helpers.formatDate
import com.ahmetocak.shoppingapp.common.helpers.imageBitmapToFile
import com.mr0xf00.easycrop.CropError
import com.mr0xf00.easycrop.CropResult
import com.mr0xf00.easycrop.crop
import com.mr0xf00.easycrop.rememberImageCropper
import com.mr0xf00.easycrop.ui.ImageCropperDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(modifier: Modifier = Modifier, onSignOutClicked: () -> Unit) {

    val viewModel: ProfileViewModel = hiltViewModel()

    val uiState by viewModel.uiState.collectAsState()

    var showUpdateDialog by remember { mutableStateOf(false) }

    var showVerifyPhoneNumberDialog by remember { mutableStateOf(false) }

    var imageUri by remember { mutableStateOf<Uri?>(null) }

    var showImageCropperDialog by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState()

    val activity = LocalContext.current as Activity

    if (uiState.userMessages.isNotEmpty()) {
        Toast.makeText(
            LocalContext.current,
            uiState.userMessages.first(),
            Toast.LENGTH_SHORT
        ).show()
        viewModel.consumedUserMessage()
        showUpdateDialog = false
    }

    when (uiState.verifyPhoneNumber) {
        VerifyPhoneNumberState.NOTHING -> {
            showVerifyPhoneNumberDialog = false
        }

        VerifyPhoneNumberState.ON_VERIFICATION_COMPLETED -> {
            showUpdateDialog = false
            showVerifyPhoneNumberDialog = false
            Toast.makeText(
                LocalContext.current,
                stringResource(id = R.string.verification_completed),
                Toast.LENGTH_SHORT
            ).show()
        }

        VerifyPhoneNumberState.ON_CODE_SENT -> {
            showVerifyPhoneNumberDialog = true
        }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
        showImageCropperDialog = true
    }

    ProfileScreenContent(
        modifier = modifier,
        userImgUrl = uiState.photoUrl,
        userName = uiState.name ?: "",
        phoneNumber = uiState.phoneNumber ?: "",
        address = uiState.userDetail?.address ?: "",
        birthdate = uiState.userDetail?.birthdate?.formatDate() ?: "",
        onAccountInfoClicked = {
            viewModel.setAccountInfoType(it)
            showUpdateDialog = !showUpdateDialog
        },
        showUpdateDialog = showUpdateDialog,
        updateValue = viewModel.updateValue,
        onUpdateValueChange = { viewModel.updateAccountInfoValue(it) },
        onDismissRequest = {
            viewModel.clearAccountInfoValue()
            showUpdateDialog = false
        },
        onUpdateClick = {
            when (viewModel.infoType) {
                InfoType.NAME -> {
                    viewModel.updateUserName()
                }

                InfoType.MOBILE -> {
                    viewModel.sendVerificationCode(activity)
                }

                InfoType.ADDRESS -> {
                    viewModel.uploadUserAddress()
                }

                else -> {}
            }
        },
        dialogTitle = when (viewModel.infoType) {
            InfoType.NAME -> {
                stringResource(id = R.string.name)
            }

            InfoType.MOBILE -> {
                stringResource(id = R.string.mobile)
            }

            InfoType.ADDRESS -> {
                stringResource(id = R.string.address)
            }

            InfoType.BIRTHDATE -> {
                stringResource(id = R.string.birthdate)
            }
        },
        infoType = viewModel.infoType,
        onUserPhotoClicked = {
            launcher.launch("image/*")
        },
        imageUri = imageUri,
        showImageCropDialog = showImageCropperDialog,
        uploadPhoto = {
            if (it != null) {
                showImageCropperDialog = false
                viewModel.updateUserPhoto(it)
            }
        },
        showVerifyPhoneNumberDialog = showVerifyPhoneNumberDialog,
        codeValue = viewModel.verificationCode,
        onCodeValueChange = {
            viewModel.updateVerificationCodeValue(it)
        },
        onVerifyPhoneNumberDismiss = {
            showVerifyPhoneNumberDialog = false
        },
        verifyPhoneNumber = {
            viewModel.verifyUserPhoneNumber()
        },
        datePickerState = datePickerState,
        onDatePickerDialogDismiss = {
            showUpdateDialog = false
        },
        onDateConfirmClick = {
            datePickerState.selectedDateMillis?.let { viewModel.updateUserBirthdate(it) }
            showUpdateDialog = false
        },
        onSignOutClicked = onSignOutClicked
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileScreenContent(
    modifier: Modifier,
    userImgUrl: Uri?,
    userName: String,
    phoneNumber: String,
    address: String,
    birthdate: String,
    onAccountInfoClicked: (InfoType) -> Unit,
    showUpdateDialog: Boolean,
    onDismissRequest: () -> Unit,
    updateValue: String,
    onUpdateValueChange: (String) -> Unit,
    onUpdateClick: () -> Unit,
    dialogTitle: String,
    infoType: InfoType,
    onUserPhotoClicked: () -> Unit,
    imageUri: Uri?,
    showImageCropDialog: Boolean,
    uploadPhoto: (Uri?) -> Unit,
    showVerifyPhoneNumberDialog: Boolean,
    codeValue: String,
    onCodeValueChange: (String) -> Unit,
    verifyPhoneNumber: () -> Unit,
    onVerifyPhoneNumberDismiss: () -> Unit,
    datePickerState: DatePickerState,
    onDatePickerDialogDismiss: () -> Unit,
    onDateConfirmClick: () -> Unit,
    onSignOutClicked: () -> Unit
) {
    if (showUpdateDialog) {
        when (infoType) {
            InfoType.BIRTHDATE -> {
                DatePickerDialog(
                    onDismissRequest = onDatePickerDialogDismiss,
                    confirmButton = {
                        TextButton(onClick = onDateConfirmClick) {
                            Text(text = "Confirm")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = onDatePickerDialogDismiss) {
                            Text(text = "Cancel")
                        }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }

            else -> {
                UpdateAccountInfoDialog(
                    modifier = modifier,
                    onDismissRequest = onDismissRequest,
                    updateValue = updateValue,
                    onUpdateValueChange = onUpdateValueChange,
                    onUpdateClick = onUpdateClick,
                    title = dialogTitle,
                    infoType = infoType
                )
            }
        }
    }

    if (showImageCropDialog) {
        ImageCrop(imageUri = imageUri, context = LocalContext.current, uploadPhoto = uploadPhoto)
    }

    if (showVerifyPhoneNumberDialog) {
        VerifyPhoneNumberDialog(
            modifier = modifier,
            codeValue = codeValue,
            onCodeValueChange = onCodeValueChange,
            verifyPhoneNumber = verifyPhoneNumber,
            onVerifyPhoneNumberDismiss = onVerifyPhoneNumberDismiss
        )
    }

    Column(modifier = modifier.fillMaxSize()) {
        ProfileSection(
            modifier = modifier.weight(3f),
            userImgUrl = userImgUrl,
            userName = userName,
            onUserPhotoClicked = onUserPhotoClicked,
            onSignOutClicked = onSignOutClicked
        )
        AccountInfoSection(
            modifier = modifier.weight(4f),
            phoneNumber = phoneNumber,
            address = address,
            birthdate = birthdate,
            name = userName,
            onAccountInfoClicked = onAccountInfoClicked
        )
    }
}

@Composable
private fun ProfileSection(
    modifier: Modifier,
    userImgUrl: Uri?,
    userName: String,
    onUserPhotoClicked: () -> Unit,
    onSignOutClicked: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.horizontalGradient(
                    listOf(
                        colorResource(id = R.color.orange_100),
                        colorResource(id = R.color.orange_200),
                        colorResource(id = R.color.orange_300),
                        colorResource(id = R.color.orange_400),
                        colorResource(id = R.color.orange_500)
                    )
                )
            ),
        contentAlignment = Alignment.TopEnd
    ) {
        IconButton(onClick = onSignOutClicked) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                contentDescription = null,
                tint = Color.Black
            )
        }
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        colorResource(id = R.color.orange_100),
                        colorResource(id = R.color.orange_200),
                        colorResource(id = R.color.orange_300),
                        colorResource(id = R.color.orange_400),
                        colorResource(id = R.color.orange_500)
                    ),
                    tileMode = TileMode.Decal
                )
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (userImgUrl != null) {
            AsyncImage(
                modifier = Modifier
                    .size(128.dp)
                    .clip(CircleShape)
                    .clickable(onClick = onUserPhotoClicked),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(userImgUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                error = painterResource(id = R.drawable.empty_profile_img)
            )
        }
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(id = R.dimen.six_level_margin))
                .padding(top = dimensionResource(id = R.dimen.two_level_margin)),
            text = userName,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            color = Color.Black
        )
    }
}

@Composable
private fun AccountInfoSection(
    modifier: Modifier,
    phoneNumber: String,
    birthdate: String,
    address: String,
    name: String,
    onAccountInfoClicked: (InfoType) -> Unit
) {
    Column(modifier = modifier.fillMaxSize()) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(id = R.dimen.two_level_margin))
                .padding(top = dimensionResource(id = R.dimen.four_level_margin)),
            text = stringResource(id = R.string.account_info),
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )
        LazyColumn(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = dimensionResource(id = R.dimen.three_level_margin))
        ) {
            items(accountInfoList) {
                Info(
                    imageId = it.imageId,
                    titleId = it.titleId,
                    description = when (it.infoType) {
                        InfoType.NAME -> { name }
                        InfoType.MOBILE -> { phoneNumber }
                        InfoType.ADDRESS -> { address }
                        InfoType.BIRTHDATE -> { birthdate }
                    },
                    infoType = it.infoType,
                    onAccountInfoClicked = onAccountInfoClicked
                )
                HorizontalDivider(modifier = modifier.fillMaxWidth())
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Info(
    modifier: Modifier = Modifier,
    imageId: Int,
    titleId: Int,
    infoType: InfoType,
    description: String,
    onAccountInfoClicked: (InfoType) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(96.dp),
        onClick = { onAccountInfoClicked(infoType) },
        shape = RoundedCornerShape(0),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier
                .fillMaxSize()
                .padding(horizontal = dimensionResource(id = R.dimen.two_level_margin)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.two_level_margin))
        ) {
            Image(
                modifier = modifier.size(48.dp),
                painter = painterResource(id = imageId),
                contentDescription = null,
                colorFilter = if (isSystemInDarkTheme()) ColorFilter.tint(colorResource(id = R.color.orange_500)) else null
            )
            Column(modifier = modifier.fillMaxWidth()) {
                Text(text = stringResource(id = titleId), fontWeight = FontWeight.Bold)
                Text(text = description, style = TextStyle(color = Color.Gray))
            }
        }
    }
}

@Composable
private fun UpdateAccountInfoDialog(
    modifier: Modifier,
    onDismissRequest: () -> Unit,
    updateValue: String,
    onUpdateValueChange: (String) -> Unit,
    onUpdateClick: () -> Unit,
    title: String,
    infoType: InfoType
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card(modifier = modifier.fillMaxWidth()) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(id = R.dimen.two_level_margin)),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Update $title",
                    style = MaterialTheme.typography.titleLarge
                )
                OutlinedTextField(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(vertical = dimensionResource(id = R.dimen.two_level_margin)),
                    value = updateValue,
                    onValueChange = onUpdateValueChange,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = when (infoType) {
                            InfoType.MOBILE -> { KeyboardType.Number }
                            else -> { KeyboardType.Text }
                        }
                    )
                )
                Row(
                    modifier = modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(onClick = onDismissRequest) {
                        Text(text = stringResource(id = R.string.cancel))
                    }
                    Spacer(modifier = modifier.width(dimensionResource(id = R.dimen.two_level_margin)))
                    Button(
                        onClick = onUpdateClick,
                        enabled = updateValue.isNotBlank()
                    ) {
                        Text(text = stringResource(id = R.string.update))
                    }
                }
            }
        }
    }
}

@Composable
private fun VerifyPhoneNumberDialog(
    modifier: Modifier,
    codeValue: String,
    onCodeValueChange: (String) -> Unit,
    verifyPhoneNumber: () -> Unit,
    onVerifyPhoneNumberDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onVerifyPhoneNumberDismiss) {
        Card(modifier = modifier.fillMaxWidth()) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(id = R.dimen.two_level_margin)),
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = stringResource(id = R.string.enter_verification_code))
                OutlinedTextField(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(vertical = dimensionResource(id = R.dimen.two_level_margin)),
                    value = codeValue,
                    onValueChange = onCodeValueChange,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                )
                Button(
                    onClick = verifyPhoneNumber,
                    enabled = codeValue.isNotBlank()
                ) {
                    Text(text = stringResource(id = R.string.verify))
                }
            }
        }
    }
}

@Composable
private fun ImageCrop(imageUri: Uri?, context: Context, uploadPhoto: (Uri?) -> Unit) {
    val imageCropper = rememberImageCropper()
    val errorMessage = stringResource(id = R.string.unknown_error)

    if (imageUri != null) {
        LaunchedEffect(true) {
            when (val result = imageCropper.crop(imageUri, context)) {
                CropResult.Cancelled -> {}
                is CropError -> {
                    Toast.makeText(
                        context,
                        errorMessage,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is CropResult.Success -> {
                    uploadPhoto(imageBitmapToFile(context, result.bitmap))
                }
            }
        }
    }

    val cropState = imageCropper.cropState
    if (cropState != null) ImageCropperDialog(state = cropState)
}

private val accountInfoList = listOf(
    AccountInfo(InfoType.NAME, R.string.name, R.drawable.ic_name),
    AccountInfo(InfoType.MOBILE, R.string.mobile, R.drawable.ic_mobile),
    AccountInfo(InfoType.ADDRESS, R.string.address, R.drawable.ic_address),
    AccountInfo(InfoType.BIRTHDATE, R.string.birthdate, R.drawable.ic_birthdate)
)

data class AccountInfo(
    val infoType: InfoType,
    val titleId: Int,
    val imageId: Int
)

enum class InfoType {
    NAME,
    MOBILE,
    ADDRESS,
    BIRTHDATE
}