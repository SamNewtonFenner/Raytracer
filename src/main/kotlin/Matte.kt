class Matte(override val albedo: Colour): Material() {

    override fun scatter(ray: Ray, hit: Hit): Ray {
        var target = hit.point + hit.normal + getRandomVectorWithinUnit()
        return Ray(hit.point, target - hit.point)
    }

}