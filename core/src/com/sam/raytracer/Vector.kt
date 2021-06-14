import kotlin.math.sqrt

class Vector(x: Float, y: Float, z: Float): Ternary<Vector>(x,y,z) {

    fun getUnitVector(): Vector {
        return this / length()
    }

    fun squaredLength(): Float {
        return x * x + y * y + z * z
    }

    fun length(): Float {
        return sqrt(squaredLength())
    }

    fun dotWith(v: Vector): Float {
        return x * v.x + y * v.y + z * v.z
    }

    fun crossWith(v: Vector): Vector {
        return Vector(
            (this.y * v.z) - (this.z * v.y),
            (this.z * v.x) - (this.x * v.z),
            (this.x * v.y) - (this.y * v.x)
        )
    }

    override fun createTernary(x: Float, y: Float, z: Float): Vector {
        return Vector(x,y,z)
    }

}