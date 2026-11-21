package org.cobra.moreores.client.render.item.entity;//package org.cobra.moreores.client.render.item.entity;

import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.ArrowRenderState;
import net.minecraft.resources.Identifier;
import org.cobra.moreores.MoreOresModLoader;
import org.cobra.moreores.world.entity.GemArrow;

public class GemArrowEntityRenderer extends ArrowRenderer<GemArrow, ArrowRenderState> {
    public static final Identifier TEXTURE = MoreOresModLoader.id("textures/entity/item/gem_arrow.png");
    public GemArrowEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected Identifier getTextureLocation(ArrowRenderState state) {
        return TEXTURE;
    }

    @Override
    public ArrowRenderState createRenderState() {
        return new ArrowRenderState();
    }

    @Override
    public void extractRenderState(GemArrow persistentProjectileEntity, ArrowRenderState projectileEntityRenderState, float f) {
        super.extractRenderState(persistentProjectileEntity, projectileEntityRenderState, f);
    }
}
