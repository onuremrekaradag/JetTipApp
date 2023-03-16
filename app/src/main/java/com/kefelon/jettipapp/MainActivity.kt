package com.kefelon.jettipapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kefelon.jettipapp.ui.theme.JetTipAppTheme
import com.kefelon.jettipapp.ui.theme.Purple500

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetTipAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MyApp {
                        Text(text = "deneme", fontSize = 100.sp, modifier = Modifier.fillMaxSize())
                    }
                }
            }
        }
    }


    @Composable
    fun MyApp(content: @Composable () -> Unit) {
        var sliderPosition by remember { mutableStateOf(0f) }
        var enterBillTextInt by remember { mutableStateOf(0) }
        var splitCount by remember { mutableStateOf(1) }

        Column {
            TopHeader(
                enterBillTextInt = enterBillTextInt,
                sliderPosition = sliderPosition,
                splitCount = splitCount
            )
            Calculator(
                sliderPosition = sliderPosition, updateSliderPosition = {
                    sliderPosition = it
                },
                enterBillTextInt = enterBillTextInt, updateEnterBillTextField = {
                    enterBillTextInt = it
                },
                splitCount = splitCount, updateSplitCount = {
                    splitCount = it
                })
        }

    }

    @Preview
    @Composable
    fun TopHeader(enterBillTextInt: Int = 0, sliderPosition: Float = 0f, splitCount: Int = 0) {

        JetTipAppTheme() {
            Card(
                modifier = Modifier
                    .padding(20.dp)
                    .padding(top = 10.dp)
                    .fillMaxWidth()
                    .height(150.dp),
                shape = RoundedCornerShape(corner = CornerSize(8)),
                backgroundColor = MaterialTheme.colors.primary,
            ) {

                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Total Per Person",
                        fontWeight = FontWeight(900),
                        fontSize = 25.sp,
                        color = Color.Black
                    )
                    Text(
                        text = "$" + String.format(
                            "%.2f",
                            (enterBillTextInt + (enterBillTextInt * sliderPosition) / 100) / splitCount
                        ),
                        fontSize = 45.sp,
                        fontWeight = FontWeight(900),
                        color = Color.Black
                    )

                }


            }
        }
    }

    @Preview
    @Composable
    fun Calculator(
        sliderPosition: Float = 0f,
        updateSliderPosition: ((Float) -> Unit)? = null,
        enterBillTextInt: Int = 0,
        updateEnterBillTextField: ((Int) -> Unit)? = null,
        splitCount: Int = 0,
        updateSplitCount: ((Int) -> Unit)? = null,
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            border = BorderStroke(width = 2.dp, color = Color.LightGray),
            shape = RoundedCornerShape(2)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                EnterBill(enterBillTextInt = enterBillTextInt, updateEnterBillTextField = {
                    updateEnterBillTextField?.invoke(it)
                })

                if (enterBillTextInt != 0) {
                    Split(splitCount = splitCount, updateSplitCount = {
                        updateSplitCount?.invoke(it)
                    })
                    Tip(enterBillTextInt = enterBillTextInt, sliderPosition = sliderPosition)
                    TipPercentage(sliderPosition = sliderPosition, updateSliderPosition = {
                        updateSliderPosition?.invoke(it)
                    })
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    private fun EnterBill(
        enterBillTextInt: Int = 0,
        updateEnterBillTextField: ((Int) -> Unit)? = null
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .padding(start = 10.dp, end = 10.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            value = if (enterBillTextInt == 0) "" else "$enterBillTextInt",
            onValueChange = {
                if (it.isNotEmpty()) {
                    updateEnterBillTextField?.invoke(it.toInt())
                } else {
                    updateEnterBillTextField?.invoke(0)
                }
            },
            label = {
                Text("Enter Bill", color = Purple500)
            },
            leadingIcon = {
                Image(
                    painter = painterResource(id = R.drawable.baseline_attach_money_24),
                    contentDescription = "",
                    colorFilter = ColorFilter.tint(Color.Gray)
                )
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colors.primaryVariant,
                unfocusedBorderColor = MaterialTheme.colors.primaryVariant
            )
        )
    }

    @Preview(showBackground = true)
    @Composable
    private fun Split(splitCount: Int = 0, updateSplitCount: ((Int) -> Unit)? = null) {
        Row(
            modifier =
            Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Split", fontSize = 18.sp)

            Spacer(modifier = Modifier.weight(1f))

            Card(
                modifier = Modifier
                    .size(40.dp), shape = CircleShape, elevation = 4.dp
            ) {
                Image(
                    modifier = Modifier.clickable {
                        if (splitCount != 1) {
                            updateSplitCount?.invoke(splitCount - 1)
                        }
                    }, painter = painterResource(id = R.drawable.baseline_remove_24),
                    contentDescription = "", colorFilter = ColorFilter.tint(Color.Black)
                )
            }

            Text(
                modifier = Modifier.padding(start = 13.dp, end = 13.dp),
                text = "$splitCount",
                fontSize = 17.sp
            )

            Card(
                modifier = Modifier
                    .size(40.dp), shape = CircleShape, elevation = 4.dp
            ) {
                Image(
                    modifier = Modifier.clickable {
                        updateSplitCount?.invoke(splitCount + 1)
                    }, painter = painterResource(id = R.drawable.baseline_add_24),
                    contentDescription = "",
                    colorFilter = ColorFilter.tint(Color.Black)
                )
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun Tip(enterBillTextInt: Int = 0, sliderPosition: Float = 0f) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Text(text = "Tip", fontSize = 18.sp)

            Spacer(modifier = Modifier.weight(1f))

            Text(text = "$${(enterBillTextInt * sliderPosition.toInt()) / 100}", fontSize = 18.sp)
        }
    }


    @Preview(showBackground = true)
    @Composable
    fun TipPercentage(
        sliderPosition: Float = 0f,
        updateSliderPosition: ((Float) -> Unit)? = null
    ) {
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "${sliderPosition.toInt()} %", fontSize = 18.sp)

            Slider(
                modifier = Modifier.padding(20.dp),
                value = sliderPosition,
                valueRange = 0f..100f,
                steps = 5,
                onValueChange = {
                    updateSliderPosition?.invoke(it)
                })
        }
    }
}


