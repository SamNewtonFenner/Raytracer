class Matte(private val albedo: Colour): Material() {

    override fun scatter(ray: Ray, hit: Hit): Ray {
        val target = hit.point + hit.normal + getRandomVectorWithinUnit()
        return Ray(hit.point, target - hit.point)
    }

    override fun getColourForHit(hit: Hit): Colour {
        return albedo
    }

}