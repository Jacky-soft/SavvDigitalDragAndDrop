package studio.apping.savvdigitaldraganddrop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
        val itemMatrix: MutableState<List<List<String>>> = remember { mutableStateOf(
            listOf(
                listOf("a","b"),
                listOf("c","d"),
                listOf("e","f"),
            )) }

        //TODO make the drag layout and drop layout less gap between the rows.
        DropLayout(itemMatrix) { from, to ->
            val matrix = mutableListOf<List<String>>()
            matrix.addAll(itemMatrix.value)
            val element = matrix.getElement(from.first, from.second)

            if (from.third) {
                matrix.removeRow(from.first)
            } else {
                matrix.removeColumn(from.first, from.second)
            }

            if (to.third) {
                matrix.addRow(to.first, element)
            } else {
                matrix.addColumn(to.first, to.second, element)
            }

            itemMatrix.value = matrix
        }
        DragLayout(itemMatrix)
    }
}

@Composable
fun DragLayout(
    dragMatrix: MutableState<List<List<String>>>
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(2.dp)) {
        Spacer(modifier = Modifier.height(30.dp))

        for (i in 0..dragMatrix.value.lastIndex) {
            val currentDragRow = dragMatrix.value[i]
            if (currentDragRow.size == 1) {
                DragBox(Modifier.height(60.dp), Triple(i, 0, true))
            } else if (currentDragRow.size == 2) {
                Row {
                   DragBox(Modifier.weight(1f).height(60.dp), Triple(i,0,false))
                   DragBox(Modifier.weight(1f).height(60.dp), Triple(i,1,false))
                }
            }
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
fun DropLayout(
    dropMatrix: MutableState<List<List<String>>>,
    updateMatrixFunc: (Triple<Int, Int, Boolean>, Triple<Int, Int, Boolean>) -> Unit
) {

    Column(
        Modifier.fillMaxSize().padding(2.dp)) {
        var matrixIndex = 0
        DropBox(modifier = Modifier
            .fillMaxWidth()
            .height(30.dp),  Triple(matrixIndex/2, 0, true)) { from, to -> updateMatrixFunc(from, to)}
        for (i in 1..dropMatrix.value.size) {
            matrixIndex += 1
            val currentRow = dropMatrix.value[matrixIndex / 2]
            val currentRowSize = currentRow.size
            if (currentRowSize < 2){
                Row {
                    DropBox(modifier = Modifier
                        .height(60.dp)
                        .weight(1f), Triple(matrixIndex/2, 0, false)) { from, to -> updateMatrixFunc(from, to)}
                    Spacer(modifier = Modifier.width(4.dp))
                    DropBox(modifier = Modifier
                        .height(60.dp)
                        .weight(1f), Triple(matrixIndex/2, 1, false)) { from, to -> updateMatrixFunc(from, to)}
                }
            } else {
                ItemArea(modifier = Modifier.height(60.dp), note = "Full $currentRow")
            }
            matrixIndex += 1
            DropBox(modifier = Modifier
                .fillMaxWidth()
                .height(30.dp), Triple(matrixIndex/2, 0, true)) { from, to -> updateMatrixFunc(from, to)
            }
        }
    }
}

@Composable
fun DragBox(modifier: Modifier, from: Triple<Int, Int, Boolean>) {
    DragTarget(
        modifier = modifier, dataToDrop = from,
    ) {
        ItemArea(modifier = modifier, note = from.toString())
    }
}

@Composable
fun DropBox(modifier: Modifier, to: Triple<Int, Int, Boolean>, newSingleElement: (Triple<Int, Int, Boolean>, Triple<Int, Int, Boolean>) -> Unit) { //TODO temMatrixIndex can be removed when refactor
    DropTarget<Triple<Int,Int,Boolean>>(
        modifier = modifier
    ) { isInBound, data ->
        val bgColor = if (isInBound) {
            Color.Red
        } else {
//                Color.White // Be transparent for dev purpose
            Color.Transparent // Transparent overlap looks confusing
        }

        data?.let {
            if (isInBound) {
                val from = it
                newSingleElement(from, to)
            }
        }

        Column(
            modifier = modifier
                .fillMaxWidth()
                .shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp))
                .background(
                    //                Color.White // Be transparent for dev purpose
                    bgColor,
                    RoundedCornerShape(16.dp)
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text =  "Drop",
                fontSize = 18.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun ItemArea(modifier: Modifier, note: String) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp))
            .background(
                                Color.White, // Be transparent for dev purpose
//                Color.Transparent,
                RoundedCornerShape(16.dp)
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text =  note,
            fontSize = 18.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )
    }
}
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SavvDigitalDragAndDropTheme {
    }
}