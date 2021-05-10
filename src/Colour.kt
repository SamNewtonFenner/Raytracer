class Colour(val r: Float, val g: Float, val b: Float) : Ternary<Colour>(r, g, b) {
    override fun createTernary(x: Float, y: Float, z: Float): Colour {
        return Colour(x, y, z);
    }

    companion object {
        fun black() = Colour(0F, 0F, 0F);
        fun blue() = Colour(0.5F, 0.7F, 1.0F);
        fun green() = Colour(0.8F, 0.8F, 0F);
        fun darkGrey() = Colour(0.1F, 0.1F, 0.1F);
        fun lightGrey() = Colour(0.5F, 0.5F, 0.5F);
        fun white() = Colour(1.0F, 1.0F, 1.0F);
    }
}
