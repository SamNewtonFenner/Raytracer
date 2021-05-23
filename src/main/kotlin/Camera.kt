import kotlin.random.Random

class Camera(
    var projection: Projection,
    private val world: World,
    private val samplesPerPixel: Int,
    private val mode: CameraMode,
    private val maxReflections: Int,
    private val renderWidth: Int,
    private val renderHeight: Int,
)
{
    fun getImage(scalingFactor: Int = 1): Image {
        var windowWidth = renderWidth * scalingFactor
        var windowLength = renderHeight * scalingFactor
        val image = Image(windowWidth, windowLength)
        val colourGrid = getColourGrid(renderWidth, renderHeight)

        val startTime = System.currentTimeMillis()
        drawGridToImage(windowLength, windowWidth, image, colourGrid, scalingFactor)
        val endTime = System.currentTimeMillis()
        println("Image drawing took ${endTime - startTime}ms")

        return image
    }

    private fun drawGridToImage(
        windowLength: Int,
        windowWidth: Int,
        image: Image,
        colourGrid: Array<Array<Colour>>,
        scalingFactor: Int
    ) {
        for (j in 0 until windowLength) {
            for (i in 0 until windowWidth) {
                image.setColour(
                    i,
                    j,
                    colourGrid[i / scalingFactor][j / scalingFactor]
                )
            }
        }
    }

    private fun getColourGrid(width: Int, height: Int): Array<Array<Colour>> {
        val startTime = System.currentTimeMillis()
        var displayedProgress = 0

        val grid = Array(width) { Array(height) { Colour.black() } }
        for (j in 0 until height) {
            for (i in 0 until width) {
                grid[i][j] = getColourForPixel(i, j, width, height)
            }

            var currentProgress = ((j.toFloat()/height)*100).toInt()
            if ( currentProgress > displayedProgress ) {
                displayedProgress = currentProgress
                println("Rendering... ${displayedProgress}%")
            }
        }

        val endTime = System.currentTimeMillis()
        println("Image generation took ${endTime - startTime}ms")

        return grid
    }

    private fun getColourForPixel(i: Int, j: Int, width: Int, height: Int): Colour {
        var colour = Colour.black()
        for (s in 0 until samplesPerPixel) {
            val u: Float = (i + Random.nextFloat()) / width.toFloat()
            var v: Float = (j + Random.nextFloat()) / height.toFloat()
            val ray = getRay(u, 1 - v)
            val sample =  getColourForRay(ray, 0)
            colour = colour + sample
        }
        colour /= samplesPerPixel
        return colour
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