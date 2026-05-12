package ru.benos.libs.bos_ui_layout.nodes

import com.mojang.blaze3d.platform.NativeImage
import net.minecraft.client.Minecraft
import net.minecraft.resources.ResourceLocation
import ru.benos.libs.bos_ui_layout.UiDsl
import ru.benos.libs.bos_ui_layout.UiRuntime
import ru.benos.libs.bos_ui_layout.datas.IUiImageStretch
import ru.benos.libs.bos_ui_layout.datas.UiModifier
import ru.benos.libs.bos_ui_layout.datas.base.UiColor
import ru.benos.libs.bos_ui_layout.datas.base.UiRect
import ru.benos.libs.bos_ui_layout.datas.base.UiSize

@UiDsl
class UiImageNode(
    resource: String,

    private val multiplyColor: UiColor,
    private val uv: UiRect?,
    private val stretch: IUiImageStretch,

    override val modifier: UiModifier
) : AbstractUiNode() {
    private val resourceLocation = ResourceLocation.parse(resource)

    override val enableScissor: Boolean = true

    override fun measure(runtime: UiRuntime, availableSize: UiSize): UiSize {
        val inner = UiRect(0, 0, availableSize).shrink(modifier.padding)
        val source = resolveSource()

        val content =
            when (stretch) {
                IUiImageStretch.Expand -> inner.size
                else -> stretch.calcRect(source.size, inner).size
            }

        return modifier.resolveSize(content.width, content.height, availableSize)
            .applyTransformLayout()
    }

    override fun render(runtime: UiRuntime, bounds: UiRect) {
        registerEvents(runtime, bounds)

        transformative(runtime, bounds) {
            val inner = bounds.shrink(modifier.padding)
            val source = resolveSource()
            val imageBounds = stretch.calcRect(source.size, inner)

            scissor(runtime, inner) {
                renderImage(runtime, imageBounds, source)
            }
        }
    }

    private fun renderImage(runtime: UiRuntime, bounds: UiRect, source: Source) {
        if (bounds.width <= 0 || bounds.height <= 0) return

        runtime.guiGraphics.setColor(
            multiplyColor.rFloat,
            multiplyColor.gFloat,
            multiplyColor.bFloat,
            multiplyColor.aFloat
        )
        runtime.guiGraphics.blit(
            resourceLocation,
            bounds.x, bounds.y,
            bounds.width, bounds.height,
            source.uv.x.toFloat(), source.uv.y.toFloat(),
            source.uv.width, source.uv.height,
            source.textureSize.width, source.textureSize.height
        )
        runtime.guiGraphics.setColor(1.0f, 1.0f, 1.0f, 1.0f)
    }

    private fun resolveSource(): Source {
        val textureSize = getTextureSize(resourceLocation)
        val sourceUv = uv ?: UiRect(0, 0, textureSize)

        return Source(sourceUv, sourceUv.size, textureSize)
    }

    private data class Source(
        val uv: UiRect,
        val size: UiSize,
        val textureSize: UiSize
    )

    companion object {
        private val TEXTURE_SIZES: MutableMap<ResourceLocation, UiSize> = mutableMapOf()

        fun clearTextureCache() {
            TEXTURE_SIZES.clear()
        }

        private fun getTextureSize(resource: ResourceLocation): UiSize =
            TEXTURE_SIZES.getOrPut(resource) { readTextureSize(resource) }

        private fun readTextureSize(resource: ResourceLocation): UiSize {
            val manager = Minecraft.getInstance().resourceManager

            return manager.open(resource).use { stream ->
                NativeImage.read(stream).use { image ->
                    UiSize(image.width, image.height)
                }
            }
        }
    }
}
