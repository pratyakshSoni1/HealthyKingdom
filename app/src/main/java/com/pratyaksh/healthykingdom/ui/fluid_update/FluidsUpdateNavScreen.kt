package com.pratyaksh.healthykingdom.ui.fluid_update

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.pratyaksh.healthykingdom.R
import com.pratyaksh.healthykingdom.ui.utils.SimpleTopBar
import com.pratyaksh.healthykingdom.utils.LifeFluids
import com.pratyaksh.healthykingdom.utils.Resource
import com.pratyaksh.healthykingdom.utils.Routes
import kotlinx.coroutines.flow.Flow

@Composable
fun FluidsUpdateNavScreen(
    navController: NavHostController
) {

    Scaffold(
        topBar = {
            SimpleTopBar(onBackPress = { navController.popBackStack() }, title = "Update Fluids")
        }
    ) {
        Column(
            Modifier.padding(it),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            NavMenuItem(
                title = "Blood Data", imageIcon = painterResource(id = R.drawable.ic_blood),
                onClick = { navController.navigate(Routes.FLUIDS_UPDATION_SCREEN.withArgs(LifeFluids.BLOOD)) }
            )
            Divider()

            NavMenuItem(
                title = "Platelets Data", imageIcon = painterResource(id = R.drawable.ic_platelets),
                onClick = { navController.navigate(Routes.FLUIDS_UPDATION_SCREEN.withArgs(LifeFluids.PLATELETS)) }
            )
            Divider()

            NavMenuItem(
                title = "Plasma Data", imageIcon = painterResource(id = R.drawable.ic_plasma),
                onClick = { navController.navigate(Routes.FLUIDS_UPDATION_SCREEN.withArgs(LifeFluids.PLASMA)) }
            )
            Divider()

        }
    }

}
@Composable
fun Divider(){
    Spacer(
        Modifier
            .height(1.5.dp)
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(Color.LightGray)
            .clip(RoundedCornerShape(100.dp))
    )
}

@Composable
fun NavMenuItem(
    title: String,
    imageIcon: Painter,
    onClick:()->Unit
){

    Row(
        modifier= Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 6.dp, horizontal = 14.dp),
        verticalAlignment= Alignment.CenterVertically
    ){

        Image(
            modifier= Modifier.size(36.dp),
            painter = imageIcon,
            contentDescription = null
        )

        Spacer(Modifier.width(6.dp))

        Text(text= title, fontSize= 18.sp, modifier= Modifier.weight(1f))
        Icon(imageVector = Icons.Rounded.KeyboardArrowRight, contentDescription = null, modifier=Modifier.size(32.dp))

    }

}

@Composable
fun NavMenuItem(
    title: String,
    imageIcon: ImageVector,
    onClick:()->Unit
){

    Row(
        modifier= Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 6.dp, horizontal = 14.dp),
        verticalAlignment= Alignment.CenterVertically
    ){

        Icon(
            modifier= Modifier.size(36.dp),
            imageVector = imageIcon,
            contentDescription = null
        )
        Spacer(Modifier.width(6.dp))

        Text(text= title, fontSize= 18.sp, modifier= Modifier.weight(1f))
        Icon(imageVector = Icons.Rounded.KeyboardArrowRight, contentDescription = null, modifier=Modifier.size(32.dp))

    }

}