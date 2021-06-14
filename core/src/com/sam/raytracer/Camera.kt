import kotlinx.coroutines.*
import kotlin.random.Random

class Camera(
    private val world: World,
    private val mode: CameraMode,
    var fieldOfView: Float,
    var aspectRatio: Float
)
{
    var renderSectionWidth = 50
    var samplesPerPass = 1
    var maxSamples = 20
    var maxBounces = 10
    var renderWidth = 400
    var renderHeight = 200
    var freshBuckets = false
    var initialOrigin = Vector(0F,0F,0F)
    var initialDirection = Vector(0F,0F,1F)
    var projection: Projection = Projection(initialOrigin, initialDirection, fieldOfView, aspectRatio)

    private var grid = getEmptyColourGrid()

    fun colourGrid(): Array<Array<ColourBucket>> {
        if (grid.size != renderWidth || maxSamples != grid[0][0].maxColours) grid = getEmptyColourGrid()
        topUpColourGrid();
        return grid
    }

    private fun getEmptyColourGrid(): Array<Array<ColourBucket>> {
        return Array(renderWidth) { Array(renderHeight) { ColourBucket(maxSamples) } }
    }

    private fun topUpColourGrid() {
        runBlocking {
            withContext(Dispatchers.IO) {
                grid[0].indices.chunked(renderSectionWidth).forEach {
                    launch {
                        topUpGridSection(it)
                    }
                }
            }
        }
    }

    private fun topUpGridSection(firstOrderIndexes: List<Int>) {
        for (j in firstOrderIndexes) {
            for (i in grid.indices) {
                if(freshBuckets) {
                    grid[i][j].setColours(getSamplesForPixel(i, j, grid.size, grid[0].size))
                } else {
                    grid[i][j].addColours(getSamplesForPixel(i, j, grid.size, grid[0].size))
                }
            }
        }
    }

    private fun getSamplesForPixel(i: Int, j: Int, width: Int, height: Int): Collection<Colour> {
        val samples = arrayListOf<Colour>()
        for (s in 0 until samplesPerPass) {
            val u: Float = (i + Random.nextFloat()) / width.toFloat()
            var v: Float = 1 - (j + Random.nextFloat()) / height.toFloat()
            val ray = getRay(u, v)
            samples.add(getColourForRay(ray, 0))
        }
        return samples
    }

    private fun getRay(u: Float, v: Float): Ray {
        val target = projection.lowerLeftCorner + (projection.horizontal * u) + (projection.vertical * v)
        return Ray(projection.origin,target - projection.origin)
    }

    private fun getColourForRay(ray: Ray, step: Int): Colour {
        var hit = tryToGetHit(ray)
        return if (hit != null && step < maxBounces) {
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
        return hits.minBy { hit -> hit.distance }!!
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