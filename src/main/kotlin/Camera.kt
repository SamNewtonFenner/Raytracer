import kotlin.random.Random

class Camera(
    var projection: Projection,
    private val world: World,
    private val samplesPerPass: Int,
    private val mode: CameraMode,
    private val maxReflections: Int,
    private val renderWidth: Int,
    private val renderHeight: Int,
    private val scalingFactor: Int = 1
)
{
    private val windowWidth = renderWidth * scalingFactor
    private val windowLength = renderHeight * scalingFactor
    private val grid = getEmptyColourGrid(renderWidth, renderHeight)
    private val image = Image(windowWidth, windowLength)

    fun getImage(): Image {

        topUpColourGrid();

        val startTime = System.currentTimeMillis()
        drawGridToImage()
        val endTime = System.currentTimeMillis()
        println("Image drawing took ${endTime - startTime}ms")

        return image
    }

    private fun drawGridToImage() {
        for (j in 0 until windowLength) {
            for (i in 0 until windowWidth) {
                image.setColour(
                    i,
                    j,
                    grid[i / scalingFactor][j / scalingFactor].getColour()
                )
            }
        }
    }

    private fun getEmptyColourGrid(width: Int, height: Int): Array<Array<ColourBucket>> {
        return Array(width) { Array(height) { ColourBucket() } }
    }

    private fun topUpColourGrid() {
        val startTime = System.currentTimeMillis()
        var displayedProgress = 0

        for (j in 0 until grid[0].size) {
            for (i in grid.indices) {
                grid[i][j].addColours(getSamplesForPixel(i, j, grid.size, grid[0].size))
            }

            var currentProgress = ((j.toFloat()/grid[0].size)*100).toInt()
            if ( currentProgress > displayedProgress ) {
                displayedProgress = currentProgress
                println("Sampling... ${displayedProgress}%")
            }
        }

        val endTime = System.currentTimeMillis()
        println("Sample collection took ${endTime - startTime}ms")
    }

    private fun getSamplesForPixel(i: Int, j: Int, width: Int, height: Int): Collection<Colour> {
        val samples = arrayListOf<Colour>()
        for (s in 0 until samplesPerPass) {
            val u: Float = (i + Random.nextFloat()) / width.toFloat()
            var v: Float = (j + Random.nextFloat()) / height.toFloat()
            val ray = getRay(u, 1 - v)
            samples.add(getColourForRay(ray, 0))
        }
        return samples
    }

    private fun getRay(u: Float, v: Float): Ray {
        val projectionTarget = projection.lowerLeftCorner + projection.width * u + projection.height * v
        return Ray(projection.origin,projectionTarget - projection.origin)
    }

    private fun getColourForRay(ray: Ray, step: Int): Colour {
        var hit = tryToGetHit(ray)
        return if (hit != null && step < maxReflections) {
            when (mode) {
                CameraMode.SURFACE_NORMAL -> getSurfaceNormalColour(hit)
                CameraMode.AMBIENT_OCCLUSION -> getAmbientOcclusionColour(ray, hit, step + 1)
                CameraMode.MATERIAL -> getMaterialColour(ray, hit, step + 1)
            }
        } else {
            getBackGroundColour(ray)
        }
    }

    private fun getSurfaceNormalColour(hit: Hit): Colour {
        return Colour(hit.normal.x + 1, hit.normal.y + 1, hit.normal.z + 1)/2
    }

    private fun getAmbientOcclusionColour(ray: Ray, hit: Hit, step: Int): Colour {
        val matte = Matte(Colour.lightGrey())
        var scatteredRay = matte.scatter(ray, hit)
        return matte.albedo * getColourForRay(scatteredRay, step)
    }

    private fun getMaterialColour(ray: Ray, hit: Hit, step: Int): Colour {
        val scatteredRay = hit.material.scatter(ray, hit)
        return hit.material.albedo * getColourForRay(scatteredRay, step)
    }

    private fun tryToGetHit(ray: Ray): Hit? {
        var hits = world.hittables
            .filter { hittable -> hittable.worthCheckingForHits(ray) }
            .flatMap { hittable -> hittable.hitsByRay(ray)}
            .filter { hit -> hitWithinBounds(hit) }
        return if (hits.any()) getClosestHit(hits) else null
    }

    private fun getClosestHit(hits: List<Hit>): Hit {
        return hits.minByOrNull { hit -> hit.distance }!!
    }

    private fun hitWithinBounds(hit: Hit): Boolean {
        return (hit.distance > 0.001F && Float.MAX_VALUE > hit.distance)
    }

    private fun getBackGroundColour(ray: Ray): Colour {
        val unitDirection = ray.direction.getUnitVector()
        val t = 0.5F * (unitDirection.y + 1)
        return (Colour.white() * (1.0F - t)) + (Colour.blue() * t)
    }

}