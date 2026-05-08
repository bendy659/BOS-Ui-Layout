package ru.benos.libs.bos_ui_layout.client.nodes.grid

import ru.benos.libs.bos_ui_layout.client.UiDsl
import ru.benos.libs.bos_ui_layout.client.UiRuntime
import ru.benos.libs.bos_ui_layout.client.datas.UiGridChild
import ru.benos.libs.bos_ui_layout.client.datas.UiModifier
import ru.benos.libs.bos_ui_layout.client.datas.UiRect
import ru.benos.libs.bos_ui_layout.client.datas.UiSize
import ru.benos.libs.bos_ui_layout.client.nodes.AbstractUiNode
import kotlin.math.max

@UiDsl
class UiGridNode(
    private val rows: Int,
    private val columns: Int,
    private val hGap: Int,
    private val vGap: Int,
    private val children: List<UiGridChild>,

    override val modifier: UiModifier
): AbstractUiNode() {
    private data class MeasuredGrid(
        val columnWidths: IntArray,
        val rowHeights: IntArray
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as MeasuredGrid

            if (!columnWidths.contentEquals(other.columnWidths)) return false
            if (!rowHeights.contentEquals(other.rowHeights)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = columnWidths.contentHashCode()
            result = 31 * result + rowHeights.contentHashCode()
            return result
        }
    }

    override fun measure(runtime: UiRuntime, availableSize: UiSize): UiSize {
        val inner = UiRect(0, 0, availableSize).shrink(modifier.padding)
        val measured = measureGrid(runtime, inner.width, inner.height)

        val contentWidth  = measured.columnWidths.sum() + hGap * (columns - 1).coerceAtLeast(0)
        val contentHeight = measured.rowHeights.sum()   + vGap   * (rows    - 1).coerceAtLeast(0)

        return modifier.resolveSize(contentWidth, contentHeight, availableSize)
    }

    override fun render(runtime: UiRuntime, bounds: UiRect) {
        super.render(runtime, bounds)

        val inner    = bounds.shrink(modifier.padding)
        val measured = measureGrid(runtime, inner.width, inner.height)

        val columnX = IntArray(columns)
        val rowY    = IntArray(rows)

        var cursorX = inner.x
        repeat(columns) { col ->
            columnX[col] = cursorX
            cursorX += measured.columnWidths[col] + hGap
        }

        var cursorY = inner.y
        repeat(rows) { row ->
            rowY[row] = cursorY
            cursorY += measured.rowHeights[row] + vGap
        }

        children.forEach { child ->
            if (child.row !in 0 until rows || child.column !in 0 until columns) return@forEach

            child.node.render(
                runtime,
                UiRect(
                    columnX[child.column],
                    rowY[child.row],
                    measured.columnWidths[child.column],
                    measured.rowHeights[child.row]
                )
            )
        }
    }

    private fun measureGrid(runtime: UiRuntime, availableWidth: Int, availableHeight: Int): MeasuredGrid {
        val totalHGap    = hGap * (columns - 1).coerceAtLeast(0)
        val colWidth     = ((availableWidth - totalHGap) / columns).coerceAtLeast(0)
        val columnWidths = IntArray(columns) { colWidth }

        val rowHeights = IntArray(rows) { 0 }
        children.forEach { child ->
            if (child.row !in 0 until rows || child.column !in 0 until columns) return@forEach

            val measured = child.node.measure(runtime, UiSize(columnWidths[child.column], availableHeight))
            rowHeights[child.row] = max(rowHeights[child.row], measured.height)
        }

        return MeasuredGrid(columnWidths, rowHeights)
    }
}