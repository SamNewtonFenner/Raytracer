import java.awt.EventQueue
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

class App : Runnable, ActionListener {

    private val width = 1600;
    private val height = 800;
    private val samples = 100;
    private val appTitle = "Raycaster";
    private val maxReflections = 10;

    private val world = World(
        arrayOf(
            Sphere(Vector(0F, 0F, -4F), 0.5F, Metal(Colour(0.8F, 0.8F, 0.8F), 0F)),
            Sphere(Vector(0F, -100.5F, -4F), 100F, Matte(Colour.green())),
            Sphere(Vector(-1F,0F, -4F), 0.5F, Matte(Colour(0.8F, 0.3F, 0.3F))),
            Sphere(Vector(1F, 0F, -4F), 0.5F, Metal(Colour(0.5F, 0.7F, 0.8F), 0.4F)),
        )
    );
    private val frame = ApplicationWindow(appTitle, width, height);
    private val panel = Panel(width, height, Image(width, height));

    private val projection = Projection(
        Vector(0F, 0F, 0F),
        Vector(-2.0F, -1.0F, -4.0F),
        Vector(0.0F, 2.0F, 0.0F),
        Vector(4.0F, 0.0F, 0.0F)
    );
    private val camera = Camera(projection, world, samples, CameraMode.MATERIAL, maxReflections);

    private fun startGUIAndRun() {
        frame.add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.isVisible = true;

        panel.setNewImage(camera.getImage(width, height));
    }

    override fun actionPerformed(e: ActionEvent?) {
        panel.repaint();
    }

    override fun run() {
        EventQueue.invokeLater(::startGUIAndRun);
    }

}

fun main() {
    App().run();
}