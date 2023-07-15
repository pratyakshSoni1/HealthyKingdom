package com.pratyaksh.healthykingdom.ui.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.pratyaksh.healthykingdom.utils.BloodGroups
import com.pratyaksh.healthykingdom.utils.BloodGroupsInfo
import com.pratyaksh.healthykingdom.utils.LifeFluids
import com.pratyaksh.healthykingdom.utils.Plasma
import com.pratyaksh.healthykingdom.utils.PlasmaGroupInfo
import com.pratyaksh.healthykingdom.utils.PlateletsGroupInfo


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FluidInfoDialog(
    fluidType: LifeFluids,
    canDonateTo: List<BloodGroups>,
    canReceivefrom: List<BloodGroups>,
    canRecPlasmaFrom: List<Plasma>,
    canDonPlasmaTo: List<Plasma>,
    bloodGroup: BloodGroupsInfo? = null,
    plasmaGroup: PlasmaGroupInfo? = null,
    plateletsGroup: PlateletsGroupInfo? = null,
    onDismissReq: () -> Unit
) {

    Dialog(
        onDismissRequest = onDismissReq
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f)
                .verticalScroll(rememberScrollState())
                .clip(RoundedCornerShape(14.dp))
                .background(Color.White)
                .padding(horizontal = 14.dp)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                when (fluidType) {
                    LifeFluids.PLASMA -> {
                        GroupLabel(plasma = plasmaGroup!!.type, fontSize = 24.sp)
                    }

                    LifeFluids.BLOOD -> {
                        GroupLabel(
                            group = bloodGroup!!.type,
                            fontSize = 24.sp,
                            type = LifeFluids.BLOOD
                        )
                    }

                    LifeFluids.PLATELETS -> {
                        GroupLabel(
                            group = plateletsGroup!!.type,
                            fontSize = 24.sp,
                            type = LifeFluids.PLATELETS
                        )
                    }
                }
                Spacer(Modifier.width(4.dp))
                Text(
                    "Fluid Info",
                    fontWeight = FontWeight.Bold,
                    fontSize = 21.sp,
                    modifier = Modifier.weight(1f)
                )
                IconButton(icon = Icons.Rounded.Close, onClick = { onDismissReq() })
            }
            Spacer(Modifier.height(12.dp))

            Text(
                text = "Can Donate To ",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(8.dp))

            FlowRow(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x33FF6060))
                    .padding(12.dp),
                maxItemsInEachRow = 4
            ) {
                when (fluidType) {
                    LifeFluids.PLASMA -> {
                        for (item in canDonPlasmaTo) {
                            GroupLabel(plasma = item, fontSize = 24.sp)
                        }
                    }

                    LifeFluids.BLOOD, LifeFluids.PLATELETS -> {
                        for (item in canDonateTo) {
                            GroupLabel(group = item, fontSize = 18.sp, type = fluidType)
                        }
                    }
                }

            }
            Spacer(Modifier.height(21.dp))

            Text(
                text = "Can Receive From",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(8.dp))

            FlowRow(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround,
                maxItemsInEachRow = 4,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x3BDD0000))
                    .padding(12.dp)
            ) {
                when (fluidType) {
                    LifeFluids.PLASMA -> {
                        for (item in canRecPlasmaFrom) {
                            GroupLabel(plasma = item, fontSize = 24.sp)
                        }
                    }

                    LifeFluids.BLOOD, LifeFluids.PLATELETS -> {
                        for (item in canReceivefrom) {
                            GroupLabel(group = item, fontSize = 18.sp, type = fluidType)
                        }
                    }
                }

            }


        }

    }


}
