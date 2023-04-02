package studio.apping.savvdigitaldraganddrop

fun <T> MutableList<List<T>>.getElement(fromRow: Int, fromColumn: Int) : T{
    val row = this[fromRow]
    return row[fromColumn]
}

fun <T> MutableList<List<T>>.removeRow(fromRow: Int) {
    this.removeAt(fromRow)
}

fun <T> MutableList<List<T>>.removeColumn(fromRow: Int, fromColumn: Int) {
    val row = mutableListOf<T>()
    row.addAll(this[fromRow])
    row.removeAt(fromColumn)
    this[fromRow] = row
}

fun <T> MutableList<List<T>>.addRow(toRow: Int, element: T) {
    val newRow = listOf(element)
    if (toRow > this.lastIndex)
        this.add(newRow)
    else
        this.add(toRow, newRow)
}

fun <T> MutableList<List<T>>.addColumn(toRow: Int, toColumn: Int, element: T) {
    val row = mutableListOf<T>()
    row.addAll(this[toRow])
    row.add(toColumn, element)
    this[toRow] = row
}