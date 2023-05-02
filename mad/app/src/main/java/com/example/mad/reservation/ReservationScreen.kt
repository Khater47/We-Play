package com.example.mad.reservation

import android.app.DatePickerDialog
import android.content.res.Configuration
import android.util.Log
import android.widget.DatePicker
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mad.ui.theme.MadTheme
import java.util.Calendar
import java.util.Date


@Composable
fun ReservationScreen() {

    val configuration = LocalConfiguration.current

    Box(Modifier.fillMaxSize()) {

        when (configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> {
                PortraitReservation()
            }

            else -> {
                LandscapeReservation()
            }
        }
    }
}

@Composable
fun PortraitReservation(){

    Column(
        Modifier
            .fillMaxWidth(),

        ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            CalendarContainer()
        }
        Column(
            Modifier
                .clip(RoundedCornerShape(16.dp))
                .fillMaxWidth()
                .padding(16.dp)
                .weight(2f)
                .verticalScroll(rememberScrollState()),
        ){
            ReservationListContainer()
        }
    }
}

@Composable
fun LandscapeReservation(){
    Row {
        Column(
            Modifier
                .weight(2f)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,

            ) { CalendarContainer() }
        Column(
            Modifier
                .weight(3f)
                .fillMaxHeight()
                .clip(RoundedCornerShape(16.dp))
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) { ReservationScreen() }

    }
}


@Composable
fun CalendarContainer(){

//    val calendar = Calendar.getInstance()
//
//    val context = LocalContext.current
//    val year:Int = calendar.get(Calendar.YEAR)
//    val month:Int = calendar.get(Calendar.MONTH)
//    val day:Int = calendar.get(Calendar.DAY_OF_MONTH)
//    calendar.time = Date()
//
//    val datePickerDialog = DatePickerDialog(context,
//            { _: DatePicker, y:Int, m:Int, dM:Int ->
//                Log.d("DATE","$dM/$m/$y")
//            },year,month,day
//        )
//
//
//    Button(onClick={datePickerDialog.show()}){
//        Text(text="Open Date Picker")
//    }


}

@Composable
fun ReservationListContainer(){

}



@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MadTheme {
        ReservationScreen()
    }
}
