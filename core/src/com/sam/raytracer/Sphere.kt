import kotlin.math.sqrt

class Sphere(
    private val center: Vector,
    private val radius: Float,
    private val material: Material
): Hittable() {

    override fun hitsByRay(ray: Ray): List<Hit> {
        val originToCentre = ray.origin - center
        val a = ray.direction.dotWith(ray.direction)
        val b = originToCentre.dotWith(ray.direction)
        val c = originToCentre.dotWith(originToCentre) - (radius * radius)
        val discriminant = (b*b) - (a*c)

        var hits = mutableListOf<Hit>()

        if (discriminant > 0) {
            val sqrtDiscriminant = sqrt(discriminant)
            hits.add(getHitRecord((-b - sqrtDiscriminant)/a, ray))
            hits.add(getHitRecord((-b + sqrtDiscriminant)/a, ray))
        }

        return hits
    }

    override fun worthCheckingForHits(ray: Ray): Boolean {
        return (ray.origin - center).dotWith(ray.direction) < 0
    }

    private fun getHitRecord(root: Float, ray: Ray): Hit {
        val pointOfHit = ray.pointAt(root)
        val surfaceNormal = (pointOfHit - center) / radius
        return Hit(root, pointOfHit, surfaceNormal, material)
    }

}