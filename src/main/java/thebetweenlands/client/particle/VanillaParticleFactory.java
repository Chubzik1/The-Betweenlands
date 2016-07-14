package thebetweenlands.client.particle;

import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;

import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;

public class VanillaParticleFactory extends ParticleFactory {
	private final IParticleFactory factory;

	/**
	 * A vanilla particle factory that builds particles from an {@link IParticleFactory}.
	 * <p><b>Do note that the first value in the additional arguments must be the particle ID (default is 0)!</b>
	 * @param type
	 * @param factory
	 */
	public VanillaParticleFactory(Class type, IParticleFactory factory) {
		super(type);
		this.factory = factory;
	}

	@Override
	protected Particle createParticle(ImmutableParticleArgs args) {
		Object[] data = args.data;
		if(data.length > 1) {
			int[] additionalArgs = ArrayUtils.toPrimitive(Arrays.copyOfRange(data, 1, data.length - 1, Integer[].class));
			return this.factory.getEntityFX((int)data[0], args.world, args.x, args.y, args.z, args.motionX, args.motionY, args.motionZ, additionalArgs);
		} else {
			return this.factory.getEntityFX((int)data[0], args.world, args.x, args.y, args.z, args.motionX, args.motionY, args.motionZ);
		}
	}

	@Override
	protected void setBaseArguments(ParticleArgs args) {
		args.withData(0);
	}
}
