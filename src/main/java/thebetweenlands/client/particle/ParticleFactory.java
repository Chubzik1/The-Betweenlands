package thebetweenlands.client.particle;

import java.util.Arrays;
import java.util.function.Consumer;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.world.World;
import thebetweenlands.client.particle.ParticleTextureStitcher.IParticleSpriteReceiver;

public abstract class ParticleFactory<T extends Particle> {
	/**
	 * Immutable particle arguments
	 */
	public static class ImmutableParticleArgs {
		public final World world;
		public final double x, y, z, motionX, motionY, motionZ;
		public final float scale;
		public final int color;
		public final Object[] data;

		public ImmutableParticleArgs(World world, double x, double y, double z, ParticleArgs builder) {
			this.world = world;
			this.x = x;
			this.y = y;
			this.z = z;
			this.motionX = builder.motionX;
			this.motionY = builder.motionY;
			this.motionZ = builder.motionZ;
			this.scale = builder.scale;
			this.color = builder.color;
			this.data = builder.data;
			builder.reset();
		}
	}

	/**
	 * Particle arguments
	 */
	public static class ParticleArgs<T extends ParticleArgs> {
		public static final class ArgumentDataBuilder<T extends ParticleArgs> {
			private final Object[] data;
			private final T args;

			/**
			 * Creates a new argument data builder.
			 * Must not be longer than the additional arguments of the underlying default.
			 * @param size
			 * @param args
			 */
			private ArgumentDataBuilder(int size, T args) {
				this.data = new Object[size];
				this.args = args;
			}

			/**
			 * Sets the data at the specified index
			 * @param index
			 * @param data
			 * @return
			 */
			public ArgumentDataBuilder setData(int index, Object data) {
				this.data[index] = data;
				return this;
			}

			/**
			 * Sets the object at the specified index to {@link ParticleFactory#EMPTY_ARG}
			 * @param index
			 * @return
			 */
			public ArgumentDataBuilder setEmpty(int index) {
				this.data[index] = EMPTY_ARG;
				return this;
			}

			/**
			 * Replaces all null objects in the data array with {@link ParticleFactory#EMPTY_ARG}
			 * @return
			 */
			public ArgumentDataBuilder fillEmpty() {
				for(int i = 0; i < this.data.length; i++) {
					if(this.data[i] == null)
						this.data[i] = EMPTY_ARG;
				}
				return this;
			}

			/**
			 * Builds the additional arguments and adds them
			 * @return
			 */
			public T build() {
				this.args.withData(this.data);
				return (T) this.args;
			}
		}

		private static final ParticleArgs BUILDER = new ParticleArgs();

		private static final Object[] NO_DATA = new Object[0];

		private boolean motionSet = false;
		private double motionX, motionY, motionZ;
		private boolean scaleSet = false;
		private float scale;
		private boolean colorSet = false;
		private int color;
		private Object[] data;
		private boolean dataSet = false;

		private ParticleArgs() {
			this.reset();
		}

		/**
		 * Resets all values
		 */
		public void reset() {
			this.motionX = 0;
			this.motionY = 0;
			this.motionZ = 0;
			this.scale = 1;
			this.color = 0xFFFFFFFF;
			this.data = NO_DATA;
			this.dataSet = false;
			this.motionSet = false;
			this.scaleSet = false;
			this.colorSet = false;
		}

		/**
		 * Sets the motion values
		 * @param motionX
		 * @param motionY
		 * @param motionZ
		 * @return
		 */
		public T withMotion(double motionX, double motionY, double motionZ) {
			this.motionX = motionX;
			this.motionY = motionY;
			this.motionZ = motionZ;
			this.motionSet = true;
			return (T) this;
		}

		/**
		 * Sets the scale
		 * @param scale
		 * @return
		 */
		public T withScale(float scale) {
			this.scale = scale;
			this.scaleSet = true;
			return (T) this;
		}

		/**
		 * Sets the color
		 * @param color
		 * @return
		 */
		public T withColor(int color) {
			this.color = color;
			this.colorSet = true;
			return (T) this;
		}

		/**
		 * Sets the additional data.
		 * Must not be longer than the additional arguments of the underlying default.
		 * @param data
		 * @return
		 */
		public T withData(Object... data) {
			if(data == null) 
				data = NO_DATA;
			this.data = data;
			this.dataSet = true;
			return (T) this;
		}

		/**
		 * Returns an arguments builder
		 * @param size
		 * @return
		 */
		public ArgumentDataBuilder<T> withDataBuilder(int size) {
			return new ArgumentDataBuilder<T>(size, (T) this);
		}

		/**
		 * Returns the motion X
		 * @return
		 */
		public double getMotionX() {
			return this.motionX;
		}

		/**
		 * Returns the motion Y
		 * @return
		 */
		public double getMotionY() {
			return this.motionY;
		}

		/**
		 * Returns the motion Z
		 * @return
		 */
		public double getMotionZ() {
			return this.motionZ;
		}

		/**
		 * Returns whether the motion was set
		 * @return
		 */
		public boolean isMotionSet() {
			return this.motionSet;
		}

		/**
		 * Returns the scale
		 * @return
		 */
		public float getScale() {
			return this.scale;
		}

		/**
		 * Returns whether the scale was set
		 * @return
		 */
		public boolean isScaleSet() {
			return this.scaleSet;
		}

		/**
		 * Returns the color
		 * @return
		 */
		public int getColor() {
			return this.color;
		}

		/**
		 * Returns whether the color was set
		 * @return
		 */
		public boolean isColorSet() {
			return this.colorSet;
		}

		/**
		 * Returns the additional data
		 * @return
		 */
		public Object[] getData() {
			return this.data;
		}

		/**
		 * Returns whether the additional data was set
		 * @return
		 */
		public boolean isDataSet() {
			return this.dataSet;
		}

		/**
		 * Returns a singleton instance of ParticleArgs
		 * @return
		 */
		public static ParticleArgs get() {
			BUILDER.reset();
			return BUILDER;
		}

		/**
		 * Populates the specified {@link ParticleArgs} with the composed particle arguments where this {@link ParticleArgs} is the underlying default.
		 * The additional data of the specified {@link ParticleArgs} must have a length equal or smaller than the length of the additional data of this {@link ParticleArgs}.
		 * @param container Arguments to be populated with default arguments if necessary
		 * @return
		 */
		public final ParticleArgs getArguments(ParticleArgs container) {
			if(!container.isMotionSet()) {
				container.withMotion(this.getMotionX(), this.getMotionY(), this.getMotionZ());
			}
			if(!container.isColorSet()) {
				container.withColor(this.getColor());
			}
			if(!container.isScaleSet()) {
				container.withScale(this.getScale());
			}
			if(this.isDataSet()) {
				Object[] initialAdditionalArgs = container.getData();
				Object[] defaultArgs = this.getData();
				Object[] additionalArgs = container.getData();
				if(defaultArgs.length > initialAdditionalArgs.length) {
					container.withData(additionalArgs = new Object[defaultArgs.length]);
				}
				for(int i = 0; i < additionalArgs.length; i++) {
					if(i < initialAdditionalArgs.length) {
						if(initialAdditionalArgs[i] == EMPTY_ARG) {
							if(i >= defaultArgs.length)
								throw new IndexOutOfBoundsException(String.format("The length of the additional arguments of "
										+ "the specified ParticleArgs must be equal or smaller than "
										+ "the underlying default additional arguments. Index: %s, Length: %s", i, defaultArgs.length));
							additionalArgs[i] = defaultArgs[i];
						} else {
							additionalArgs[i] = initialAdditionalArgs[i];
						}
					} else if(i < defaultArgs.length) {
						additionalArgs[i] = defaultArgs[i];
					}
				}
			}
			return container;
		}

	}

	private final ParticleTextureStitcher<T> stitcher;
	private final Class<T> type;
	private final ParticleArgs baseArgs;
	private final ParticleArgs defaultArgs;

	/**
	 * Creates a new particle factory for the specified particle type
	 * @param type
	 */
	public ParticleFactory(Class<T> type) {
		this(type, null);
	}

	/**
	 * Creates a new particle factory for the specified particle and adds a particle texture stitcher
	 * @param type
	 * @param stitcher
	 */
	public ParticleFactory(Class<T> type, @Nullable ParticleTextureStitcher<T> stitcher) {
		this.stitcher = stitcher;
		this.type = type;
		this.baseArgs = new ParticleArgs();
		this.setBaseArguments(this.baseArgs);
		this.defaultArgs = new ParticleArgs();
	}

	/**
	 * Returns the particle texture stitcher
	 * @return
	 */
	public final ParticleTextureStitcher<? extends Particle> getStitcher() {
		return this.stitcher;
	}

	/**
	 * Returns the particle type
	 * @return
	 */
	public final Class<T> getType() {
		return this.type;
	}

	/**
	 * Sets the color of the specified particle
	 * @param particle
	 * @param color
	 */
	public static final void setParticleColor(Particle particle, int color) {
		particle.setRBGColorF((float)(color >> 16 & 0xff) / 255F, (float)(color >> 8 & 0xff) / 255F, (float)(color & 0xff) / 255F);
		particle.setAlphaF((float)(color >> 24 & 0xff) / 255F);
	}

	/**
	 * Creates a new particle from the immutable particle args and sets the sprites
	 * @param args
	 * @return
	 */
	protected final T getParticle(ImmutableParticleArgs args) {
		T particle = this.createParticle(args);
		if(this.getStitcher() != null) {
			((IParticleSpriteReceiver)particle).setStitchedSprites(this.getStitcher().getSprites());
		}
		setParticleColor(particle, args.color);
		return particle;
	}

	/**
	 * Creates a new particle from the immutable particle args
	 * @param args
	 * @return
	 */
	protected abstract T createParticle(ImmutableParticleArgs args);

	/**
	 * Specifies an additional argument as empty. Any empty argument will be replaced by the 
	 * underlying default argument or null if an underlying default argument is not available. Using this in 
	 * {@link ParticleFactory#setBaseArguments(ParticleArgs)} has no effect. If this is used in 
	 * {@link ParticleFactory#setDefaultArguments(World, ParticleArgs)} the underlying default is
	 * from {@link ParticleFactory#setBaseArguments(ParticleArgs)}.
	 */
	public static final Object EMPTY_ARG = new Object();

	/**
	 * Sets the base arguments.
	 * Using {@link ParticleFactory#EMPTY_ARG} has no effect.
	 * @param args
	 */
	protected void setBaseArguments(ParticleArgs args) { }

	/**
	 * Sets the default arguments based on the world the particle is being spawned in.
	 * The underlying default arguments are set by {@link ParticleFactory#setBaseArguments(ParticleArgs)}.
	 * Overrides arguments set by {@link ParticleFactory#setBaseArguments(ParticleArgs)}.
	 * @param world
	 * @param args
	 */
	protected void setDefaultArguments(World world, ParticleArgs args) { }

	public static final class BaseArgsBuilder extends ParticleArgs<BaseArgsBuilder> {
		private final ParticleFactory factory;

		private BaseArgsBuilder(ParticleFactory factory) {
			super();
			this.factory = factory;
		}

		/**
		 * Runs a {@link Consumer} over this {@link ParticleArgs}
		 * @param consumer
		 * @return
		 */
		public BaseArgsBuilder accept(Consumer<ParticleArgs> consumer) {
			consumer.accept(this);
			return this;
		}

		/**
		 * Builds the arguments and sets the base arguments of the factory if overwritten
		 * @return
		 */
		public ParticleFactory build() {
			if(this.isMotionSet())
				this.factory.baseArgs.withMotion(this.getMotionX(), this.getMotionY(), this.getMotionZ());
			if(this.isColorSet())
				this.factory.baseArgs.withColor(this.getColor());
			if(this.isScaleSet())
				this.factory.baseArgs.withScale(this.getScale());
			if(this.isDataSet())
				this.factory.baseArgs.withData(this.getData());
			return this.factory;
		}
	}

	private final BaseArgsBuilder baseArgsBuilder = new BaseArgsBuilder(this);

	/**
	 * Returns the base arguments builder.
	 * Used to override base arguments set by {@link ParticleFactory#setBaseArguments(ParticleArgs)}.
	 * @return
	 */
	public BaseArgsBuilder getBaseArgsBuilder(){
		this.baseArgsBuilder.reset();
		return this.baseArgsBuilder;
	}

	/**
	 * Creates an instance of a particle.
	 * The specified {@link ParticleArgs} overrides the default arguments set by {@link ParticleFactory#setDefaultArguments(World, ParticleArgs)}
	 * @param type
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @param args
	 * @return
	 */
	public final T create(World world, double x, double y, double z, @Nullable ParticleArgs args) {
		if(args == null)
			args = ParticleArgs.get();
		this.defaultArgs.reset();
		this.setDefaultArguments(world, this.defaultArgs);
		boolean hasActualDefaults = this.defaultArgs.isColorSet() || this.defaultArgs.isMotionSet() || this.defaultArgs.isScaleSet() || this.defaultArgs.isDataSet();
		if(hasActualDefaults) {
			args = this.baseArgs.getArguments(this.defaultArgs).getArguments(args);
		} else {
			args = this.baseArgs.getArguments(args);
		}
		return this.getParticle(new ImmutableParticleArgs(world, x, y, z, args));
	}

	/**
	 * Spawns a particle.
	 * The specified {@link ParticleArgs} overrides the default arguments set by {@link ParticleFactory#setDefaultArguments(World, ParticleArgs)}
	 * @param type
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @param args
	 * @return
	 */
	public final T spawn(World world, double x, double y, double z, @Nullable ParticleArgs args) {
		T particle = this.create(world, x, y, z, args);
		if(particle != null)
			Minecraft.getMinecraft().effectRenderer.addEffect(particle);
		return particle;
	}
}
