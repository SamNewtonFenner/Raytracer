class Metal(private val albedo: Colour, private val fuzz: Float): Material() {

    override fun scatter(ray: Ray, hit: Hit): Ray {
        val reflected = reflect(ray.direction.getUnitVector(), hit.normal)
        return Ray(hit.point, reflected + (getRandomVectorWithinUnit() * fuzz))
    }

    override fun getColourForHit(hit: Hit): Colour {
        return albedo
    }

    private fun reflect(incoming: Vector, normal: Vector): Vector {
        return incoming - (normal * 2F * incoming.dotWith(normal))
    }

}