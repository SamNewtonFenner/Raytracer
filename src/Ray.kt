class Ray(val origin: Vector, val direction: Vector) {

    fun pointAt(distance: Float): Vector {
        return origin + (direction * distance);
    }

}