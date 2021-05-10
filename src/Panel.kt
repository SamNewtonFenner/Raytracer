import java.awt.Dimension
import java.awt.Graphics
import javax.swing.JPanel

class Panel(width: Int, height: Int, initialImage: Image): JPanel() {

    private var image: Image;

    init{
        image = initialImage;
        preferredSize = Dimension(width, height);
    }

    fun setNewImage(image: Image) {
        this.image = image;
    }

    override fun paintComponent(graphics: Graphics) {
        super.paintComponent(graphics);
        val awtImage = image.mapToAWT(createImage(width, height));
        graphics.drawImage(awtImage,0,0,null);
    }
}