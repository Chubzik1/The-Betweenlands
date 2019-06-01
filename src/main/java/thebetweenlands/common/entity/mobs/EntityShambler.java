package thebetweenlands.common.entity.mobs;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thebetweenlands.api.entity.IEntityBL;
import thebetweenlands.common.entity.ai.EntityAIHurtByTargetImproved;
import thebetweenlands.common.registries.SoundRegistry;

public class EntityShambler extends EntityMob implements IEntityMultiPart, IEntityBL {

	private static final DataParameter<Boolean> JAWS_OPEN = EntityDataManager.createKey(EntityShambler.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> TONGUE_EXTEND = EntityDataManager.createKey(EntityShambler.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Integer> JAW_ANGLE = EntityDataManager.createKey(EntityShambler.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> JAW_ANGLE_PREV = EntityDataManager.createKey(EntityShambler.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> TONGUE_LENGTH = EntityDataManager.createKey(EntityShambler.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> TONGUE_LENGTH_PREV = EntityDataManager.createKey(EntityShambler.class, DataSerializers.VARINT);

	public MultiPartEntityPart[] tongue_array; // we may want to make more tongue parts
	public MultiPartEntityPart tongue_end = new MultiPartEntityPart(this, "tongue_end", 0.5F, 0.5F);
	public MultiPartEntityPart tongue_1 = new MultiPartEntityPart(this, "tongue_1", 0.125F, 0.125F);
	public MultiPartEntityPart tongue_2 = new MultiPartEntityPart(this, "tongue_2", 0.125F, 0.125F);
	public MultiPartEntityPart tongue_3 = new MultiPartEntityPart(this, "tongue_3", 0.125F, 0.125F);
	public MultiPartEntityPart tongue_4 = new MultiPartEntityPart(this, "tongue_4", 0.125F, 0.125F);
	public MultiPartEntityPart tongue_5 = new MultiPartEntityPart(this, "tongue_5", 0.125F, 0.125F);
	
	public MultiPartEntityPart tongue_6 = new MultiPartEntityPart(this, "tongue_6", 0.125F, 0.125F);
	public MultiPartEntityPart tongue_7 = new MultiPartEntityPart(this, "tongue_7", 0.125F, 0.125F);
	public MultiPartEntityPart tongue_8 = new MultiPartEntityPart(this, "tongue_8", 0.125F, 0.125F);
	public MultiPartEntityPart tongue_9 = new MultiPartEntityPart(this, "tongue_9", 0.125F, 0.125F);
	public MultiPartEntityPart tongue_10 = new MultiPartEntityPart(this, "tongue_10", 0.125F, 0.125F);
	
	public MultiPartEntityPart tongue_11 = new MultiPartEntityPart(this, "tongue_11", 0.125F, 0.125F);
	public MultiPartEntityPart tongue_12 = new MultiPartEntityPart(this, "tongue_12", 0.125F, 0.125F);
	public MultiPartEntityPart tongue_13 = new MultiPartEntityPart(this, "tongue_13", 0.125F, 0.125F);
	public MultiPartEntityPart tongue_14 = new MultiPartEntityPart(this, "tongue_14", 0.125F, 0.125F);
	public MultiPartEntityPart tongue_15 = new MultiPartEntityPart(this, "tongue_15", 0.125F, 0.125F);

	public EntityShambler(World world) {
		super(world);
		this.setSize(0.95F, 1.25F);
		tongue_array = new MultiPartEntityPart[] {tongue_end, tongue_1, tongue_2, tongue_3, tongue_4, tongue_5, tongue_6, tongue_7, tongue_8, tongue_9, tongue_10, tongue_11, tongue_12, tongue_13, tongue_14, tongue_15};
	}

	@Override
	public World getWorld() {
		return getEntityWorld();
	}

	@Override
	protected void initEntityAI() {
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityAIAttackMelee(this, 0.8D, true));
		this.tasks.addTask(2, new EntityAIMoveTowardsRestriction(this, 1.0D));
		this.tasks.addTask(3, new EntityAIWander(this, 0.75D));
		//this.tasks.addTask(4, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(5, new EntityAILookIdle(this));

		this.targetTasks.addTask(0, new EntityAIHurtByTargetImproved(this, true));
		this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, true));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<EntityVillager>(this, EntityVillager.class, false));
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataManager.register(JAWS_OPEN, false);
		dataManager.register(TONGUE_EXTEND, false);
		dataManager.register(JAW_ANGLE, 0);
		dataManager.register(JAW_ANGLE_PREV, 0);
		dataManager.register(TONGUE_LENGTH, 0);
		dataManager.register(TONGUE_LENGTH_PREV, 0);
	}

	public boolean jawsAreOpen() {
		return dataManager.get(JAWS_OPEN);
	}

	public void setOpenJaws(boolean jawState) {
		dataManager.set(JAWS_OPEN, jawState);
	}

	public boolean isExtendingTongue() {
		return dataManager.get(TONGUE_EXTEND);
	}

	public void setExtendingTongue(boolean tongueState) {
		dataManager.set(TONGUE_EXTEND, tongueState);
	}

	public void setJawAngle(int count) {
		dataManager.set(JAW_ANGLE, count);
	}

	public void setJawAnglePrev(int count) {
		dataManager.set(JAW_ANGLE_PREV, count);
	}

	public void setTongueLength(int count) {
		dataManager.set(TONGUE_LENGTH, count);
	}

	public void setTongueLengthPrev(int count) {
		dataManager.set(TONGUE_LENGTH, count);
	}

	public int getJawAngle() {
		return dataManager.get(JAW_ANGLE);
	}

	public int getJawAnglePrev() {
		return dataManager.get(JAW_ANGLE_PREV);
	}

	public int getTongueLength() {
		return dataManager.get(TONGUE_LENGTH);
	}

	public int getTongueLengthPrev() {
		return dataManager.get(TONGUE_LENGTH);
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
		getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
		getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2.0D);
		getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(28.0D);
	}

	@Override
	public boolean getCanSpawnHere() {
		return super.getCanSpawnHere();
	}

	@Override
	public int getMaxSpawnedInChunk() {
		return 3;
	}
	
	@Override
	protected float getSoundPitch() {
		return super.getSoundPitch() * 1.5F;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return SoundRegistry.SHAMBLER_LIVING;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSource) {
		return SoundRegistry.SHAMBLER_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundRegistry.SHAMBLER_DEATH;
	}

	@Override
	public void onLivingUpdate() {
		if (!getEntityWorld().isRemote) {

			if (getAttackTarget() != null && canEntityBeSeen(getAttackTarget())) {
				faceEntity(getAttackTarget(), 10.0F, 20.0F);
				double distance = getDistance(getAttackTarget().posX, getAttackTarget().getEntityBoundingBox().minY, getAttackTarget().posZ);

				if (distance > 5.0D) {
					if(jawsAreOpen()) {
						setOpenJaws(false);
						if (isExtendingTongue())
							setExtendingTongue(false);
					}
				}

				if (distance <= 5.0D && distance >= 1) {
					if (!jawsAreOpen()) {
						setOpenJaws(true);
						if (!isExtendingTongue()) {
							setExtendingTongue(true);
							playSound(SoundRegistry.SHAMBLER_LICK, 1F, 1F + this.rand.nextFloat() * 0.3F);
						}
					}
				}
			}

			if (getAttackTarget() == null) {
				if(jawsAreOpen())
					setOpenJaws(false);
				if (isExtendingTongue())
					setExtendingTongue(false);
			}

			setJawAnglePrev(getJawAngle());
			setTongueLengthPrev(getTongueLength());

			if (getJawAngle() > 0 && !jawsAreOpen())
				setJawAngle(getJawAngle() - 1);

			if (jawsAreOpen() && getJawAngle() <= 10)
				setJawAngle(getJawAngle() + 1);

			if (getJawAngle() < 0 && !jawsAreOpen())
				setJawAngle(0);

			if (jawsAreOpen() && getJawAngle() > 10)
				setJawAngle(10);

			if (getTongueLength() > 0 && !isExtendingTongue())
				setTongueLength(getTongueLength() - 1);

			if (isExtendingTongue() && getTongueLength() <= 9)
				setTongueLength(getTongueLength() + 1);

			if (getTongueLength() < 0 && !isExtendingTongue())
				setTongueLength(0);

			if (isExtendingTongue() && getTongueLength() > 9) {
				setTongueLength(9);
				setExtendingTongue(false);
			}
		}
		super.onLivingUpdate();
	}

	@Override
    public void onUpdate() {
		super.onUpdate();
		renderYawOffset = rotationYaw;
		double a = Math.toRadians(rotationYaw);
		double offSetX = Math.sin(a) * -0.25D;
		double offSetZ = -Math.cos(a) * -0.25D;
		Vec3d vector = getLookVec();
		tongue_end.setLocationAndAngles(posX + offSetX + ((double) vector.x * getTongueLength() * 0.5D), (posY + 0.90625D - 0.3125D) + ((double) vector.y * getTongueLength() * 0.5D), posZ + offSetZ + ((double) vector.z * getTongueLength() * 0.5D), rotationYaw, rotationPitch);
		tongue_1.setLocationAndAngles(posX + offSetX + ((double) vector.x * getTongueLength() * 0.03125D), (posY + 0.90625D - 0.125D) + ((double) vector.y * getTongueLength() * 0.03125D), posZ + offSetZ + ((double) vector.z * getTongueLength() * 0.03125D), rotationYaw, rotationPitch);
		tongue_2.setLocationAndAngles(posX + offSetX + ((double) vector.x * getTongueLength() * 0.0625D), (posY + 0.90625D - 0.125D) + ((double) vector.y * getTongueLength() * 0.0625D), posZ + offSetZ + ((double) vector.z * getTongueLength() * 0.0625D), rotationYaw, rotationPitch);
		tongue_3.setLocationAndAngles(posX + offSetX + ((double) vector.x * getTongueLength() * 0.09375D), (posY + 0.90625D - 0.125D) + ((double) vector.y * getTongueLength() * 0.09375D), posZ + offSetZ + ((double) vector.z * getTongueLength() * 0.09375D), rotationYaw, rotationPitch);
		tongue_4.setLocationAndAngles(posX + offSetX + ((double) vector.x * getTongueLength() * 0.125D), (posY + 0.90625D - 0.125D) + ((double) vector.y * getTongueLength() * 0.125D), posZ + offSetZ + ((double) vector.z * getTongueLength() * 0.125D), rotationYaw, rotationPitch);
		tongue_5.setLocationAndAngles(posX + offSetX + ((double) vector.x * getTongueLength() * 0.15625D), (posY + 0.90625D - 0.125D) + ((double) vector.y * getTongueLength() * 0.15625D), posZ + offSetZ + ((double) vector.z * getTongueLength() * 0.15625D), rotationYaw, rotationPitch);
		tongue_6.setLocationAndAngles(posX + offSetX + ((double) vector.x * getTongueLength() * 0.1875D), (posY + 0.90625D - 0.125D) + ((double) vector.y * getTongueLength() * 0.1875D), posZ + offSetZ + ((double) vector.z * getTongueLength() * 0.1875D), rotationYaw, rotationPitch);
		tongue_7.setLocationAndAngles(posX + offSetX + ((double) vector.x * getTongueLength() * 0.21875D), (posY + 0.90625D - 0.125D) + ((double) vector.y * getTongueLength() * 0.21875D), posZ + offSetZ + ((double) vector.z * getTongueLength() * 0.21875D), rotationYaw, rotationPitch);
		tongue_8.setLocationAndAngles(posX + offSetX + ((double) vector.x * getTongueLength() * 0.25D), (posY + 0.90625D - 0.125D) + ((double) vector.y * getTongueLength() * 0.25D), posZ + offSetZ + ((double) vector.z * getTongueLength() * 0.25D), rotationYaw, rotationPitch);
		tongue_9.setLocationAndAngles(posX + offSetX + ((double) vector.x * getTongueLength() * 0.28125D), (posY + 0.90625D - 0.125D) + ((double) vector.y * getTongueLength() * 0.28125D), posZ + offSetZ + ((double) vector.z * getTongueLength() * 0.28125D), rotationYaw, rotationPitch);
		tongue_10.setLocationAndAngles(posX + offSetX + ((double) vector.x * getTongueLength() * 0.3125D), (posY + 0.90625D - 0.125D) + ((double) vector.y * getTongueLength() * 0.3125D), posZ + offSetZ + ((double) vector.z * getTongueLength() * 0.3125D), rotationYaw, rotationPitch);
		tongue_11.setLocationAndAngles(posX + offSetX + ((double) vector.x * getTongueLength() * 0.34375D), (posY + 0.90625D - 0.125D) + ((double) vector.y * getTongueLength() * 0.34375D), posZ + offSetZ + ((double) vector.z * getTongueLength() * 0.34375D), rotationYaw, rotationPitch);
		tongue_12.setLocationAndAngles(posX + offSetX + ((double) vector.x * getTongueLength() * 0.375D), (posY + 0.90625D - 0.125D) + ((double) vector.y * getTongueLength() * 0.375D), posZ + offSetZ + ((double) vector.z * getTongueLength() * 0.375D), rotationYaw, rotationPitch);
		tongue_13.setLocationAndAngles(posX + offSetX + ((double) vector.x * getTongueLength() * 0.40625D), (posY + 0.90625D - 0.125D) + ((double) vector.y * getTongueLength() * 0.40625D), posZ + offSetZ + ((double) vector.z * getTongueLength() * 0.40625D), rotationYaw, rotationPitch);
		tongue_14.setLocationAndAngles(posX + offSetX + ((double) vector.x * getTongueLength() * 0.4375D), (posY + 0.90625D - 0.125D) + ((double) vector.y * getTongueLength() * 0.4375D), posZ + offSetZ + ((double) vector.z * getTongueLength() * 0.4375D), rotationYaw, rotationPitch);
		tongue_15.setLocationAndAngles(posX + offSetX + ((double) vector.x * getTongueLength() * 0.46875D), (posY + 0.90625D - 0.125D) + ((double) vector.y * getTongueLength() * 0.46875D), posZ + offSetZ + ((double) vector.z * getTongueLength() * 0.46875D), rotationYaw, rotationPitch);

		checkCollision();
    }

	@Override
	public void updatePassenger(Entity entity) {
		if (entity instanceof EntityLivingBase) {
			double a = Math.toRadians(rotationYaw);
			double offSetX = Math.sin(a) * getTongueLength() > 0 ? -0.125D : -0.35D;
			double offSetZ = -Math.cos(a) * getTongueLength() > 0 ? -0.125D : -0.35D;
			entity.setPosition(tongue_end.posX + offSetX, tongue_end.posY - entity.height * 0.3D, tongue_end.posZ + offSetZ);
		}
	}

	@Override
	public boolean canRiderInteract() {
		return true;
	}

	@Override
	public boolean shouldRiderSit() {
		return false;
	}

	@SuppressWarnings("unchecked")
	protected Entity checkCollision() {
		List<EntityLivingBase> list = getEntityWorld().getEntitiesWithinAABB(EntityLivingBase.class, tongue_end.getEntityBoundingBox());
		for (int i = 0; i < list.size(); i++) {
			Entity entity = list.get(i);
			if (entity != null && entity == getAttackTarget() && !(entity instanceof IEntityMultiPart) && !(entity instanceof MultiPartEntityPart)) {
				if (entity instanceof EntityLivingBase)
					if (!isBeingRidden()) {
						entity.startRiding(this, true);
						if (!getEntityWorld().isRemote)
							if (isExtendingTongue())
								setExtendingTongue(false); //eeeeeh!
					}
			}
		}
		return null;
	}

    @SideOnly(Side.CLIENT)
    public float smoothedAngle(float partialTicks) {
        return getJawAnglePrev() + (getJawAngle() - getJawAnglePrev()) * partialTicks;
    }

	@Override
	public boolean attackEntityFromPart(MultiPartEntityPart part, DamageSource source, float dmg) {
		damageShambler(source, dmg); // we may want seperate tongue damage - dunno
		return true;
	}

	protected boolean damageShambler(DamageSource source, float ammount) {
		return super.attackEntityFrom(source, ammount);
	}
}
