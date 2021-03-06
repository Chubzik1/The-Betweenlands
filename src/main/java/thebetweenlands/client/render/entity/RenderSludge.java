package thebetweenlands.client.render.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.CullFace;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thebetweenlands.client.render.model.entity.ModelSludge;
import thebetweenlands.common.entity.mobs.EntitySludge;

@SideOnly(Side.CLIENT)
public class RenderSludge extends RenderLiving<EntitySludge> {
	private static final ResourceLocation TEXTURE = new ResourceLocation("thebetweenlands:textures/entity/sludge.png");

	public RenderSludge(RenderManager renderManager) {
		this(renderManager, new ModelSludge());
	}

	public RenderSludge(RenderManager renderManager, ModelBase model) {
		super(renderManager, model, 0.0f);
	}

	@Override
	protected void preRenderCallback(EntitySludge entity, float partialTicks) {
		float squishFactor = entity.getSquishFactor(partialTicks) / 1.5F;
		float scale = 1.0F / (squishFactor + 1.0F);

		GlStateManager.translate(0, (1.0F - entity.scale.getAnimationProgressSin(partialTicks)) * 2.5F, 0);
		GlStateManager.scale(scale, 1.0F / scale, scale);
	}

	@Override
	public void doRender(EntitySludge entity, double x, double y, double z, float entityYaw, float partialTicks) {
		GlStateManager.depthMask(false);

		GlStateManager.cullFace(CullFace.FRONT);
		super.doRender(entity, x, y, z, entityYaw, partialTicks);

		GlStateManager.cullFace(CullFace.BACK);
		super.doRender(entity, x, y, z, entityYaw, partialTicks);

		GlStateManager.depthMask(true);
		GlStateManager.colorMask(false, false, false, false);

		super.doRender(entity, x, y, z, entityYaw, partialTicks);

		GlStateManager.colorMask(true, true, true, true);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntitySludge entity) {
		return TEXTURE;
	}
}
