package net.akashi.weaponmod.Entities.Projectiles.Arrows;

import com.google.common.collect.Sets;
import net.akashi.weaponmod.Registry.ModEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PlayMessages;

import java.util.Collection;
import java.util.Set;

//Basically A Copy Of Vanilla Arrow Class
public class BaseArrow extends AbstractArrow {
	private static final EntityDataAccessor<Integer> ID_EFFECT_COLOR
			= SynchedEntityData.defineId(net.minecraft.world.entity.projectile.Arrow.class, EntityDataSerializers.INT);
	private Potion potion = Potions.EMPTY;
	private final Set<MobEffectInstance> effects = Sets.newHashSet();
	private boolean fixedColor;
	private boolean isSpectralArrow = false;

	public BaseArrow(EntityType<? extends BaseArrow> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}

	public BaseArrow(EntityType<? extends BaseArrow> pEntityType, Level pLevel, double pX, double pY, double pZ) {
		super(pEntityType, pX, pY, pZ, pLevel);
	}

	public BaseArrow(EntityType<? extends BaseArrow> pEntityType, Level pLevel, LivingEntity pShooter) {
		super(pEntityType, pShooter, pLevel);
	}

	public void setSpectralArrow(boolean target) {
		isSpectralArrow = target;
	}

	public boolean isSpectralArrow() {
		return isSpectralArrow;
	}

	public void setEffectsFromItem(ItemStack pStack) {
		if (pStack.is(Items.TIPPED_ARROW)) {
			this.potion = PotionUtils.getPotion(pStack);
			Collection<MobEffectInstance> collection = PotionUtils.getCustomEffects(pStack);
			if (!collection.isEmpty()) {
				for (MobEffectInstance mobeffectinstance : collection) {
					this.effects.add(new MobEffectInstance(mobeffectinstance));
				}
			}

			int i = getCustomColor(pStack);
			if (i == -1) {
				this.updateColor();
			} else {
				this.setFixedColor(i);
			}
		} else if (pStack.is(Items.ARROW)) {
			this.potion = Potions.EMPTY;
			this.effects.clear();
			this.entityData.set(ID_EFFECT_COLOR, -1);
		}

	}

	public static int getCustomColor(ItemStack pStack) {
		CompoundTag compoundtag = pStack.getTag();
		return compoundtag != null && compoundtag.contains("CustomPotionColor", 99) ? compoundtag.getInt("CustomPotionColor") : -1;
	}

	private void updateColor() {
		this.fixedColor = false;
		if (this.potion == Potions.EMPTY && this.effects.isEmpty()) {
			this.entityData.set(ID_EFFECT_COLOR, -1);
		} else {
			this.entityData.set(ID_EFFECT_COLOR, PotionUtils.getColor(PotionUtils.getAllEffects(this.potion, this.effects)));
		}

	}

	public void addEffect(MobEffectInstance pEffectInstance) {
		this.effects.add(pEffectInstance);
		this.getEntityData().set(ID_EFFECT_COLOR, PotionUtils.getColor(PotionUtils.getAllEffects(this.potion, this.effects)));
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(ID_EFFECT_COLOR, -1);
	}

	@Override
	public void shootFromRotation(Entity pShooter, float pX, float pY, float pZ, float pVelocity, float pInaccuracy) {
		super.shootFromRotation(pShooter, pX, pY, pZ, pVelocity, pInaccuracy);
	}

	@Override
	public void tick() {
		super.tick();
		if (this.level().isClientSide) {
			if (this.inGround) {
				if (this.inGroundTime % 5 == 0) {
					this.makeParticle(1);
				}
			} else {
				this.makeParticle(2);
				if (isSpectralArrow) {
					this.level().addParticle(ParticleTypes.INSTANT_EFFECT, this.getX(), this.getY(), this.getZ(),
							0.0D, 0.0D, 0.0D);
				}
			}
		} else if (this.inGround && this.inGroundTime != 0 && !this.effects.isEmpty() && this.inGroundTime >= 600) {
			this.level().broadcastEntityEvent(this, (byte) 0);
			this.potion = Potions.EMPTY;
			this.effects.clear();
			this.entityData.set(ID_EFFECT_COLOR, -1);
		}

	}

	private void makeParticle(int pParticleAmount) {
		int i = this.getColor();
		if (i != -1 && pParticleAmount > 0) {
			double d0 = (double) (i >> 16 & 255) / 255.0D;
			double d1 = (double) (i >> 8 & 255) / 255.0D;
			double d2 = (double) (i >> 0 & 255) / 255.0D;

			for (int j = 0; j < pParticleAmount; ++j) {
				this.level().addParticle(ParticleTypes.ENTITY_EFFECT, this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D), d0, d1, d2);
			}

		}
	}

	public int getColor() {
		return this.entityData.get(ID_EFFECT_COLOR);
	}

	private void setFixedColor(int pFixedColor) {
		this.fixedColor = true;
		this.entityData.set(ID_EFFECT_COLOR, pFixedColor);
	}

	@Override
	public void addAdditionalSaveData(CompoundTag pCompound) {
		super.addAdditionalSaveData(pCompound);
		if (this.potion != Potions.EMPTY) {
			pCompound.putString("Potion", BuiltInRegistries.POTION.getKey(this.potion).toString());
		}

		if (this.fixedColor) {
			pCompound.putInt("Color", this.getColor());
		}

		if (!this.effects.isEmpty()) {
			ListTag listtag = new ListTag();

			for (MobEffectInstance mobeffectinstance : this.effects) {
				listtag.add(mobeffectinstance.save(new CompoundTag()));
			}

			pCompound.put("CustomPotionEffects", listtag);
		}

	}

	@Override
	public void readAdditionalSaveData(CompoundTag pCompound) {
		super.readAdditionalSaveData(pCompound);
		if (pCompound.contains("Potion", 8)) {
			this.potion = PotionUtils.getPotion(pCompound);
		}

		for (MobEffectInstance mobeffectinstance : PotionUtils.getCustomEffects(pCompound)) {
			this.addEffect(mobeffectinstance);
		}

		if (pCompound.contains("Color", 99)) {
			this.setFixedColor(pCompound.getInt("Color"));
		} else {
			this.updateColor();
		}

	}

	@Override
	protected void doPostHurtEffects(LivingEntity pLiving) {
		super.doPostHurtEffects(pLiving);
		Entity entity = this.getEffectSource();

		for (MobEffectInstance mobeffectinstance : this.potion.getEffects()) {
			pLiving.addEffect(new MobEffectInstance(mobeffectinstance.getEffect(), Math.max(mobeffectinstance.mapDuration((p_268168_) -> {
				return p_268168_ / 8;
			}), 1), mobeffectinstance.getAmplifier(), mobeffectinstance.isAmbient(), mobeffectinstance.isVisible()), entity);
		}

		if (!this.effects.isEmpty()) {
			for (MobEffectInstance mobeffectinstance1 : this.effects) {
				pLiving.addEffect(mobeffectinstance1, entity);
			}
		}
		if (isSpectralArrow) {
			MobEffectInstance mobeffectinstance = new MobEffectInstance(MobEffects.GLOWING, 200, 0);
			pLiving.addEffect(mobeffectinstance, this.getEffectSource());
		}
	}

	@Override
	protected ItemStack getPickupItem() {
		if (isSpectralArrow) {
			new ItemStack(Items.SPECTRAL_ARROW);
		}
		if (this.effects.isEmpty() && this.potion == Potions.EMPTY) {
			return new ItemStack(Items.ARROW);
		} else {
			ItemStack itemstack = new ItemStack(Items.TIPPED_ARROW);
			PotionUtils.setPotion(itemstack, this.potion);
			PotionUtils.setCustomEffects(itemstack, this.effects);
			if (this.fixedColor) {
				itemstack.getOrCreateTag().putInt("CustomPotionColor", this.getColor());
			}

			return itemstack;
		}
	}

	@Override
	public void handleEntityEvent(byte pId) {
		if (pId == 0) {
			int i = this.getColor();
			if (i != -1) {
				double d0 = (double) (i >> 16 & 255) / 255.0D;
				double d1 = (double) (i >> 8 & 255) / 255.0D;
				double d2 = (double) (i >> 0 & 255) / 255.0D;

				for (int j = 0; j < 20; ++j) {
					this.level().addParticle(ParticleTypes.ENTITY_EFFECT, this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D), d0, d1, d2);
				}
			}
		} else {
			super.handleEntityEvent(pId);
		}
	}
}