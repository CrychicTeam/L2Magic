package dev.xkmc.l2magic.init.data.spell.fire;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.xkmc.l2magic.content.engine.context.DataGenContext;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.iterator.LoopIterator;
import dev.xkmc.l2magic.content.engine.iterator.RingRandomIterator;
import dev.xkmc.l2magic.content.engine.logic.*;
import dev.xkmc.l2magic.content.engine.modifier.ForwardOffsetModifier;
import dev.xkmc.l2magic.content.engine.modifier.RotationModifier;
import dev.xkmc.l2magic.content.engine.modifier.SetDirectionModifier;
import dev.xkmc.l2magic.content.engine.particle.DustParticleInstance;
import dev.xkmc.l2magic.content.engine.processor.DamageProcessor;
import dev.xkmc.l2magic.content.engine.processor.IgniteProcessor;
import dev.xkmc.l2magic.content.engine.selector.ApproxCylinderSelector;
import dev.xkmc.l2magic.content.engine.selector.LinearCubeSelector;
import dev.xkmc.l2magic.content.engine.selector.SelectionType;
import dev.xkmc.l2magic.content.engine.spell.SpellAction;
import dev.xkmc.l2magic.content.engine.spell.SpellCastType;
import dev.xkmc.l2magic.content.engine.spell.SpellTriggerType;
import dev.xkmc.l2magic.content.engine.variable.BooleanVariable;
import dev.xkmc.l2magic.content.engine.variable.ColorVariable;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import dev.xkmc.l2magic.init.data.SpellDataGenEntry;
import dev.xkmc.l2magic.init.data.spell.UnrealHelper;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.item.Items;

import java.util.List;

public class BlazingHammer extends SpellDataGenEntry {

    public static final ResourceKey<SpellAction> BLAZING_HAMMER = spell("blazing_hammer");

    @Override
    public void genLang(RegistrateLangProvider pvd) {
        pvd.add(SpellAction.lang(BLAZING_HAMMER.location()), "Blazing Hammer");
    }

    @Override
    public void register(BootstrapContext<SpellAction> ctx) {
        new SpellAction(
                blazingHammer(new DataGenContext(ctx)),
                Items.LAVA_BUCKET, 3900,
                SpellCastType.INSTANT,
                SpellTriggerType.TARGET_POS
        ).verifyOnBuild(ctx, BLAZING_HAMMER);
    }

    private static ConfiguredEngine<?> blazingHammer(DataGenContext ctx) {
        return new ListLogic(List.of(
                new MoveEngine(
                        List.of(
                                new SetDirectionModifier(
                                        DoubleVariable.ZERO,
                                        DoubleVariable.of("-1"),
                                        DoubleVariable.ZERO
                                ),
                                new ForwardOffsetModifier(
                                        DoubleVariable.of("-5")
                                )
                        ),
                        new ListLogic(List.of(
                                UnrealHelper.cuboidSurface(
                                        2D,
                                        4D,
                                        2D,
                                        0.2D,
                                        new DustParticleInstance(
                                                ColorVariable.Static.of(0xFF0000),
                                                DoubleVariable.of("1"),
                                                DoubleVariable.of("0.2"),
                                                IntVariable.of("30")
                                        )
                                ),
                                new RandomVariableLogic(
                                        "r",
                                        3,
                                        new LoopIterator(
                                                IntVariable.of("20"),
                                                new MoveEngine(
                                                        List.of(
                                                                new SetDirectionModifier(
                                                                        DoubleVariable.of("r0-min(r0,r1)"),
                                                                        DoubleVariable.ZERO,
                                                                        DoubleVariable.of("r1-min(r0,r1)")
                                                                ),
                                                                new ForwardOffsetModifier(
                                                                        DoubleVariable.of("(1+0.2*l)*max(-1,min(1,r2*100000-50000))")
                                                                ),
                                                                new SetDirectionModifier(
                                                                        DoubleVariable.ZERO,
                                                                        DoubleVariable.of("-1"),
                                                                        DoubleVariable.ZERO
                                                                )
                                                        ),
                                                        new DustParticleInstance(
                                                                ColorVariable.Static.of(0xFF0000),
                                                                DoubleVariable.of("1"),
                                                                DoubleVariable.of("0.2"),
                                                                IntVariable.of("30")
                                                        )
                                                ),
                                                "l"
                                        )
                                )
                        ))
                ),
                new DelayLogic(
                        IntVariable.of("20"),
                        new ListLogic(List.of(
                                new RingRandomIterator(
                                        DoubleVariable.of("0"),
                                        DoubleVariable.of("2"),
                                        DoubleVariable.of("0"),
                                        DoubleVariable.of("360"),
                                        IntVariable.of("200"),
                                        new RandomVariableLogic(
                                                "r",
                                                2,
                                                new MoveEngine(
                                                        List.of(
                                                                new RotationModifier(
                                                                        DoubleVariable.ZERO,
                                                                        DoubleVariable.of("45*r0")
                                                                )
                                                        ),
                                                        new DustParticleInstance(
                                                                ColorVariable.Static.of(0xFF0000),
                                                                DoubleVariable.of("1"),
                                                                DoubleVariable.of("0.2+r1"),
                                                                IntVariable.of("40")
                                                        )
                                                )
                                        ),
                                        null
                                ),
                                new ProcessorEngine(
                                        SelectionType.ENEMY,
                                        new ApproxCylinderSelector(
                                                DoubleVariable.of("8"),
                                                DoubleVariable.of("4")
                                        ),
                                        List.of(
                                                new DamageProcessor(ctx.damage(DamageTypes.IN_FIRE),
                                                        DoubleVariable.of("10"), true, true),
                                                new IgniteProcessor(
                                                        List.of(),
                                                        IntVariable.of("200")
                                                )
                                        )
                                )
                        ))
                )
        ));
    }

}
