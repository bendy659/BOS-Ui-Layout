package ru.benos.libs.bos_ui_layout

import net.minecraft.client.Minecraft
import net.minecraft.resources.ResourceLocation
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL20

object UiShaderCache {
    private val CACHE: MutableMap<String, Int> = mutableMapOf()

    class Builder(val program: Int) {
        fun getUniform(name: String): Int =
            GL20.glGetUniformLocation(program, name)

        fun setUniform(name: String, value: Float) =
            getUniform(name).value(value)

        fun Int.value(value: Float) =
            GL20.glUniform1f(this, value)

        fun Int.vec2(x: Float, y: Float) =
            GL20.glUniform2f(this, x, y)

        fun Int.vec3(x: Float, y: Float, z: Float) =
            GL20.glUniform3f(this, x, y, z)

        fun Int.vec4(x: Float, y: Float, z: Float, w: Float) =
            GL20.glUniform4f(this, x, y, z, w)
    }

    fun clear() {
        CACHE.values.forEach { GL20.glDeleteProgram(it) }
        CACHE.clear()
    }

    fun get(resource: String): Int =
        CACHE.getOrPut(resource) { compile(resource) }

    fun getSimple(): Int =
        get("bos_ui_layout:simple")

    private fun compile(resource: String): Int {
        println("Compiling shader: $resource")

        val manager = Minecraft.getInstance().resourceManager

        val split = resource.split(':', )
        val rl    = ResourceLocation.parse("${split[0]}:shaders/core/${split[1]}")

        val vertexRl = ResourceLocation.parse("${rl.namespace}:${rl.path}.vsh")
        val vertexStr = manager.openAsReader(vertexRl).readText()

        val fragmentRl = ResourceLocation.parse("${rl.namespace}:${rl.path}.fsh")
        val fragmentStr = manager.openAsReader(fragmentRl).readText()

        val vertexCompiled = compileShader(GL20.GL_VERTEX_SHADER, vertexStr)
        val fragmentCompiled = compileShader(GL20.GL_FRAGMENT_SHADER, fragmentStr)

        val program = GL20.glCreateProgram()
        GL20.glAttachShader(program, vertexCompiled)
        GL20.glAttachShader(program, fragmentCompiled)
        GL20.glLinkProgram(program)

        if (GL20.glGetProgrami(program, GL20.GL_LINK_STATUS) == GL11.GL_FALSE) {
            println("Program link error: ${GL20.glGetProgramInfoLog(program)}")
        }

        GL20.glDeleteShader(vertexCompiled)
        GL20.glDeleteShader(fragmentCompiled)

        return program
    }

    private fun compileShader(type: Int, str: String): Int {
        val shader = GL20.glCreateShader(type)

        GL20.glShaderSource(shader, str)
        GL20.glCompileShader(shader)

        if (GL20.glGetShaderi(shader, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            println("Shader compile error: ${GL20.glGetShaderInfoLog(shader)}")
        }

        return shader
    }

    fun getUniform(program: Int, name: String): Int =
        GL20.glGetUniformLocation(program, name)

    fun setUniform(program: Int, name: String, value: Float) =
        GL20.glUniform1f(getUniform(program, name), value)

    fun setUniform(program: Int, name: String, x: Float, y: Float) =
        GL20.glUniform2f(getUniform(program, name), x, y)
}