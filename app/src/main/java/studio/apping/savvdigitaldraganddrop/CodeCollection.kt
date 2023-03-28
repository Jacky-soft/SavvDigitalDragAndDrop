package studio.apping.savvdigitaldraganddrop
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.Surface
//import androidx.compose.material.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.MutableState
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.shadow
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//
//
//@Composable
//fun MainScreen() {
//    LongPressDraggable(modifier = Modifier.fillMaxSize()) {
//        Column {
//            Row() {
//                DisplayLayout(modifier = Modifier.weight(1f))
//                DropLayout(modifier = Modifier.weight(1f))
//            }
//            Spacer(modifier = Modifier.weight(1f))
//            TemporaryDragItem()
//        }
//    }
//}
//
//@Composable
//fun DisplayLayout(modifier: Modifier) {
//    Surface (modifier){
//
//    }
//}
//
//@Composable
//fun TemporaryDragItem() {
//    Row {
//        Spacer(modifier = Modifier.weight(1f))
//        DragTarget(
//            modifier = Modifier
//                .size(100.dp)
//                .padding(8.dp), dataToDrop = 1
//        ) {
//            Text(
//                text = "Drag Me", modifier = Modifier
//                    .size(100.dp)
//                    .padding(8.dp)
//                    .background(Color.DarkGray, RoundedCornerShape(14.dp))
//            )
//        }
//        Spacer(modifier = Modifier.weight(1f))
//    }
//}
//
//@Composable
//fun DropLayout(modifier: Modifier) {
//    Surface(modifier) {
//
//        val numberOfRows = remember { mutableStateOf(0) }
//        when (numberOfRows.value) {
//            0 -> DropMap0(numberOfRows)
//            1 -> DropMap1(numberOfRows)
//            2 -> DropMap2(numberOfRows)
//            3 -> DropMap3(numberOfRows)
//            4 -> DropMap4(numberOfRows)
//            5 -> DropMap5(numberOfRows)
//            else -> DropMapDisabled(numberOfRows)
//        }
//    }
//}
//
//@Composable
//fun DropMap1(layoutNumber: MutableState<Int>) {
//    Column {
//        CreateNewRow(layoutNumber = layoutNumber)
//        // TODO middle has a row os two drop areas
//        CreateNewRow(layoutNumber = layoutNumber)
//    }
//}
//
//@Composable
//fun DropMap2(layoutNumber: MutableState<Int>) {
//    Column {
//        CreateNewRow(layoutNumber = layoutNumber)
//        CreateNewRow(layoutNumber = layoutNumber)
//        CreateNewRow(layoutNumber = layoutNumber)
//    }
//}
//
//@Composable
//fun DropMap3(layoutNumber: MutableState<Int>) {
//    Column {
//        CreateNewRow(layoutNumber = layoutNumber)
//        CreateNewRow(layoutNumber = layoutNumber)
//        CreateNewRow(layoutNumber = layoutNumber)
//        CreateNewRow(layoutNumber = layoutNumber)
//    }
//}
//
//@Composable
//fun DropMap4(layoutNumber: MutableState<Int>) {
//    Column {
//        CreateNewRow(layoutNumber = layoutNumber)
//        CreateNewRow(layoutNumber = layoutNumber)
//        CreateNewRow(layoutNumber = layoutNumber)
//        CreateNewRow(layoutNumber = layoutNumber)
//        CreateNewRow(layoutNumber = layoutNumber)
//    }
//}
//
//@Composable
//fun DropMap5(layoutNumber: MutableState<Int>) {
//    Column {
//        CreateNewRow(layoutNumber = layoutNumber)
//        CreateNewRow(layoutNumber = layoutNumber)
//        CreateNewRow(layoutNumber = layoutNumber)
//        CreateNewRow(layoutNumber = layoutNumber)
//        CreateNewRow(layoutNumber = layoutNumber)
//        CreateNewRow(layoutNumber = layoutNumber)
//    }
//}
//
//@Composable
//fun DropMapDisabled(layoutNumber: MutableState<Int>) {
//    Column {
//    }
//}
//
//@Composable
//fun DropTargetTemplate(layoutNumber: MutableState<Int>) {
//    Box() {
//        DragTarget(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(100.dp), dataToDrop = -1
//        ) {
//            Text(
//                text = "Drag Me", modifier = Modifier
//                    .size(100.dp)
//                    .padding(8.dp)
//                    .background(Color.DarkGray, RoundedCornerShape(14.dp))
//            )
//        }
//        DropTarget<Int>(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(100.dp)
//        ) { isInBound, data ->
//            val bgColor = if (isInBound) {
//                Color.Red
//            } else {
//                Color.White
//            }
//
//            data?.let {
//                if (isInBound) {
////                    foodItems[foodItem.id] = foodItem
//                    layoutNumber.value += data
//                }
//            }
//
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp))
//                    .background(
//                        bgColor,
//                        RoundedCornerShape(16.dp)
//                    ),
//                verticalArrangement = Arrangement.Center,
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Text(
//                    text = if (data != null) "${layoutNumber.value}" else "Drop Here",
//                    fontSize = 18.sp,
//                    color = Color.Black,
//                    fontWeight = FontWeight.Bold
//                )
//            }
//        }
//    }
//}
//
//@Composable
//fun CreateNewRow(layoutNumber: MutableState<Int>) {
//    DropTargetTemplate(layoutNumber = layoutNumber)
//}
//
//@Composable
//fun CreateNewColumn() {
//    Row {
//        DropTargetTemplate(layoutNumber =)
//        DropTargetTemplate(layoutNumber =)
//    }
//}
//
//@Composable
//fun DropMap0(layoutNumber: MutableState<Int>) {
//    Column {
//        CreateNewRow(layoutNumber = layoutNumber)
//    }
//}
