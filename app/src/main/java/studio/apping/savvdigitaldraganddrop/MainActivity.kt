package studio.apping.savvdigitaldraganddrop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import studio.apping.savvdigitaldraganddrop.ui.theme.SavvDigitalDragAndDropTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SavvDigitalDragAndDropTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    LongPressDraggable(modifier = Modifier.fillMaxSize()) {
        val itemNumbers = remember { mutableStateOf(0) }
        Column {
            DropOffArea(itemNumbers)
            Spacer(modifier = Modifier.weight(1f))
            ControlButtons(itemNumbers)
            DragExampleItem()
        }
    }
}

@Composable
fun DragExampleItem() {
    DragTarget(
            modifier = Modifier
                .width(200.dp)
                .height(70.dp)
                .padding(8.dp), dataToDrop = 1
        ) {
            Text(
                text = "Long Press to Drag Me",
                modifier = Modifier
                    .width(200.dp)
                    .height(70.dp)
                    .padding(8.dp)
                    .background(Color.DarkGray, RoundedCornerShape(14.dp))
            )
        }
}

@Composable
fun DropOffArea(itemNumbers: MutableState<Int>) {
    Column() {
        for (i in 1..itemNumbers.value){
            DropRow()
        }
    }
}

@Composable
fun DropRow() {
    val isDual = remember { mutableStateOf(false) }
    Row {
        val dropBoxModifier = Modifier
            .weight(1f)
            .height(60.dp)
            .padding(4.dp)

        Button(onClick = {
                val toggleValue = !isDual.value
                isDual.value = toggleValue
        }) {
            Text(text = if (isDual.value) "Mono" else "Dual")
        }
        if (isDual.value) {
            Row {
               DropBox(dropBoxModifier)
               DropBox(dropBoxModifier)
            }
        } else {
            DropBox(dropBoxModifier)
        }
    }
}

@Composable
fun DropBox(modifier: Modifier) {
    Box(modifier) {
//        Text(text = "Drop Box", modifier = Modifier.background(Color.DarkGray))
        DropTarget<Int>(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
        ) { isInBound, data ->
            val bgColor = if (isInBound) {
                Color.Red
            } else {
                Color.White
            }

            data?.let {
                if (isInBound) {
//                    foodItems[foodItem.id] = foodItem
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp))
                    .background(
                        bgColor,
                        RoundedCornerShape(16.dp)
                    ),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text =  "Drop Here",
                    fontSize = 18.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun ControlButtons(itemNumbers: MutableState<Int>) {
    Row(Modifier.fillMaxWidth()) {
        Button(onClick = { if (itemNumbers.value > 0) {
            itemNumbers.value -= 1
        } }) {
            Text(text = "1 Less Row")
        }
        Button(onClick = { if (itemNumbers.value < 6) {
            itemNumbers.value += 1
        } }) {
            Text(text = "1 More Row")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SavvDigitalDragAndDropTheme {
    }
}