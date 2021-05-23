abstract class Hittable {
    abstract fun hitsByRay(ray: Ray): List<Hit>
    abstract fun worthCheckingForHits(ray: Ray): Boolean
}

