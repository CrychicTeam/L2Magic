package dev.xkmc.l2magic.init.data;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.xkmc.l2magic.content.engine.spell.SpellCastType;
import dev.xkmc.l2magic.content.engine.spell.SpellTriggerType;
import dev.xkmc.l2magic.init.L2Magic;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.Locale;

public enum LMLangData {
	CMD_INVALID_SPELL("command.invalid_spell", "Invalid spell ID %s", 1),
	CMD_WRONG_TYPE("command.wrong_type", "Spell %s expects type %s", 2),
	CMD_SUCCESS("command.success", "Spell %s is executed successfully", 1),
	CMD_SUCCESS_COUNT("command.success_count", "Spell %s is executed by %s entities successfully", 2),
	CMD_FAIL("command.fail", "Spell %s failed to execute", 1),
	SPELL_CAST_TYPE("spell_cast_type", "Cast Type: %s", 1),
	SPELL_TRIGGER_TYPE("spell_trigger_type", "Orientation: %s", 1),
	;

	final String id, def;
	final int count;

	LMLangData(String id, String def, int count) {
		this.id = id;
		this.def = def;
		this.count = count;
	}

	public MutableComponent get(Object... objs) {
		if (objs.length != count)
			throw new IllegalArgumentException("for " + name() + ": expect " + count + " parameters, got " + objs.length);
		return translate(L2Magic.MODID + "." + id, objs);
	}

	public static MutableComponent lang(SpellCastType type) {
		return SPELL_CAST_TYPE.get(Component.translatable(L2Magic.MODID + ".spell_cast_type." +
				type.name().toLowerCase(Locale.ROOT)).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.YELLOW);
	}

	public static MutableComponent lang(SpellTriggerType type) {
		return SPELL_TRIGGER_TYPE.get(Component.translatable(L2Magic.MODID + ".spell_trigger_type." +
				type.name().toLowerCase(Locale.ROOT)).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.YELLOW);
	}

	public static void genLang(RegistrateLangProvider pvd) {
		for (var e : values())
			pvd.add(L2Magic.MODID + "." + e.id, e.def);
		for (var e : SpellDataGenRegistry.LIST) e.genLang(pvd);
		for (var e : SpellCastType.values())
			pvd.add(L2Magic.MODID + ".spell_cast_type." + e.name().toLowerCase(Locale.ROOT), e.defDesc());
		for (var e : SpellTriggerType.values())
			pvd.add(L2Magic.MODID + ".spell_trigger_type." + e.name().toLowerCase(Locale.ROOT), e.defDesc());
	}

	public static MutableComponent translate(String key, Object... objs) {
		return Component.translatable(key, objs);
	}

}
