package com.pratyaksh.healthykingdom.ui.homepage.components.hospital_detail_sheet

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pratyaksh.healthykingdom.R
import com.pratyaksh.healthykingdom.ui.utils.GroupLabel
import com.pratyaksh.healthykingdom.ui.utils.IconButton
import com.pratyaksh.healthykingdom.utils.BloodGroups
import com.pratyaksh.healthykingdom.utils.LifeFluids
import com.pratyaksh.healthykingdom.utils.PlasmaGroupInfo
import com.pratyaksh.healthykingdom.utils.getOnlyGroup

@Composable
fun HospitalDetailsSheet(
    uiState: MarkerDetailSheetUiState,
    onDetailsClick: (hospitalId: String) -> Unit,
    onCloseClick: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(bottom = 16.dp, start = 14.dp, end = 14.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Box(
                Modifier
                    .width(64.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.LightGray)
            )

            Box(
                Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                IconButton(
                    icon = Icons.Rounded.Close,
                    onClick = { onCloseClick() },
                    iconColor = Color.LightGray
                )
            }
        }
        if (uiState.isLoading) {
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxHeight(0.8f)
                        .padding(top = 18.dp, bottom = 26.dp, start = 14.dp, end = 14.dp),
                    color = Color.Blue
                )
            }
        } else if (uiState.isError) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column {
                    Image(
                        painter = painterResource(R.drawable.ic_visibility),
                        contentDescription = "Error fetching details",
                        modifier = Modifier.size(64.dp),
                        colorFilter = ColorFilter.tint(color = Color(0xFFDFDFDF))
                    )
                    Spacer(Modifier.height(10.dp))
                    Text(
                        text = "Unexpected Error\n Check your internet",
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = Color.LightGray
                    )
                }
            }
        } else {
            Box(contentAlignment = Alignment.Center) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Image(
                        painter = painterResource(id = R.drawable.hospital),
                        contentDescription = uiState.hospitalName,
                        modifier = Modifier.size(48.dp)
                    )

                    Spacer(modifier = (Modifier.height(8.dp)))
                    Text(
                        text = uiState.hospitalName,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        fontSize = 16.sp,
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            FluidGroupList(
                type = LifeFluids.BLOOD,
                fluidsAvailable = uiState.availBloodTypes.getOnlyGroup()
            )
            Spacer(modifier = Modifier.height(8.dp))
            FluidGroupList(plasmaAvailable = uiState.availPlasmaTypes)
            Spacer(modifier = Modifier.height(8.dp))
            FluidGroupList(
                type = LifeFluids.PLATELETS,
                fluidsAvailable = uiState.availPlateletsTypes.getOnlyGroup()
            )
            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0x4DFFBF00))
                    .padding(8.dp)

            ) {
                if (uiState.requests != null) {
                    Column {
                        Text(
                            text = "Requests",
                            modifier = Modifier
                                .padding(top = 6.dp)
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Spacer(Modifier.height(8.dp))
                        FluidGroupList(
                            type = LifeFluids.BLOOD,
                            fluidsAvailable = uiState.requests.blood
                        )
                        Spacer(Modifier.height(6.dp))
                        FluidGroupList(
                            type = LifeFluids.PLATELETS,
                            fluidsAvailable = uiState.requests.platelets
                        )
                        Spacer(Modifier.height(6.dp))
                        FluidGroupList(plasmaAvailable = uiState.requests.plasma)
                        Spacer(Modifier.height(8.dp))
                    }
                } else {
                    Text(
                        text = "No Requests",
                        fontSize = 18.sp,
                        color = Color(0x3E000000),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
            Spacer(Modifier.height(12.dp))

            Button(
                onClick = { onDetailsClick(uiState.hospitalId) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(Color(0xFF0027FF)),
                shape = RoundedCornerShape(100.dp)
            ) {
                Text(
                    text = "Details",
                    color = Color.White,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                Icon(
                    imageVector = Icons.Rounded.KeyboardArrowRight,
                    contentDescription = null,
                    tint = Color.White
                )
            }


        }

    }

}

@Composable
private fun FluidGroupList(type: LifeFluids, fluidsAvailable: List<BloodGroups>) {

    var painterResId: Int
    var description: String

    when (type) {
        LifeFluids.PLASMA -> {
            painterResId = R.drawable.ic_plasma
            description = "available plasma groups"
        }

        LifeFluids.BLOOD -> {
            painterResId = R.drawable.ic_blood
            description = "available plasma groups"
        }

        LifeFluids.PLATELETS -> {
            painterResId = R.drawable.ic_platelets
            description = "available platelet groups"
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Image(
            painter = painterResource(painterResId),
            contentDescription = description,
            modifier = Modifier.size(43.dp)
        )

        Text(
            text = " :",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(Modifier.width(8.dp))
        if (fluidsAvailable.isNotEmpty()) {
            for (fluid in fluidsAvailable) {
                GroupLabel(type = type, group = fluid)
                Spacer(Modifier.width(8.dp))
            }
        } else {
            Icon(
                painter = painterResource(R.drawable.ic_visibility),
                contentDescription = "not available",
                modifier = Modifier.size(24.dp),
                tint = Color.Red
            )
        }
    }


}

@Composable
private fun FluidGroupList(plasmaAvailable: List<PlasmaGroupInfo>) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Image(
            painter = painterResource(R.drawable.ic_plasma),
            contentDescription = "available plasma groups",
            modifier = Modifier.size(43.dp)
        )
        Text(
            text = " :",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(Modifier.width(8.dp))
        if (plasmaAvailable.isNotEmpty()) {
            for (fluid in plasmaAvailable) {
                GroupLabel(plasma = fluid.type)
                Spacer(Modifier.width(8.dp))
            }
        } else {
            Icon(
                painter = painterResource(R.drawable.ic_visibility),
                contentDescription = "not available",
                modifier = Modifier.size(24.dp),
                tint = Color.Red
            )

        }
    }


}

