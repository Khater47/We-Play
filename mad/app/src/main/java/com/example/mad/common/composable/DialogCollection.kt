package com.example.mad.common.composable

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.mad.MainViewModel
import com.example.mad.common.getKeyboard
import com.example.mad.model.ProfileSport
import com.example.mad.ui.theme.Bronze
import com.example.mad.ui.theme.Gold
import com.example.mad.ui.theme.MadTheme
import com.example.mad.ui.theme.Silver

@Composable
fun DialogList(
    data: List<String>,
    openDialog: (Boolean) -> Unit,
    setData: (String) -> Unit
) {

    val selected = remember {
        mutableStateOf("")
    }

    fun dismiss() = openDialog(false)

    Dialog(
        onDismissRequest = { dismiss() },
    ) {

        Column(Modifier.background(Color.White, shape = RoundedCornerShape(percent = 10))) {

            Column(Modifier.fillMaxWidth()) {
                LazyColumn(Modifier.padding(10.dp)) {
                    itemsIndexed(data) { index, item ->
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selected.value = item
                                }
                                .background(
                                    color = if (selected.value != item) Color.White else Color.Gray,
                                    shape = if (index == 0) RoundedCornerShape(
                                        topStart = 5.dp,
                                        topEnd = 5.dp
                                    ) else RectangleShape
                                )) {
                            Column(Modifier.padding(10.dp)) {
                                Text(
                                    text = item,
                                    fontSize = 16.sp,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                            Divider()
                        }

                    }
                }
            }

            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp)
            ) {
                ButtonDialog(cancel = { dismiss() },
                    confirm = {
                        setData(selected.value)
                        openDialog(false)
                    })
            }
        }

    }

}

@Composable
fun FullDialogSport(
    openDialog: (Boolean) -> Unit,
    vm:MainViewModel,
) {

    val (trophiesText,setTrophiesText) = remember {
        mutableStateOf("")
    }

    val (score,setScore) = remember {
        mutableStateOf(0)
    }

    val description = "Add your favorites sport and the related statistics, " +
            "like level from 1 to 5 and the " +
            "number of trophies that you have earn it"

    Dialog(onDismissRequest = {
        openDialog(false)
    },
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .background(
                    MaterialTheme.colorScheme.surface,
                    RectangleShape
                ),
        ) {

            /*HEADER DIALOG*/
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Column(
                    Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center) {

                    IconButton(onClick = {
                        openDialog(false)
                    },modifier=Modifier.fillMaxHeight()) {
                        Icon(imageVector = Icons.Default.Clear, contentDescription = "close dialog",
                        tint = MaterialTheme.colorScheme.onSurface)
                    }
                }
                Column(
                    Modifier
                        .weight(5f)
                        .padding(horizontal = 10.dp)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Center) {

                    Text(text = "Set sport", color = MaterialTheme.colorScheme.onSurface,
                    modifier=Modifier.fillMaxWidth(),
                    fontSize=20.sp,style=MaterialTheme.typography.bodyMedium)
                }
                Column(
                    Modifier
                        .weight(2f)
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            val trophies = if(trophiesText=="") 0L else trophiesText.toLong()


                            val ps = ProfileSport(sport="Basketball",level=score.toLong(),trophies)
                            val userId = "f9SYx0LJM3TSDxUFMcX6JEwcaxh1"

                            vm.insertUserProfileSport(userId,ps)
                            openDialog(false)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            contentColor = MaterialTheme.colorScheme.primary
                        ),
                        shape= RectangleShape,
                        modifier=Modifier.fillMaxHeight()
                    ) {
                        Text(text = "Save")
                    }
                }
            }

            /*DESCRIPTION DIALOG TEXT*/
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(15.dp)){
                Text(description,fontSize=15.sp,style=MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface)
            }



            /*LEVEL*/
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center) {
                Text(text="Level",fontSize=20.sp,style=MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface)
            }

            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center) {
                IconButtonRating(score,setScore)
            }



            MedalItemDialog(text = "Trophies", medal = trophiesText, setMedal = setTrophiesText)


        }
    }


}

@Composable
fun ListContainerDialog(data:List<String>){

    //TODO RADIO BUTTON
    LazyColumn{
        items(data){
            item ->
                Column(
                    Modifier
                        .fillMaxWidth()
                        ) {

                }
        }
    }
}


@Composable
fun MedalItemDialog(
    text:String,
    medal:String,
    setMedal:(String)->Unit,
){


    Column(
        Modifier
            .fillMaxWidth()
            .padding(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {

        Text(text = text,fontSize=20.sp,style=MaterialTheme.typography.bodyMedium)
    }
    Column(
        Modifier
            .fillMaxWidth()
            .padding(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {

        OutlinedTextField(value = medal,
            onValueChange = {setMedal(it)
            },
            label={ Text(text = text)},
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
        )


    }
}


//@Preview(showBackground = true,device="spec:width=411dp,height=891dp")
//@Composable
//fun DefaultPreviewDialog() {
//
//    val sportList = listOf(
//        "Soccer",
//        "Volleyball",
//        "Basket",
//        "Cricket",
//    )
//    val (isOpenSportDialog, openSportDialog) = remember {
//        mutableStateOf(false)
//    }
//    val (sport, setSport) = remember {
//        mutableStateOf(0)
//    }
//    val (gold,setGold) = remember {
//        mutableStateOf(0)
//    }
//    val (silver,setSilver) = remember {
//        mutableStateOf(0)
//    }
//    val (bronze,setBronze) = remember {
//        mutableStateOf(0)
//    }
//
//
//    MadTheme {
////            DialogList(sportList,openSportDialog,setSport)
//        FullDialogSport(openSportDialog)
//    }
//}

//device="spec:width=411dp,height=891dp,orientation=landscape"