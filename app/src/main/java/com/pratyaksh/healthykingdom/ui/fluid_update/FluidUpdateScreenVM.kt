package com.pratyaksh.healthykingdom.ui.fluid_update

import androidx.lifecycle.ViewModel
import com.pratyaksh.healthykingdom.domain.use_case.getFluidsData.GetSpecificFluidByHospitalUseCase
import com.pratyaksh.healthykingdom.domain.use_case.update_lifefluids.UpdateLifeFluidsUseCase
import com.pratyaksh.healthykingdom.utils.LifeFluids
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FluidUpdateScreenVM @Inject constructor(
    private val updateFluidUseCase: UpdateLifeFluidsUseCase,
    private val getFluidUseCase: GetSpecificFluidByHospitalUseCase
): ViewModel() {

    fun onUpdateFluid(){

    }

    fun initScreen( userId: String ){

    }

//    fun onUpdateBloodGroup(newValue){}

}