package com.sam.raytracer

import ktx.app.KtxScreen

import Camera
import ColourBucket
import App
import Projection
import Vector
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import java.util.*

class Screen(private val game: App, private val camera: Camera, private val width: Float, private val height: Float) : KtxScreen {

    private var originXoffset = 0F
    private var originZoffset = 0F
    private var frameCount = 0;
    private var fps = ""
    private var lastRenderTimes: Queue<Long> = LinkedList<Long>(arrayListOf(0))
    private var lastRenderEnd = System.currentTimeMillis()

    override fun render(delta: Float) {

        handleInput()

        game.batch.begin()
        game.batch.draw(getTexture(camera.colourGrid()),0f,0f,width,height)
        drawStats()
        game.batch.end()

        frameCount++

        val now = System.currentTimeMillis()
        lastRenderTimes.add(now - lastRenderEnd)
        lastRenderEnd = now

    }

    private fun handleInput() {
        camera.freshBuckets = false

        handleMovement()

        handleResolutionChange()

        handleBounceChanges()

        handleSamplingChanges()

    }

    private fun handleMovement() {
        if (Gdx.input.isKeyPressed(Input.Keys.A) ||
            Gdx.input.isKeyPressed(Input.Keys.D) ||
            Gdx.input.isKeyPressed(Input.Keys.S) ||
            Gdx.input.isKeyPressed(Input.Keys.W) ||
            Gdx.input.isKeyPressed(Input.Keys.Z)
        ) {

            val movementSpeed = if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) 0.5F else 0.02F

            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                originXoffset -= movementSpeed
            } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                originXoffset += movementSpeed
            }

            if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                originZoffset -= movementSpeed
            } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                originZoffset += movementSpeed
            }

            if (Gdx.input.isKeyPressed(Input.Keys.Z)) {
                originXoffset = 0F
                originZoffset = 0F
            }

            camera.freshBuckets = true
            camera.projection = Projection(
                Vector(camera.initialOrigin.x + originXoffset, camera.initialOrigin.y, camera.initialOrigin.z + originZoffset),
                Vector(camera.initialDirection.x + originXoffset, camera.initialDirection.y, camera.initialDirection.z + originZoffset),
                camera.fieldOfView,
                camera.aspectRatio
            )
        }
    }

    private fun handleResolutionChange() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.T)) {
            camera.renderWidth += 50
            camera.renderHeight += 25
            camera.freshBuckets = true
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.G)) {
            if (camera.renderWidth > 50) {
                camera.renderWidth -= 50
                camera.renderHeight -= 25
                camera.freshBuckets = true
            }
        }
    }

    private fun handleSamplingChanges() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.U)) {
            camera.samplesPerPass++
            camera.freshBuckets = true
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.J) && camera.samplesPerPass > 1) {
            camera.samplesPerPass--
            camera.freshBuckets = true
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.I)) {
            camera.maxSamples++
            camera.freshBuckets = true
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.K)) {
            if (camera.maxSamples > 1) {
                camera.maxSamples--
                camera.freshBuckets = true
            }
        }
    }

    private fun handleBounceChanges() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.Y)) {
            camera.maxBounces++
            camera.freshBuckets = true
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.H) && camera.maxBounces > 1) {
            camera.maxBounces--
            camera.freshBuckets = true
        }
    }

    private fun getTexture(colourGrid: Array<Array<ColourBucket>>): Texture {
        val pixMap = Pixmap(colourGrid.indices.last, colourGrid[0].indices.last, Pixmap.Format.RGB888)
        for (j in colourGrid.indices) {
            for (i in colourGrid[0].indices) {
                val c = colourGrid[j][i].getColour()
                pixMap.setColor(c.r, c.g, c.b, 1f)
                pixMap.drawPixel(j, i)
            }
        }
        val texture = Texture(pixMap)
        pixMap.dispose()
        return texture
    }

    private fun drawStats() {
        updateStats()
        game.font.draw(game.batch, getStats(), 5f, height - 5f)
    }

    private fun updateStats() {
        if (lastRenderTimes.size > 5) {
            lastRenderTimes.remove()
        }
        if (frameCount % 5 == 0) {
            fps = (1000f / (lastRenderTimes.sum() / lastRenderTimes.size)).toInt().toString()
        }
    }

    private fun getStats() =
        "FPS: $fps \n" +
        "Render Time: ${lastRenderTimes.sum() / lastRenderTimes.size}ms \n" +
        "Resolution: ${camera.renderWidth}x${camera.renderHeight}\n" +
        "Max Bounces:  ${camera.maxBounces}\n" +
        "Samples: ${camera.samplesPerPass}\n" +
        "Max Samples:  ${camera.maxSamples}"

}