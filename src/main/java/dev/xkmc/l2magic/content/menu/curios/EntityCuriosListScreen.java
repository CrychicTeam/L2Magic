package dev.xkmc.l2magic.content.menu.curios;

import dev.xkmc.l2tabs.compat.common.BaseCuriosListScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class EntityCuriosListScreen extends BaseCuriosListScreen<EntityCuriosListMenu> {

	public EntityCuriosListScreen(EntityCuriosListMenu cont, Inventory plInv, Component title) {
		super(cont, plInv, title);
	}

}
