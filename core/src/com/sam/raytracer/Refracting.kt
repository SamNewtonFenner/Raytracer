import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.Random

class Refracting(private val refractionIndex: Float): Material() {

    override fun scatter(ray: Ray, hit: Hit): Ray {
        var niOverNt: Float
        var outwardNormal: Vector
        var cosine: Float
        var reflectProbability: Double = 1.0
        if (ray.direction.getUnitVector().dotWith(hit.normal) > 0) {
            outwardNormal = -hit.normal
            niOverNt = refractionIndex
            cosine = refractionIndex * ray.direction.dotWith(hit.normal) / ray.direction.length()
        } else {
            outwardNormal = hit.normal
            niOverNt = 1.0F / refractionIndex
            cosine = -ray.direction.dotWith(hit.normal) / ray.direction.length()
        }
        val refracted = refract(ray.direction, outwardNormal, niOverNt)
        if (refracted !== null) {
            reflectProbability = schlick(cosine)
            if (Random.nextFloat() < reflectProbability) {
                val reflected = reflect(ray.direction.getUnitVector(), hit.normal)
                return Ray(hit.point, reflected)
            } else {
                return Ray(hit.point, refracted)
            }
        } else {
            val reflected = reflect(ray.direction.getUnitVector(), hit.normal)
            return Ray(hit.point, reflected)
        }
    }

    override fun getColourForHit(hit: Hit): Colour {
        return Colour.white()
    }

    private fun schlick(cosine: Float): Double {
        var r = (1 - refractionIndex) / (1 + refractionIndex)
        r *= r
        return r + (1 - r) * (1.0 - cosine).pow(5.0)
    }

    private fun refract(v: Vector, normal: Vector, ni_over_nt: Float): Vector? {
        val uv = v.getUnitVector()
        val dot = uv.dotWith(normal)
        val discriminant = 1 - (ni_over_nt * ni_over_nt * (1 - dot * dot))
        if (discriminant > 0) {
            return ((uv - (normal * dot)) * ni_over_nt) - (normal * sqrt(discriminant))
        } else {
            return null
        }
    }

    private fun reflect(incoming: Vector, normal: Vector): Vector {
        return incoming - (normal * 2F * incoming.dotWith(normal))
    }
}