import kotlin.random.Random

class Metal(override val albedo: Colour, private val fuzz: Float): Material() {

    override fun scatter(ray: Ray, hit: Hit): Ray {
        var reflected = reflect(ray.direction.getUnitVector(), hit.normal)
        return Ray(hit.point, reflected + (getRandomVectorWithinUnit() * fuzz))
    }

    private fun reflect(incoming: Vector, normal: Vector): Vector {
        return incoming - (normal * 2F * incoming.dotWith(normal))
    }

}