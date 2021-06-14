class ColourBucket(val maxColours: Int, initialColours: ArrayList<Colour> = arrayListOf()) {

    private var coloursInBucket: ArrayList<Colour> = initialColours;
    private var colourAtLastAccess: Colour = Colour.black()

    fun setColours(colours: Collection<Colour>) {
        coloursInBucket = ArrayList(colours)
    }

    fun addColours(colours: Collection<Colour>) {
        if(coloursInBucket.size < maxColours) {
            coloursInBucket.addAll(colours)
        }
    }

    fun getColour(): Colour {
        if (coloursInBucket.size >= maxColours) {
            return colourAtLastAccess
        }
        colourAtLastAccess = getBucketColour()
        return colourAtLastAccess
    }

    private fun getBucketColour() =
        coloursInBucket.reduce { acc: Colour, colour: Colour -> acc + colour } / coloursInBucket.size
}