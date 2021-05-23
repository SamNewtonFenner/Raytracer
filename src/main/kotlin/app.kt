import java.awt.EventQueue
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.Timer

class App : Runnable, ActionListener {

    private val windowWidth = 2000
    private val windowHeight = 1000
    private val renderWidth = 2000
    private val renderHeight = 1000
    private val samples = 50
    private val appTitle = "Raycaster"
    private val maxReflections = 5

    private var originXoffset = 0F
    private var up = true

    private val world = World(
        arrayOf(
            Sphere(Vector(0F, 0F, -4F), 0.5F, Metal(Colour(0.8F, 0.8F, 0.8F), 0F)),
            Sphere(Vector(0F, -100.5F, -4F), 100F, Matte(Colour.green())),
            Sphere(Vector(-1F,0F, -4F), 0.5F, Matte(Colour(0.8F, 0.3F, 0.3F))),
            Sphere(Vector(1F, 0F, -4F), 0.5F, Metal(Colour(0.5F, 0.7F, 0.8F), 0.4F)),
        )
    )
    private val frame = ApplicationWindow(appTitle, windowWidth, windowHeight)
    private val panel = Panel(windowWidth, windowHeight, Image(windowWidth, windowHeight))

    private val projection = Projection(
        Vector(0F, 0F, 0F),
        Vector(-2.0F, -1.0F, -4.0F),
        Vector(0.0F, 2.0F, 0.0F),
        Vector(4.0F, 0.0F, 0.0F)
    )
    private val camera = Camera(
        projection,
        world,
        samples,
        CameraMode.MATERIAL,
        maxReflections,
        renderWidth,
        renderHeight
    )

    private fun startGUIAndRun() {
        frame.add(panel)
        frame.pack()
        frame.setLocationRelativeTo(null)
        frame.isVisible = true

        panel.setNewImage(camera.getImage(windowWidth, windowHeight))

//        val timer = Timer(50, this)
//        timer.start()
    }

    override fun actionPerformed(e: ActionEvent?) {
        camera.projection =  Projection(
            Vector(0F + originXoffset, 0F, 0F),
            Vector(-2.0F + originXoffset, -1.0F, -4.0F),
            Vector(0.0F, 2.0F, 0.0F),
            Vector(4.0F, 0.0F, 0.0F)
        )
        if (up) {
            originXoffset +=  0.03F
        } else {
            originXoffset -=  0.03F
        }
        if(originXoffset > 2) up = false
        if(originXoffset < -2) up = true

        panel.setNewImage(camera.getImage(windowWidth, windowHeight))
        panel.repaint()
    }

    override fun run() {
        EventQueue.invokeLater(::startGUIAndRun)
    }

}

fun main() {
    App().run()
}