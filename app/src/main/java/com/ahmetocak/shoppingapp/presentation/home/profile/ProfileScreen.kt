package com.ahmetocak.shoppingapp.presentation.home.profile

import android.app.Activity
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
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
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ahmetocak.shoppingapp.R
import com.ahmetocak.shoppingapp.common.helpers.formatDate
import com.ahmetocak.shoppingapp.presentation.designsystem.components.ShoppingScaffold
import com.ahmetocak.shoppingapp.presentation.designsystem.components.ShoppingShowToastMessage
import com.ahmetocak.shoppingapp.presentation.designsystem.theme.ShoppingAppTheme
import com.ahmetocak.shoppingapp.presentation.home.HomeSections
import com.ahmetocak.shoppingapp.presentation.home.ShoppingAppBottomBar
import com.ahmetocak.shoppingapp.presentation.home.profile.dialogs.DeleteAccountDialog
import com.ahmetocak.shoppingapp.presentation.home.profile.dialogs.ImageCropper
import com.ahmetocak.shoppingapp.presentation.home.profile.dialogs.UpdateAccountInfoDialog
import com.ahmetocak.shoppingapp.presentation.home.profile.dialogs.VerifyPhoneNumberDialog
import com.ahmetocak.shoppingapp.utils.CustomPreview

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    onSignOutClicked: () -> Unit,
    onNavigateRoute: (String) -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    var imageUri by remember { mutableStateOf<Uri?>(null) }

    if (uiState.errorMessages.isNotEmpty()) {
        ShoppingShowToastMessage(message = uiState.errorMessages.first().asString())
        viewModel.consumedErrorMessage()
    }

    if (uiState.userMessages.isNotEmpty()) {
        ShoppingShowToastMessage(message = uiState.userMessages.first().asString())
        viewModel.consumedUserMessage()
        viewModel.endDialog(DialogType.UPDATE_ACCOUNT_INFO)
    }

    if (uiState.deleteAccountState.isDeleteSuccess) {
        ShoppingShowToastMessage(message = stringResource(id = R.string.delete_account_success))
        onSignOutClicked()
    }

    when (uiState.verifyPhoneNumberState.verifyPhoneNumberUiEvent) {
        VerifyPhoneNumberUiEvent.Nothing -> {}

        VerifyPhoneNumberUiEvent.OnVerificationComplete -> {
            ShoppingShowToastMessage(message = stringResource(id = R.string.verification_completed))
            viewModel.resetVerifyNumberState()
        }

        VerifyPhoneNumberUiEvent.OnCodeSent -> {
            viewModel.startDialog(DialogType.VERIFY_PHONE_NUMBER)
        }
    }

    DeleteAccountSection(uiState.deleteAccountState.dialogState, viewModel)

    VerifyPhoneNumberDialogSection(uiState.verifyPhoneNumberState.dialogState, viewModel)

    UpdateAccountInfoDialogSection(uiState.updateAccountInfoDialogState, viewModel)

    ImageCropperDialogSection(uiState.imageCropperDialogUiState, imageUri.toString(), viewModel)

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
        viewModel.startDialog(DialogType.IMAGE_CROPPER)
    }

    ShoppingScaffold(
        modifier = modifier,
        bottomBar = {
            ShoppingAppBottomBar(
                tabs = HomeSections.values(),
                currentRoute = HomeSections.PROFILE.route,
                navigateToRoute = onNavigateRoute
            )
        }
    ) { paddingValues ->
        ProfileScreenContent(
            modifier = Modifier.padding(paddingValues),
            userImgUrl = uiState.photoUrl.toString(),
            userName = uiState.name ?: "",
            phoneNumber = uiState.phoneNumber ?: "",
            address = uiState.userDetail?.address ?: "",
            birthdate = uiState.userDetail?.birthdate?.formatDate() ?: "",
            onAccountInfoClicked = remember(viewModel) {
                {
                    viewModel.setAccountInfoType(it)
                    viewModel.startDialog(DialogType.UPDATE_ACCOUNT_INFO)
                }
            },
            onUserPhotoClicked = remember { { launcher.launch("image/*") } },
            onSignOutClicked = onSignOutClicked,
            onDeleteAccountClicked = remember(viewModel) {
                { viewModel.startDialog(DialogType.DELETE_ACCOUNT) }
            }
        )
    }
}

@Composable
private fun ImageCropperDialogSection(
    dialogState: DialogUiState,
    imageUri: String?,
    viewModel: ProfileViewModel
) {
    when (dialogState) {
        DialogUiState.DialogInactive -> {}
        DialogUiState.DialogActive -> {
            ImageCropper(
                imageUri = imageUri,
                context = LocalContext.current,
                uploadPhoto = remember(viewModel) {
                    {
                        if (it != null) {
                            viewModel.endDialog(DialogType.IMAGE_CROPPER)
                            viewModel.updateUserPhoto(it)
                        }
                    }
                }
            )
        }
    }
}


@Composable
private fun ProfileScreenContent(
    modifier: Modifier,
    userImgUrl: String?,
    userName: String,
    phoneNumber: String,
    address: String,
    birthdate: String,
    onAccountInfoClicked: (InfoType) -> Unit,
    onUserPhotoClicked: () -> Unit,
    onSignOutClicked: () -> Unit,
    onDeleteAccountClicked: () -> Unit
) {
    Surface(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            ProfileSection(
                modifier = Modifier.weight(3f),
                userImgUrl = userImgUrl,
                userName = userName,
                onUserPhotoClicked = onUserPhotoClicked,
                onSignOutClicked = onSignOutClicked,
                onDeleteAccountClicked = onDeleteAccountClicked
            )
            AccountInfoSection(
                modifier = Modifier.weight(4f),
                phoneNumber = phoneNumber,
                address = address,
                birthdate = birthdate,
                name = userName,
                onAccountInfoClicked = onAccountInfoClicked
            )
        }
    }
}

@Composable
private fun ProfileSection(
    modifier: Modifier,
    userImgUrl: String?,
    userName: String,
    onUserPhotoClicked: () -> Unit,
    onSignOutClicked: () -> Unit,
    onDeleteAccountClicked: () -> Unit
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
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            IconButton(onClick = onDeleteAccountClicked) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = null,
                    tint = Color.Black
                )
            }
            IconButton(onClick = onSignOutClicked) {
                Icon(
                    imageVector = Icons.Filled.ExitToApp,
                    contentDescription = null,
                    tint = Color.Black
                )
            }
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
        AsyncImage(
            modifier = Modifier
                .size(128.dp)
                .clip(CircleShape)
                .clickable(onClick = onUserPhotoClicked),
            model = ImageRequest.Builder(LocalContext.current)
                .data(Uri.parse(userImgUrl))
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            error = painterResource(id = R.drawable.empty_profile_img),
            placeholder = painterResource(id = R.drawable.empty_profile_img)
        )
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
                InfoItem(
                    imageId = it.imageId,
                    titleId = it.titleId,
                    description = when (it.infoType) {
                        InfoType.NAME -> {
                            name
                        }

                        InfoType.MOBILE -> {
                            phoneNumber
                        }

                        InfoType.ADDRESS -> {
                            address
                        }

                        InfoType.BIRTHDATE -> {
                            birthdate
                        }
                    },
                    infoType = it.infoType,
                    onAccountInfoClicked = onAccountInfoClicked
                )
                Divider(modifier = modifier.fillMaxWidth())
            }
        }
    }
}

private val accountInfoList = listOf(
    AccountInfo(InfoType.NAME, R.string.name, R.drawable.ic_name),
    AccountInfo(InfoType.MOBILE, R.string.mobile, R.drawable.ic_mobile),
    AccountInfo(InfoType.ADDRESS, R.string.address, R.drawable.ic_address),
    AccountInfo(InfoType.BIRTHDATE, R.string.birthdate, R.drawable.ic_birthdate)
)

@Immutable
data class AccountInfo(
    val infoType: InfoType,
    val titleId: Int,
    val imageId: Int
)

@Immutable
enum class InfoType {
    NAME,
    MOBILE,
    ADDRESS,
    BIRTHDATE
}

@Composable
private fun DeleteAccountSection(
    dialogState: DialogUiState,
    viewModel: ProfileViewModel
) {
    when (dialogState) {
        DialogUiState.DialogActive -> {
            DeleteAccountDialog(
                onDismissRequest = remember(viewModel) { { viewModel.endDialog(DialogType.DELETE_ACCOUNT) } },
                onDeleteClick = remember(viewModel) { { viewModel.deleteAccount() } },
                emailValue = viewModel.email,
                passwordValue = viewModel.password,
                onEmailValChange = remember(viewModel) { { viewModel.updateEmailValue(it) } },
                onPasswordValChange = remember(viewModel) { { viewModel.updatePasswordValue(it) } }
            )
        }

        DialogUiState.DialogInactive -> {}
    }
}

@Composable
private fun VerifyPhoneNumberDialogSection(
    dialogState: DialogUiState,
    viewModel: ProfileViewModel
) {
    when (dialogState) {
        DialogUiState.DialogInactive -> {}
        DialogUiState.DialogActive -> {
            VerifyPhoneNumberDialog(
                codeValue = viewModel.verificationCode,
                onCodeValueChange = viewModel::updateVerificationCodeValue,
                verifyPhoneNumber = viewModel::verifyUserPhoneNumber,
                onVerifyPhoneNumberDismiss = remember(viewModel) {
                    { viewModel.endDialog(DialogType.VERIFY_PHONE_NUMBER) }
                }
            )
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun UpdateAccountInfoDialogSection(
    dialogState: DialogUiState,
    viewModel: ProfileViewModel
) {
    val datePickerState = rememberDatePickerState()

    val activity = LocalContext.current as Activity

    when (dialogState) {
        DialogUiState.DialogInactive -> {}
        DialogUiState.DialogActive -> {
            when (viewModel.infoType) {
                InfoType.BIRTHDATE -> {
                    DatePickerDialog(
                        onDismissRequest = { viewModel.endDialog(DialogType.UPDATE_ACCOUNT_INFO) },
                        confirmButton = {
                            TextButton(onClick = {
                                datePickerState.selectedDateMillis?.let {
                                    viewModel.updateUserBirthdate(it)
                                }
                                viewModel.endDialog(DialogType.UPDATE_ACCOUNT_INFO)
                            }) {
                                Text(text = stringResource(id = R.string.confirm))
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = {
                                viewModel.endDialog(DialogType.UPDATE_ACCOUNT_INFO)
                            }) {
                                Text(text = stringResource(id = R.string.cancel))
                            }
                        }
                    ) {
                        DatePicker(state = datePickerState)
                    }
                }

                else -> {
                    UpdateAccountInfoDialog(
                        onDismissRequest = remember(viewModel) {
                            {
                                viewModel.clearAccountInfoValue()
                                viewModel.endDialog(DialogType.UPDATE_ACCOUNT_INFO)
                            }
                        },
                        updateValue = viewModel.updateValue,
                        onUpdateValueChange = viewModel::updateAccountInfoValue,
                        onUpdateClick = remember(viewModel) {
                            {
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
                            }
                        },
                        title = when (viewModel.infoType) {
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
                        infoType = viewModel.infoType
                    )
                }
            }
        }
    }
}

@CustomPreview
@Composable
private fun ProfileScreenPreview() {
    ShoppingAppTheme {
        Surface {
            ProfileScreenContent(
                modifier = Modifier,
                userImgUrl = "",
                userName = "Ahmet Ocak",
                phoneNumber = "",
                address = "Mars",
                birthdate = "",
                onAccountInfoClicked = {},
                onUserPhotoClicked = {},
                onSignOutClicked = {},
                onDeleteAccountClicked = {}
            )
        }
    }
}