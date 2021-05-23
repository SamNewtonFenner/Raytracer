import kotlin.reflect.typeOf

abstract class Ternary<T>(var x: Float, var y: Float, var z: Float) {

    abstract fun createTernary(x: Float, y: Float, z: Float): T

    override operator fun equals(v: Any?) = (v is Ternary<*>) && (x == v.x && y == v.y && z == v.z)
    operator fun unaryPlus() = createTernary(x, y, z)
    operator fun unaryMinus() = createTernary(-x, -y, -z)
    operator fun plus(v: Ternary<T>) = createTernary(x + v.x, y + v.y, z + v.z)
    operator fun plus(f: Float) = createTernary(x + f, y + f, z + f)
    operator fun minus(v: Ternary<T>) = createTernary(x - v.x, y - v.y, z - v.z)
    operator fun times(v: Ternary<T>) = createTernary(x * v.x, y * v.y, z * v.z)
    operator fun times(f: Float) = createTernary(x * f, y * f, z * f)
    operator fun div(v: Ternary<T>) = createTernary(x / v.x, y / v.y, z / v.z)
    operator fun div(i: Int) = createTernary(x / i, y / i, z / i)
    operator fun div(f: Float) = createTernary(x / f, y / f, z / f)
    operator fun get(i: Int) = arrayOf(x, y, z)[i]
    operator fun plusAssign(v: Ternary<T>) {
        x += v.x
        y += v.y
        z += v.z
    }
    operator fun minusAssign(v: Ternary<T>) {
        x -= v.x
        y -= v.y
        z -= v.z
    }
    operator fun timesAssign(v: Ternary<T>) {
        x *= v.x
        y *= v.y
        z *= v.z
    }
    operator fun divAssign(v: Ternary<T>) {
        x /= v.x
        y /= v.y
        z /= v.z
    }
    operator fun timesAssign(f: Float) {
        x *= f
        y *= f
        z *= f
    }
    operator fun divAssign(f: Float) {
        x /= f
        y /= f
        z /= f
    }
}