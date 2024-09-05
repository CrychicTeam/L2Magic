package dev.xkmc.l2magic.content.menu.curios;

import dev.xkmc.l2core.util.Proxy;
import dev.xkmc.l2tabs.compat.api.AccessoriesMultiplex;
import dev.xkmc.l2tabs.compat.api.IAccessoriesWrapper;
import dev.xkmc.l2tabs.compat.common.BaseCuriosListMenu;
import dev.xkmc.l2tabs.compat.common.CuriosMenuPvd;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.Nullable;

public class EntityCuriosListMenu extends BaseCuriosListMenu<EntityCuriosListMenu> {

	@Nullable
	public static EntityCuriosListMenu fromNetwork(MenuType<EntityCuriosListMenu> type, int wid, Inventory plInv, FriendlyByteBuf buf) {
		int id = buf.readInt();
		int page = buf.readInt();
		var level = Proxy.getLevel();
		assert level != null;
		Entity entity = level.getEntity(id);
		if (entity instanceof LivingEntity le) {
			var wrapper = AccessoriesMultiplex.get().wrap(le, page);
			return new EntityCuriosListMenu(type, wid, plInv, wrapper);
		}
		return null;
	}

	protected EntityCuriosListMenu(MenuType<? extends EntityCuriosListMenu> type, int wid, Inventory plInv, IAccessoriesWrapper curios) {
		super(type, wid, plInv, curios);
	}

	public void switchPage(ServerPlayer sp, int page) {
		(new CuriosMenuPvd(AccessoriesMultiplex.MT_CURIOS.get(), page)).open(sp);//TODO
	}

}
