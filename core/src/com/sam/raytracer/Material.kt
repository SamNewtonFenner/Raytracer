import kotlin.random.Random

abstract class Material {
    abstract val albedo: Colour
    abstract fun scatter(ray: Ray, hit: Hit): Ray

    fun getRandomVectorWithinUnit(): Vector {
        var vector: Vector
        do {
            vector = (randomUnitVector() * 2F) - Vector(1F,1F,1F)
        } while (vector.squaredLength() >= 1)
        return vector
    }

    private fun randomUnitVector(): Vector {
        return Vector(Random.nextFloat(), Random.nextFloat(), Random.nextFloat())
    }
}