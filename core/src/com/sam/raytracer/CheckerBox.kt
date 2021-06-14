import kotlin.math.sin

class CheckerBox(private val albedo: Colour, private val secondaryAlbedo: Colour): Material() {

    private val patternSize = 10

    override fun scatter(ray: Ray, hit: Hit): Ray {
        val target = hit.point + hit.normal + getRandomVectorWithinUnit()
        return Ray(hit.point, target - hit.point)
    }

    override fun getColourForHit(hit: Hit): Colour {
        return if ((sin(patternSize * hit.point.x) * sin(patternSize * hit.point.y) * sin(patternSize * hit.point.z)) > 0)
            albedo
        else
            secondaryAlbedo
    }

}