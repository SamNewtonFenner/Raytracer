abstract class Hittable {
    abstract fun hitsByRay(ray: Ray): List<Hit>
}

