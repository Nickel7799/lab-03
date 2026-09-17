package com.example.listycity3

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.listycity3.ui.theme.ListyCity3Theme

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem


@Composable
fun CityListScreen(
    cities: List<City>,
    onUpdateCity: (City, City) -> Unit,
    onAddCity: (City) -> Unit,
    modifier: Modifier = Modifier


) {
    var newCityName by remember { mutableStateOf("") }
    var newProvinceName by remember { mutableStateOf("") }
    var newUpdateCityIndex by remember { mutableStateOf(0) }
    var showAddCityFields by remember {mutableStateOf(false) }
    Column(modifier = modifier){
        Row(
            modifier= Modifier.fillMaxWidth(),
            Arrangement.End
        ){
            FloatingActionButton(
                modifier=Modifier.padding(24.dp),
                onClick = {showAddCityFields = !showAddCityFields}
            ) {
                Text("+")
            }

        }
        //the following if statement was modified with the use of Claude for some reason all three feals are too close together horizontally, while I want them under each other. also I can't enter anything to the index field.
        if (showAddCityFields) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = newCityName,
                    onValueChange = { newCityName = it },
                    label = { Text("City") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = newProvinceName,
                    onValueChange = { newProvinceName = it },
                    label = { Text("provinces") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                var expanded by remember { mutableStateOf(false) }

                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = if (newUpdateCityIndex > 0) newUpdateCityIndex.toString() else "",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("City's number") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    // Invisible overlay that actually captures the tap
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .clickable { expanded = true }
                    )
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        (1..cities.size).forEach { number ->
                            DropdownMenuItem(
                                text = { Text(number.toString()) },
                                onClick = {
                                    newUpdateCityIndex = number
                                    expanded = false
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))

                Row {
                    Button(
                        modifier = Modifier.padding(vertical = 2.dp),
                        onClick = {
                            if (newCityName.isNotBlank() && newProvinceName.isNotBlank()) {
                                onAddCity(
                                    City(name = newCityName, province = newProvinceName)
                                )
                            }
                            newCityName = ""
                            newProvinceName = ""
                            showAddCityFields = false
                        }
                    ) {
                        Text("Add City")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                   Button( // update
                        modifier = Modifier.padding(vertical = 2.dp),
                        onClick = {
                            val oldCity = cities.getOrNull(newUpdateCityIndex - 1)
                            if (oldCity != null && newCityName.isNotBlank() && newProvinceName.isNotBlank()) {
                                onUpdateCity(
                                    oldCity,
                                    City(name = newCityName, province = newProvinceName)
                                )
                            }
                            newCityName = ""
                            newProvinceName = ""
                            newUpdateCityIndex = 0
                            showAddCityFields = false
                        }
                    ) {
                        Text("Update City")
                    }
                }
            }
        }
        LazyColumn(modifier = Modifier) {
            itemsIndexed(cities) { index, city ->
                CityRow(city = city)
                if (index < cities.lastIndex) {
                    HorizontalDivider()
                }

            }
        }
        }


}

@Composable
fun CityRow(city: City) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Text(
            text = city.name,
            fontSize = 30.sp,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = city.province,
            fontSize = 30.sp,
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CityListScreenPreview() {
    ListyCity3Theme {
        CityListScreen(
            cities = listOf(
                City("Edmonton", "AB"),
                City("Vancouver", "BC"),
                City("Calgary", "AB")
            ),
            onAddCity = {},
            onUpdateCity = { _, _ -> } //from Claude at the last statement in the file I get "Argument type mismatch: actual type is 'Function0<Unit>', but 'Function2<City, City, Unit>' was expected."
        )
    }
}