import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.sam.raytracer.Screen
import ktx.app.KtxGame
import ktx.app.KtxScreen

class App(private val width: Int, private val height: Int) : KtxGame<KtxScreen>() {

    val batch by lazy { SpriteBatch() }
    val font by lazy { BitmapFont() }

    private val world = World(
        arrayOf(
            Sphere(Vector(0F, 0F, -4F), 0.5F, Metal(Colour(0.8F, 0.8F, 0.8F), 0F)),
            Sphere(Vector(0F, -100.5F, -4F), 100F, Matte(Colour.green())),
            Sphere(Vector(-1F,0F, -4F), 0.5F, Matte(Colour(0.8F, 0.3F, 0.3F))),
            Sphere(Vector(1F, 0F, -4F), 0.5F, Metal(Colour(0.5F, 0.7F, 0.8F), 0F))
        )
    )

    private val projection = Projection(
        Vector(0F, 0F, 0F),
        Vector(-2.0F, -1.0F, -4.0F),
        Vector(0.0F, 2.0F, 0.0F),
        Vector(4.0F, 0.0F, 0.0F)
    )

    private val camera = Camera(
        projection,
        world,
        CameraMode.MATERIAL
    )

    override fun create() {
        addScreen(Screen(this, camera, width.toFloat(), height.toFloat()))
        setScreen<Screen>()
        super.create()
    }

    override fun dispose() {
        batch.dispose()
        font.dispose()
        super.dispose()
    }
}