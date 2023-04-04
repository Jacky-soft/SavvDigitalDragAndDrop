package studio.apping.savvdigitaldraganddrop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
        val itemMatrix: MutableState<List<List<DragAndDropItem>>> = remember { mutableStateOf(
            listOf(
                listOf(DragAndDropItem(Icons.Filled.AccountCircle, Color(0xff57CC99),"Account"),DragAndDropItem(Icons.Filled.Email, Color(0xffD56F3E),"Email")),
                listOf(DragAndDropItem(Icons.Filled.Share, Color(0xff89909f),"Share"),DragAndDropItem(Icons.Filled.Check, Color(0xff2A7F62),"Check")),
                listOf(DragAndDropItem(Icons.Filled.Build, Color(0xffC3ACCE),"Build"),DragAndDropItem(Icons.Filled.Edit, Color(0xff36213E),"Edit")),
            )) }

        //TODO make the drag layout and drop layout less gap between the rows.
        DragLayout(itemMatrix)
        DropLayout(itemMatrix) { from, to ->
            var _to = to
            val matrix = mutableListOf<List<DragAndDropItem>>()
            matrix.addAll(itemMatrix.value)
            val element = matrix.getElement(from.first, from.second)

            if (from.third) {
                matrix.removeRow(from.first)
                if (from.first < to.first) _to = Triple(to.first - 1, to.second, to.third)
            } else {
                matrix.removeColumn(from.first, from.second)
            }

            if (_to.third) {
                matrix.addRow(_to.first, element)
            } else {
                matrix.addColumn(_to.first, to.second, element)
            }

            itemMatrix.value = matrix
        }
    }
}

@Composable
fun DragLayout(
    dragMatrix: MutableState<List<List<DragAndDropItem>>>
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(2.dp)) {
        Spacer(modifier = Modifier.height(30.dp))

        for (i in 0..dragMatrix.value.lastIndex) {
            val currentDragRow = dragMatrix.value[i]
            if (currentDragRow.size == 1) {
                DragBox(Modifier.height(60.dp), Triple(i, 0, true), currentDragRow[0])
            } else if (currentDragRow.size == 2) {
                Row {
                   DragBox(
                       Modifier
                           .weight(1f)
                           .height(60.dp), Triple(i,0,false), currentDragRow[0])
                   DragBox(
                       Modifier
                           .weight(1f)
                           .height(60.dp), Triple(i,1,false), currentDragRow[1])
                }
            }
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
fun DropLayout(
    dropMatrix: MutableState<List<List<DragAndDropItem>>>,
    updateMatrixFunc: (Triple<Int, Int, Boolean>, Triple<Int, Int, Boolean>) -> Unit
) {

    Column(
        Modifier
            .fillMaxSize()
            .padding(2.dp)) {
        // TODO apply animation to resize the drag Composable.
        var matrixIndex = 0
        DropBox(modifier = Modifier
            .fillMaxWidth()
            .height(30.dp),  Triple(matrixIndex, 0, true)) { from, to -> updateMatrixFunc(from, to)}
        for (i in 1..dropMatrix.value.size) {
            matrixIndex += 1
            val currentRow = dropMatrix.value[matrixIndex / 2]
            val currentRowSize = currentRow.size
            if (currentRowSize < 2){
                Row {
                    DropBox(modifier = Modifier
                        .height(60.dp)
                        .weight(1f), Triple(matrixIndex, 0, false)) { from, to -> updateMatrixFunc(from, to)}
                    Spacer(modifier = Modifier.width(4.dp))
                    DropBox(modifier = Modifier
                        .height(60.dp)
                        .weight(1f), Triple(matrixIndex, 1, false)) { from, to -> updateMatrixFunc(from, to)}
                }
            } else {
                Box(modifier = Modifier.height(60.dp).fillMaxWidth())
            }
            matrixIndex += 1
            DropBox(modifier = Modifier
                .fillMaxWidth()
                .height(30.dp), Triple(matrixIndex, 0, true)) { from, to -> updateMatrixFunc(from, to)
            }
        }
    }
}

@Composable
fun DragBox(modifier: Modifier, from: Triple<Int, Int, Boolean>, item: DragAndDropItem) {
    DragTarget(
        modifier = modifier, dataToDrop = from,
    ) {
        ItemArea(modifier = modifier, item = item)
    }
}

@Composable
fun DropBox(modifier: Modifier, to: Triple<Int, Int, Boolean>, newSingleElement: (Triple<Int, Int, Boolean>, Triple<Int, Int, Boolean>) -> Unit) {
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
                // But can use matrix index to validate the rules.
                // TODO ideally, use rules to decide whether the drop is indicated or not.
                val ruleNotPassed = if (from.third) { // Rules that decide where the dragged item can be dropped and cannot dropped.
                    (to.first == from.first * 2 ||
                    to.first == from.first * 2 + 1 ||
                    to.first == from.first * 2 + 2)
                } else {
                    (to.first == from.first * 2 + 1)
                }
                // TODO need to invalid if the current row has 2 items.
                if (!ruleNotPassed) {
                    val toWithActualRowIndex = Triple(to.first/2 , to.second, to.third) // Please ignore IDE warning for to.third.
                    newSingleElement(from, toWithActualRowIndex)
                }
            }
        }
        // TODO use animateDpAsState to animate the Rows.
        Column(
            modifier = modifier
                .fillMaxWidth()
//                .shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp))
                .background(
                    //                Color.White // Be transparent for dev purpose
                    bgColor,
//                    RoundedCornerShape(16.dp)
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
//            Text(
//                text =  "",
//                fontSize = 18.sp,
//                color = Color.Black,
//                fontWeight = FontWeight.Bold
//            )
        }
    }
}

@Composable
fun ItemArea(modifier: Modifier, item: DragAndDropItem) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp))
            .background(
                Color.White, // Be transparent for dev purpose
//                Color.Transparent,
                RoundedCornerShape(16.dp)
            ),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(modifier = Modifier
            .padding(start = 16.dp, end = 8.dp)
            .size(36.dp), imageVector = item.icon, tint = item.tint, contentDescription = item.note )
        Text(
            text =  item.note,
            fontSize = 18.sp,
            color = item.tint,
            fontWeight = FontWeight.Bold
        )
    }
}

data class DragAndDropItem(val icon: ImageVector, val tint: Color, val note: String)

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SavvDigitalDragAndDropTheme {
    }
}