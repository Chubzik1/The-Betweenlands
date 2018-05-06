package thebetweenlands.common.config.properties;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import gnu.trove.map.TIntIntMap;
import gnu.trove.map.hash.TIntIntHashMap;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;
import thebetweenlands.common.TheBetweenlands;
import thebetweenlands.common.config.BetweenlandsConfig;
import thebetweenlands.common.config.ConfigProperty;

public class PortalTargetList extends ConfigProperty {
	private Map<String, TIntIntMap> itemList;

	@Override
	protected void init() {
		this.itemList = new HashMap<>();

		String[] unparsed = BetweenlandsConfig.WORLD_AND_DIMENSION.portalDimensionTargets;
		for(String listed : unparsed) {
			try {
				String[] data = listed.split(":");
				String block = data[0] + ":" + data[1];
				if(this.itemList.get(block) == null || !this.itemList.get(block).containsKey(OreDictionary.WILDCARD_VALUE)) {
					int meta = 0;
					if(data.length > 2) {
						String metaStr = data[2].split("/")[0];
						if(!"*".equals(metaStr)) {
							try {
								meta = Integer.parseInt(metaStr);
							} catch(NumberFormatException ex) {
								TheBetweenlands.logger.error("Failed to parse item: " + listed + ". Invalid metadata: " + metaStr);
								continue;
							}
						} else {
							meta = OreDictionary.WILDCARD_VALUE;
						}
					}
					TIntIntMap metaMap = this.itemList.get(block);
					if(metaMap == null) {
						this.itemList.put(block, metaMap = new TIntIntHashMap());
					}
					if(meta == OreDictionary.WILDCARD_VALUE) {
						metaMap.clear();
					}
					String[] dimData = listed.split("/");
					if(dimData.length < 2) {
						TheBetweenlands.logger.error("Failed to parse item, missing dimension: " + listed);
						continue;
					}
					int dim;
					try {
						dim = Integer.parseInt(dimData[1]);
					} catch(NumberFormatException ex) {
						TheBetweenlands.logger.error("Failed to parse item: " + listed + ". Invalid dimension: " + dimData[1]);
						continue;
					}
					metaMap.put(meta, dim);
				}
			} catch (Exception e) {
				TheBetweenlands.logger.error("Failed to parse item: " + listed);
			}
		}
	}

	/**
	 * Returns whether the specified block is listed
	 * @param stack The block state
	 * @return
	 */
	public boolean isListed(IBlockState state) {
		ResourceLocation name = state.getBlock().getRegistryName();

		TIntIntMap metaMap = this.itemList.get(name.toString());

		if(metaMap == null) {
			return false;
		}

		if(metaMap.containsKey(OreDictionary.WILDCARD_VALUE)) {
			return true;
		}
		
		int meta = state.getBlock().getMetaFromState(state);

		return metaMap.containsKey(meta);
	}
	
	/**
	 * Returns the target dimension for the specified block or
	 * the default return dimension set in the config
	 * @param state The block state
	 * @return
	 */
	public int getDimension(IBlockState state) {
		ResourceLocation name = state.getBlock().getRegistryName();

		TIntIntMap metaMap = this.itemList.get(name.toString());

		if(metaMap == null) {
			return BetweenlandsConfig.WORLD_AND_DIMENSION.portalDefaultReturnDimension;
		}

		if(metaMap.containsKey(OreDictionary.WILDCARD_VALUE)) {
			return metaMap.get(OreDictionary.WILDCARD_VALUE);
		}
		
		int meta = state.getBlock().getMetaFromState(state);

		if(metaMap.containsKey(meta)) {
			return metaMap.get(meta);
		}
		
		return BetweenlandsConfig.WORLD_AND_DIMENSION.portalDefaultReturnDimension;
	}
}