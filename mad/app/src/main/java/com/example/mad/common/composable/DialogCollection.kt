package com.example.mad.common.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.mad.screens.rentField.SearchPlaygroundScreen
import com.example.mad.ui.theme.MadTheme

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

    Dialog(onDismissRequest = { dismiss() },
    ) {

        Column(Modifier.background(Color.White,shape = RoundedCornerShape(percent = 10))) {

            Column(Modifier.fillMaxWidth()){
                LazyColumn(Modifier.padding(10.dp)) {
                    itemsIndexed(data) { index, item  ->
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
                                Text(text = item,fontSize=16.sp,style=MaterialTheme.typography.bodyMedium)
                            }
                            Divider()
                        }

                    }
                }
            }

            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp)) {
                ButtonDialog(cancel = { dismiss() },
                    confirm = {
                        if (selected.value != ""){
                            setData(selected.value)
                            openDialog(false)
                        }
                    })
            }
        }

    }

}

@Preview(showBackground = true)
@Composable
fun DefaultPreviewDialog() {

    val sportList = listOf(
        "Soccer",
        "Volleyball",
        "Basket",
        "Cricket",
    )
    val (isOpenSportDialog, openSportDialog) = remember {
        mutableStateOf(false)
    }
    val (sport, setSport) = remember {
        mutableStateOf("")
    }

    MadTheme {
            DialogList(sportList,openSportDialog,setSport)
    }
}