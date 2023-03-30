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
        val itemMatrix: MutableState<List<List<String>>> = remember { mutableStateOf(
            listOf(
                listOf("a", "b"),
                listOf("c", "d"),
                listOf("f"),
                listOf("g"),
//                listOf("i", "j")
            )) }
        val newRow: (Int, String) -> Unit = { atIndex: Int, newElement: String ->
            val matrix = mutableListOf<List<String>>()
            itemMatrix.value.forEach {
                matrix.add(it)
            }
            matrix.add(atIndex, listOf(newElement))
            itemMatrix.value = matrix
        }

        val newColumn: (Int, String, Boolean) -> Unit = { atIndex: Int, newElement: String, onLeft: Boolean ->
            val existingElement = itemMatrix.value[atIndex].first()
            val updatedRow = if (onLeft) listOf(newElement, existingElement) else listOf(existingElement, newElement)
            val matrix = mutableListOf<List<String>>()
            itemMatrix.value.forEach {
                matrix.add(it)
            }
            matrix[atIndex] = updatedRow
            itemMatrix.value = matrix
        }

//        DragLayout(itemMatrix)
        DropLayout(itemMatrix,
            { index, element ->
                newRow(index, element)
            },
            { index, element, leftOrRight ->
                newColumn(index,element, leftOrRight)
        })
    }
}

// TODO Drag layout, it can be one row long or half. Use same logic as drop to create rows,
//  but there is no small one in between. Drop update the matrix, drag is a copy of data,
//  it need a value of the current element, the layout also resizable like the drop layout.

@Composable
fun DropLayout(
    dropMatrix: MutableState<List<List<String>>>,
    createNewRowAt: (Int, String) -> Unit,
    createNewColumnAt: (Int, String, Boolean) -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(2.dp)) {

        var matrixIndex = 0
        DropToCreateNewRow(dropMatrix, matrixIndex, createNewRowAt)
        for (i in 1..dropMatrix.value.size) {
            matrixIndex += 1
            DropToInsertInRow(dropMatrix, matrixIndex, createNewColumnAt)
            matrixIndex += 1
            DropToCreateNewRow(dropMatrix, matrixIndex, createNewRowAt)
        }
    }
}

@Composable
fun DropToInsertInRow(dropMatrix: MutableState<List<List<String>>>, matrixIndex: Int,
                      createNewRowAt: (Int, String, Boolean) -> Unit) {
    val modifier = Modifier.height(70.dp)
    val currentRow = dropMatrix.value[matrixIndex / 2]
    val currentRowSize = currentRow.size
    if (currentRowSize < 2){
        Row { //TODO disable drop if this row has two columns already.
            DropBox(modifier = modifier.weight(1f), temMatrixIndex = matrixIndex) { newColumnValue ->
                val updatedAtIndex = matrixIndex / 2
                createNewRowAt(updatedAtIndex, newColumnValue, false)
            }
            Spacer(modifier = Modifier.width(4.dp))
            DropBox(modifier = modifier.weight(1f), temMatrixIndex = matrixIndex) { newColumnValue ->
                val updatedAtIndex = matrixIndex / 2
                createNewRowAt(updatedAtIndex, newColumnValue, true)
            }
        }
    } else {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp))
                .background(Color.White, RoundedCornerShape(16.dp)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text =  "Full",
                fontSize = 18.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun DropToCreateNewRow(
    dropMatrix: MutableState<List<List<String>>>,
    matrixIndex: Int,
    createNewRowAt: (Int, String) -> Unit
) {
    val modifier = Modifier
        .fillMaxWidth()
        .height(35.dp)
    DropBox(modifier = modifier, temMatrixIndex = matrixIndex) { newSingleElement ->
        val elementIndex = matrixIndex / 2
        createNewRowAt(elementIndex, newSingleElement)
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
fun DropBox(modifier: Modifier, temMatrixIndex: Int, newSingleElement: (String) -> Unit) {
    Box(modifier) {
        DropTarget<String>(
            modifier = modifier
        ) { isInBound, data ->
            val bgColor = if (isInBound) {
                Color.Red
            } else {
                Color.White
            }

            data?.let {
                if (isInBound) {
//                    foodItems[foodItem.id] = foodItem
                    newSingleElement(it)
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
                    text =  "$temMatrixIndex Drop Here",
                    fontSize = 18.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SavvDigitalDragAndDropTheme {
    }
}