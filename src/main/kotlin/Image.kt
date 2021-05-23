import java.awt.Color
import java.awt.Image as AWT_Image

class Image(width:Int, height: Int) {

    private val pixelGrid = Array(height) {Array(width) {Colour.darkGrey()}}

    fun setColour(x: Int, y: Int, colour: Colour) {
        pixelGrid[y][x] = colour
    }

    fun mapToAWT(awtImage: AWT_Image): AWT_Image {
        val g = awtImage.graphics
        for (j in pixelGrid.indices) {
            for (i in pixelGrid[0].indices) {
                val c = pixelGrid[j][i]
                g.color = Color(c.r, c.g, c.b)
                g.drawLine(i, j, i, j)
            }
        }
        g.dispose()
        return awtImage
    }


}