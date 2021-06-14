import kotlin.math.PI
import kotlin.math.tan

class Projection(val origin: Vector, lookAt: Vector, fieldOfView: Float, aspectRatio: Float) {

    private val viewUp = Vector(0F,1F,0F)

    val lowerLeftCorner: Vector
    val horizontal: Vector
    val vertical: Vector

    init {
        val theta = fieldOfView * PI / 180
        val halfHeight = (tan(theta/2)).toFloat()
        val halfWidth = aspectRatio * halfHeight
        val w = (lookAt - origin).getUnitVector()
        val u = viewUp.crossWith(w).getUnitVector()
        val v = w.crossWith(u)
        lowerLeftCorner = origin - (u * halfWidth) - (v * halfHeight) - w
        horizontal = u * halfWidth * 2F
        vertical = v * halfHeight * 2F

        println("Halfwidth: $halfWidth")
        println(lowerLeftCorner.x)
        println(lowerLeftCorner.y)
        println(lowerLeftCorner.z)
    }
}