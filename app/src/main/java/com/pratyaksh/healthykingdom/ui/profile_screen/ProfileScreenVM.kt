package com.pratyaksh.healthykingdom.ui.profile_screen

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pratyaksh.healthykingdom.domain.model.Users
import com.pratyaksh.healthykingdom.domain.use_case.getHospital.GetHospitalByIdUseCase
import com.pratyaksh.healthykingdom.domain.use_case.get_ambulance.GetAmbulanceUserCase
import com.pratyaksh.healthykingdom.domain.use_case.get_public_user.GetPublicUserById
import com.pratyaksh.healthykingdom.domain.use_case.update_users.UpdateUsersUseCase
import com.pratyaksh.healthykingdom.utils.AccountTypes
import com.pratyaksh.healthykingdom.utils.Gender
import com.pratyaksh.healthykingdom.utils.Resource
import com.pratyaksh.healthykingdom.utils.identifyUserTypeFromId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch
import org.osmdroid.util.GeoPoint
import javax.inject.Inject

@HiltViewModel
class ProfileScreenVM @Inject constructor(
    private val getAmbulance: GetAmbulanceUserCase,
    private val getHospital: GetHospitalByIdUseCase,
    private val getPublicUser: GetPublicUserById,
    private val updateUserUseCase: UpdateUsersUseCase,
) : ViewModel() {

    var uiState by mutableStateOf(ProfileScreenUiState())
        private set

    fun toggleUpdateButton(isClickable: Boolean) {
        uiState = uiState.copy(
            isUpdateBtnActive = isClickable
        )
    }

    fun initScreenState(getUserId: Flow<Resource<String?>>) {
        toggleLoadingScr(true)
        viewModelScope.launch {
            getUserId.last().let {
                if (it is Resource.Success) {
                    uiState = uiState.copy(
                        currentUserId = it.data!!
                    )
                    syncDataWithFb(it.data)
                    toggleLoadingScr(false)

                } else {
                    toggleErrorDialog(true, "Can't retrieve user")
                }
            }
        }
    }

    suspend fun syncDataWithFb(userId: String) {
        if (identifyUserTypeFromId(userId)!!.equals(AccountTypes.AMBULANCE)) {
            getAmbulance.getAmbulanceByUserId(userId).last().let {
                if (it is Resource.Success)
                    uiState = uiState.copy(
                        name = it.data!!.driverName,
                        gender = when (it.data.driverGender) {
                            "M" -> Gender.MALE
                            "F" -> Gender.FEMALE
                            else -> Gender.OTHERS
                        },
                        accountType = AccountTypes.AMBULANCE,
                        age = it.data.driverAge.toString(),
                        mail = it.data.mail ?: "",
                        vehicleNumber = it.data.vehicleNumber,
                        phone = it.data.phone!!

                    )
                else
                    toggleErrorDialog(true, "Can't Retrieve details from database")

            }
        } else if (identifyUserTypeFromId(userId)!!.equals(AccountTypes.HOSPITAL)) {
            getHospital(userId).last().let {
                if (it is Resource.Success)
                    uiState = uiState.copy(
                        name = it.data!!.name,
                        location = it.data.location,
                        accountType = AccountTypes.HOSPITAL,
                        mail = it.data.mail,
                        phone = it.data.phone,
                    )
                else
                    toggleErrorDialog(true, "Can't Retrieve details from database")
            }
        } else if (identifyUserTypeFromId(userId)!!.equals(AccountTypes.PUBLIC_USER)) {
            getPublicUser(userId).last().let {
                if (it is Resource.Success)
                    uiState = uiState.copy(
                        gender = it.data!!.gender,
                        name = it.data.userName!!,
                        providesLocation = it.data.providesLocation ?: false,
                        accountType = AccountTypes.PUBLIC_USER,
                        mail = it.data.mail ?: "",
                        location = it.data.location ?: GeoPoint(0.0, 0.0),
                        age = it.data.age.toString(),
                        phone = it.data.phone!!
                    )
                else
                    toggleErrorDialog(true, "Can't Retrieve details from database")
            }
        }
    }

    fun toggleLocationChooser(setVisible: Boolean) {
        uiState = uiState.copy(
            showLocationChooser = setVisible
        )
        Log.d("VMLogs", "UIstate: $uiState")
    }

    fun onNameValueChange(newValue: String) {
        uiState = uiState.copy(
            name = newValue
        )
    }


    fun onMailValueChange(newValue: String) {
        uiState = uiState.copy(
            mail = newValue
        )
    }

    fun toggleErrorDialog(
        setToVisible: Boolean, text: String = "Something went wrong, try later", onErrorClose: ()->Unit = { Unit }
    ) {
        uiState = uiState.copy(
            errorText = text,
            showError = setToVisible,
            isLoading = false,
            showLocationChooser = false,
            onErrorCloseAction= onErrorClose
        )
    }

    fun toggleLoadingScr(setToVisible: Boolean?) {
        uiState = uiState.copy(
            isLoading = setToVisible ?: !uiState.isLoading
        )
    }

    fun onLocationValueChange(newValue: GeoPoint) {
        uiState = uiState.copy(
            location = newValue
        )
    }

    fun onGenderChange(gender: Gender) {
        uiState = uiState.copy(
            gender = gender
        )
    }

    fun toggleProvideLoc(provide: Boolean) {
        uiState = uiState.copy(
            providesLocation = provide
        )
    }

    fun onAgeChange(age: String) {
        uiState = uiState.copy(
            age = age
        )

    }

    fun onVehicleNumberChange(number: String) {
        uiState = uiState.copy(
            vehicleNumber = number
        )

    }

    fun updateDetails() {
        toggleLoadingScr(true)
        viewModelScope.launch {
            if (identifyUserTypeFromId(uiState.currentUserId!!)!!.equals(AccountTypes.AMBULANCE)) {
                var ambulance: Users.Ambulance? = null
                getAmbulance.getAmbulanceByUserId(uiState.currentUserId!!).last().let {
                    if (it is Resource.Success)
                        ambulance = it.data!!
                    else
                        toggleErrorDialog(true, "Error, Details can't be updated")
                }
                if (ambulance != null) {
                    ambulance = ambulance!!.copy(
                        uiState.name,
                        uiState.vehicleNumber,
                        driverAge = uiState.age.toInt(),
                        driverGender = when (uiState.gender) {
                            Gender.MALE -> "M"
                            Gender.FEMALE -> "F"
                            Gender.OTHERS -> "OTH"
                        },
                        mail = uiState.mail
                    )

                    updateUserUseCase.updateAmbulance(ambulance!!).last().let {
                        if (it is Resource.Success) {
                            toggleLoadingScr(false)
                            toggleEditingMode(false)
                        } else
                            toggleErrorDialog(true, "Error while updating details")
                    }
                }
            } else if (identifyUserTypeFromId(uiState.currentUserId!!)!!.equals(AccountTypes.HOSPITAL)) {
                var hospital: Users.Hospital? = null
                getHospital(uiState.currentUserId!!).last().let {
                    if (it is Resource.Success)
                        hospital = it.data!!
                    else
                        toggleErrorDialog(true, "Error, Details can't be updated")
                }
                if (hospital != null) {
                    hospital = hospital!!.copy(
                        uiState.name,
                        mail = uiState.mail,
                        location = uiState.location!!
                    )

                    updateUserUseCase.updateHospital(hospital!!).last().let {
                        if (it is Resource.Success) {
                            toggleLoadingScr(false)
                            toggleEditingMode(false)
                        } else
                            toggleErrorDialog(true, "Error while updating details")
                    }
                }
            } else if (identifyUserTypeFromId(uiState.currentUserId!!)!!.equals(AccountTypes.PUBLIC_USER)) {
                var user: Users.PublicUser? = null
                getPublicUser(uiState.currentUserId!!).last().let {
                    if (it is Resource.Success)
                        user = it.data!!
                    else
                        toggleErrorDialog(true, "Error, Details can't be updated")
                }
                if (user != null) {
                    user = user!!.copy(
                        uiState.name,
                        mail = uiState.mail,
                        location = uiState.location,
                        providesLocation = uiState.providesLocation,
                        gender= uiState.gender,
                        age = uiState.age.toInt()
                    )
                    updateUserUseCase.updatePublicUser(user!!).last().let {
                        if (it is Resource.Success) {
                            toggleLoadingScr(false)
                            toggleEditingMode(false)
                        } else
                            toggleErrorDialog(true, "Error while updating details")
                    }
                }
            }
        }

    }

    fun toggleEditingMode(setToediting: Boolean) {
        uiState = uiState.copy(
            isEditingMode = setToediting
        )
    }

    fun verifyDetails(): Boolean {

        val namePattern = Regex("^[\\p{L} .'-]+$")

        return if (uiState.name.isEmpty() || !uiState.name.contains(namePattern)) {
            toggleErrorDialog(
                true,
                "Invalid Name, please enter a valid name without any number or special symbols"
            ) {
                toggleErrorDialog(false)
            }
            false
        } else if (!uiState.age.contains(Regex("[0-9]")) && uiState.age.toInt() < 150) {
            toggleErrorDialog(true, "Invalid age") {
                toggleErrorDialog(false)
            }
            false
        } else if (identifyUserTypeFromId(uiState.currentUserId!!)!!.equals(AccountTypes.AMBULANCE) && uiState.vehicleNumber.isNullOrEmpty()) {
            toggleErrorDialog(true, "Add a valid vehicle number") {
                toggleErrorDialog(false)
            }
            false
        } else {
            true
        }

    }
}
