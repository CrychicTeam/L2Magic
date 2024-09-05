package dev.xkmc.l2magic.content.item.utility;

import dev.xkmc.l2magic.content.menu.curios.EntityCuriosMenuPvd;
import dev.xkmc.l2magic.content.menu.equipments.EquipmentsMenuPvd;
import dev.xkmc.l2magic.init.data.LMLangData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class EquipmentWand extends BaseWand {

	public EquipmentWand(Properties properties) {
		super(properties);
	}

	@Override
	public void clickTarget(ItemStack stack, Player player, LivingEntity entity) {
		if (entity instanceof Mob mob) {
			if (player.isShiftKeyDown()) {
				new EntityCuriosMenuPvd(mob, 0).open((ServerPlayer) player);
			} else {
				new EquipmentsMenuPvd(mob).open((ServerPlayer) player);
			}
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext level, List<Component> list, TooltipFlag flag) {
		list.add(LMLangData.ITEM_WAND_EQUIPMENT.get().withStyle(ChatFormatting.GRAY));
	}

}
