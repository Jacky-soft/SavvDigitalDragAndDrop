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
                listOf("a"),
                listOf("b"),
                listOf("c"),
                listOf("d"),
                listOf("e"),
                listOf("f"),
            )) } //TODO fix the add and remove logic. Add drop text shows nothing but index. Drag text shows the actual content.
        val addRow: (Int, String) -> Unit = { atIndex: Int, newElement: String ->
            val matrix = mutableListOf<List<String>>()
            itemMatrix.value.forEach { matrix.add(it) }
            matrix.add(atIndex, listOf(newElement))
            itemMatrix.value = matrix
        }

        val addColumn: (Int, Int, String) -> Unit = { atIndex: Int, onLeft: Int, newElement: String ->
            val existingElement = itemMatrix.value[atIndex].first()
            val updatedRow = if (onLeft == 0) listOf(newElement, existingElement) else listOf(existingElement, newElement)
            val matrix = mutableListOf<List<String>>()
            itemMatrix.value.forEach {
                matrix.add(it)
            }
            matrix[atIndex] = updatedRow
            itemMatrix.value = matrix
        }

        val removeRow: (Int) -> Unit = { atIndex: Int ->
            val matrix = mutableListOf<List<String>>()
            matrix.addAll(itemMatrix.value)
            matrix.removeAt(atIndex)
            itemMatrix.value = matrix
        }

        val removeColumn: (Int, Int) -> Unit = { atRowIndex: Int, atColumnIndex: Int ->
            val matrix = mutableListOf<List<String>>()
            matrix.addAll(itemMatrix.value)
            val targetRow = mutableListOf<String>()
            targetRow.addAll(matrix[atRowIndex])
            targetRow.removeAt(atColumnIndex)
            matrix[atRowIndex] = targetRow
            itemMatrix.value = matrix
        }
//TODO make the drag layout and drop layout less gap between the rows.
        DragLayout(itemMatrix,
            { index ->
                removeRow(index)
            },
            { atRowIndex, atColumnIndex ->
                removeColumn(atRowIndex, atColumnIndex)
        })
        DropLayout(itemMatrix,
            { index, element ->
                addRow(index, element)
            },
            { atRowIndex, atColumnIndex, element ->
                addColumn(atRowIndex, atColumnIndex, element)
        })
    }
}

@Composable
fun DragLayout(
    dragMatrix: MutableState<List<List<String>>>,
    removeRowAt: (Int) -> Unit,
    removeColumnAt: (Int, Int) -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(2.dp)) {
        Spacer(modifier = Modifier.height(30.dp))

        for (i in 0..dragMatrix.value.lastIndex) {
            val currentDragRow = dragMatrix.value[i]
            if (currentDragRow.size == 1) {
                DragBox(Modifier.height(60.dp), currentDragRow.first()) {
                    removeRowAt(i)
                }
            } else if (currentDragRow.size == 2) {
                Row {
                   DragBox(
                       Modifier
                           .weight(1f)
                           .height(60.dp), currentDragRow.first()) {
                       removeColumnAt(i, 0)
                   }
                   DragBox(
                       Modifier
                           .weight(1f)
                           .height(60.dp), currentDragRow.last()) {
                       removeColumnAt(i, 1)
                   }
                }
            }
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
fun DropLayout(
    dropMatrix: MutableState<List<List<String>>>,
    createNewRowAt: (Int, String) -> Unit,
    createNewColumnAt: (Int, Int, String) -> Unit
) {  // TODO if the drag is size 1, then can not drop at the same row.

    Column(
        Modifier
            .fillMaxSize()
            .padding(2.dp)) {

        var matrixIndex = 0
        DropBox(modifier = Modifier
            .fillMaxWidth()
            .height(30.dp), temMatrixIndex = matrixIndex) { newSingleElement ->
            createNewRowAt(matrixIndex / 2, newSingleElement)
        }
        for (i in 1..dropMatrix.value.size) {
            matrixIndex += 1
            val currentRow = dropMatrix.value[matrixIndex / 2]
            val currentRowSize = currentRow.size
            if (currentRowSize < 2){
                Row {
                    DropBox(modifier = Modifier
                        .height(60.dp)
                        .weight(1f), temMatrixIndex = matrixIndex) { newColumnValue ->
                        val updatedAtIndex = matrixIndex / 2
                        createNewColumnAt(updatedAtIndex, 0, newColumnValue)
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    DropBox(modifier = Modifier
                        .height(60.dp)
                        .weight(1f), temMatrixIndex = matrixIndex) { newColumnValue ->
                        val updatedAtIndex = matrixIndex / 2
                        createNewColumnAt(updatedAtIndex, 1, newColumnValue)
                    }
                }
            } else {
                ItemArea(modifier = Modifier.height(60.dp), note = "Full $currentRow")
            }
            matrixIndex += 1
            DropBox(modifier = Modifier
                .fillMaxWidth()
                .height(30.dp), temMatrixIndex = matrixIndex) { newSingleElement ->
                val elementIndex = matrixIndex / 2
                createNewRowAt(elementIndex, newSingleElement)
            }
        }
    }
}

@Composable
fun DragBox(modifier: Modifier, value: String, removeRow: () -> Unit) {
    DragTarget(
        modifier = modifier, dataToDrop = value,
        removeRow
    ) {
        ItemArea(modifier = modifier, note = value)
    }
}

@Composable
fun DropBox(modifier: Modifier, temMatrixIndex: Int, newSingleElement: (String) -> Unit) { //TODO temMatrixIndex can be removed when refactor
    Box(modifier) {
        DropTarget<String>(
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
                    newSingleElement(it)
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
}

@Composable
fun ItemArea(modifier: Modifier, note: String) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp))
            .background(
                //                Color.White // Be transparent for dev purpose
                Color.Transparent,
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