package thebetweenlands.common.item.herblore;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import thebetweenlands.api.aspect.AspectContainer;
import thebetweenlands.api.rune.IRuneUser;
import thebetweenlands.api.rune.impl.RuneChain;
import thebetweenlands.api.rune.impl.RuneChain.IAspectBuffer;
import thebetweenlands.client.tab.BLCreativeTabs;
import thebetweenlands.common.herblore.rune.RuneDestroyBlock;
import thebetweenlands.common.herblore.rune.RuneMarkArea;
import thebetweenlands.common.herblore.rune.RuneSelectGrass;
import thebetweenlands.common.registries.AspectRegistry;

public class ItemRuneChain extends Item {
	public ItemRuneChain() {
		this.setMaxStackSize(1);
		this.setCreativeTab(BLCreativeTabs.SPECIALS);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		if(!world.isRemote) {
			RuneChain.Blueprint bp = new RuneChain.Blueprint();

			bp.addNodeBlueprint(0, new RuneMarkArea.Blueprint());
			//bp.addNodeBlueprint(1, new NodeDestroyBlocks.Blueprint());
			bp.addNodeBlueprint(1, new RuneSelectGrass.Blueprint());
			bp.addNodeBlueprint(2, new RuneDestroyBlock.Blueprint());

			System.out.println("Link mark -> grass 1: " + bp.link(1, 0, 0, 0));
			System.out.println("Link mark -> grass 2: " + bp.link(1, 1, 0, 1));
			System.out.println("Link mark -> destroy 1: " + bp.link(2, 0, 0, 0));
			System.out.println("Link mark -> destroy 2: " + bp.link(2, 1, 0, 1));

			final RuneChain composition = bp.create();

			final AspectContainer aspects = new AspectContainer();

			aspects.add(AspectRegistry.ORDANIIS, 10000);

			final IRuneUser user = new IRuneUser() {
				@Override
				public World getWorld() {
					return player.world;
				}

				@Override
				public Vec3d getPosition() {
					return player.getPositionVector();
				}

				@Override
				public Vec3d getLook() {
					return player.getLookVec();
				}

				@Override
				public Entity getEntity() {
					return player;
				}

				@Override
				public IInventory getInventory() {
					return player.inventory;
				}
			};

			final IAspectBuffer buffer = type -> aspects;
			composition.setAspectBuffer(buffer);

			composition.run(user);

			EntityItem item = new EntityItem(world, player.posX, player.posY, player.posZ, new ItemStack(Items.DIAMOND)) {
				@Override
				public void onUpdate() {
					super.onUpdate();

					if(composition.isRunning()) {
						composition.update();
					} else {
						this.setDead();
					}
				}
			};

			item.setPickupDelay(20);

			world.spawnEntity(item);
		}

		return super.onItemRightClick(world, player, hand);
	}
}
