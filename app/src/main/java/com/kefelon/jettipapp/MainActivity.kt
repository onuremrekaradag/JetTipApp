package com.kefelon.jettipapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kefelon.jettipapp.components.InputField
import com.kefelon.jettipapp.ui.theme.JetTipAppTheme
import com.kefelon.jettipapp.widgets.RoundIconButton

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
                        Text(text = "deneme", fontSize = 10.sp, modifier = Modifier.fillMaxSize())
                    }
                }
            }
        }
    }


    @Composable
    fun MyApp(content: @Composable () -> Unit) {
        var sliderPosition by remember { mutableStateOf(0f) }
        var enterBillText by remember { mutableStateOf("") }
        var splitCount by remember { mutableStateOf(1) }
        var validState by remember(enterBillText) {
            mutableStateOf(
                enterBillText.trim().isNotEmpty()
            )
        }

        Column {
            TopHeader(
                enterBillText = enterBillText,
                sliderPosition = sliderPosition,
                splitCount = splitCount,
                validState = validState
            )
            Calculator(
                sliderPosition = sliderPosition, updateSliderPosition = {
                    sliderPosition = it
                },
                enterBillText = enterBillText, updateEnterBillTextField = {
                    enterBillText = it
                },
                splitCount = splitCount, updateSplitCount = {
                    splitCount = it
                },
                validState = validState, updateValidState = {
                    validState = it
                }
            )
            content.invoke()
        }

    }

    @Preview
    @Composable
    fun TopHeader(
        enterBillText: String = "",
        sliderPosition: Float = 0f,
        splitCount: Int = 0,
        validState: Boolean = false
    ) {

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
                        text = "$" + if (validState) String.format(
                            "%.2f",
                            (Integer.parseInt(enterBillText) +
                                    (Integer.parseInt(enterBillText) * sliderPosition) / 100)
                                    / splitCount
                        ) else "",
                        fontSize = 45.sp,
                        fontWeight = FontWeight(900),
                        color = Color.Black
                    )

                }


            }
        }
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Preview
    @Composable
    fun Calculator(
        sliderPosition: Float = 0f,
        updateSliderPosition: ((Float) -> Unit)? = null,
        enterBillText: String = "",
        updateEnterBillTextField: ((String) -> Unit)? = null,
        splitCount: Int = 0,
        updateSplitCount: ((Int) -> Unit)? = null,
        validState: Boolean = true,
        updateValidState: ((Boolean) -> Unit)? = null
    ) {


        val keyboardController = LocalSoftwareKeyboardController.current

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            border = BorderStroke(width = 2.dp, color = Color.LightGray),
            shape = RoundedCornerShape(8)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                InputField(modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .padding(start = 10.dp, end = 10.dp),
                    onAction = KeyboardActions {
                        if (!validState) return@KeyboardActions
                        keyboardController?.hide()
                    },
                    labelId = "Enter Bill",
                    isSingleLine = true,
                    enabled = true,
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Previous,
                    enterBillText = enterBillText,
                    updateEnterBillTextField = {
                        updateEnterBillTextField?.invoke(it)
                    })

                if (validState) {
                    Split(splitCount = splitCount, updateSplitCount = {
                        updateSplitCount?.invoke(it)
                    })
                    Tip(
                        enterBillText = enterBillText,
                        sliderPosition = sliderPosition,
                        validState = validState
                    )
                    TipPercentage(sliderPosition = sliderPosition, updateSliderPosition = {
                        updateSliderPosition?.invoke(it)
                    })
                }
            }
        }
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

            RoundIconButton(
                modifier = Modifier,
                elevation = 4.dp,
                imageVector = ImageVector.vectorResource(
                    id = R.drawable.baseline_remove_24
                ), onClick = {
                    if (splitCount != 1) {
                        updateSplitCount?.invoke(splitCount - 1)
                    }
                })

            Text(
                modifier = Modifier.padding(start = 13.dp, end = 13.dp),
                text = "$splitCount",
                fontSize = 17.sp
            )

            RoundIconButton(modifier = Modifier
                .size(40.dp),
                elevation = 4.dp,
                imageVector = ImageVector.vectorResource(id = R.drawable.baseline_add_24),
                onClick = {
                    updateSplitCount?.invoke(splitCount + 1)
                })
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun Tip(enterBillText: String = "", sliderPosition: Float = 0f, validState: Boolean = false) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Text(text = "Tip", fontSize = 18.sp)

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "$" + if (validState) "${
                    (Integer.parseInt(enterBillText) *
                            sliderPosition.toInt()) / 100
                }" else "",
                fontSize = 18.sp
            )
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


