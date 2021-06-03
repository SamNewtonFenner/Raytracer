class ColourBucket(initialColours: ArrayList<Colour> = arrayListOf()) {

    private val colours: ArrayList<Colour> = initialColours;

    fun addColours(colour: Collection<Colour>) {
        colours.addAll(colour)
    }

    fun getColour(): Colour {
        val sum = colours.reduce { acc: Colour, colour: Colour -> acc + colour }
        return sum / colours.size
    }
}