package dev.xkmc.l2magic.content.item.utility;

import dev.xkmc.l2library.content.raytrace.IGlowingTarget;
import dev.xkmc.l2library.content.raytrace.RayTraceUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public abstract class BaseWand extends Item implements IGlowingTarget, IMobClickItem {

	public BaseWand(Properties properties) {
		super(properties);
	}

	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
		if (level.isClientSide() && selected && entity instanceof Player player) {
			RayTraceUtil.clientUpdateTarget(player, 64);
		}
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (!level.isClientSide()) {
			LivingEntity target = RayTraceUtil.serverGetTarget(player);
			if (target != null) {
				clickTarget(stack, player, target);
			} else {
				clickNothing(stack, player);
			}
		}
		return InteractionResultHolder.success(stack);
	}

	public abstract void clickTarget(ItemStack stack, Player player, LivingEntity entity);

	public void clickNothing(ItemStack stack, Player player) {

	}

	@Override
	public int getDistance(ItemStack stack) {
		return 64;
	}
}
