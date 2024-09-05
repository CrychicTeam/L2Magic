package dev.xkmc.l2magic.content.item.utility;

import dev.xkmc.l2magic.init.data.LMLangData;
import dev.xkmc.l2magic.init.registrate.LMItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class TargetSelectWand extends BaseWand {

	public TargetSelectWand(Properties properties) {
		super(properties);
	}

	@Override
	public void clickTarget(ItemStack stack, Player player, LivingEntity entity) {
		var id = LMItems.TARGET.get(stack);
		if (id != null) {
			Entity other = entity.level().getEntity(id);
			if (other instanceof LivingEntity le && le != entity) {
				boolean succeed = false;
				if (entity instanceof Mob mob) {
					mob.setTarget(le);
					succeed = true;
				}
				if (le instanceof Mob mob) {
					mob.setTarget(entity);
					succeed = true;
				}
				stack.remove(LMItems.TARGET);
				if (succeed) {
					player.sendSystemMessage(LMLangData.MSG_SET_TARGET.get(entity.getDisplayName(), le.getDisplayName()));
				} else {
					player.sendSystemMessage(LMLangData.MSG_TARGET_FAIL.get(entity.getDisplayName(), le.getDisplayName()));
				}
				return;
			}
		}
		LMItems.TARGET.set(stack, entity.getId());
		player.sendSystemMessage(LMLangData.MSG_TARGET_RECORD.get(entity.getDisplayName()));
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> list, TooltipFlag flag) {
		var level = ctx.level();
		var id = LMItems.TARGET.get(stack);
		if (level != null && id != null) {
			Entity other = level.getEntity(id);
			if (other instanceof LivingEntity le) {
				var name = le.getDisplayName();
				if (name == null) name = le.getType().getDescription();
				list.add(LMLangData.MSG_TARGET_RECORD.get(name.copy().withStyle(ChatFormatting.AQUA)).withStyle(ChatFormatting.GRAY));
			}
		} else {
			list.add(LMLangData.ITEM_WAND_TARGET.get().withStyle(ChatFormatting.GRAY));
		}
	}

}
