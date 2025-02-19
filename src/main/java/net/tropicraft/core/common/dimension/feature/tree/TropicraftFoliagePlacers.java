package net.tropicraft.core.common.dimension.feature.tree;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.tropicraft.Constants;
import net.tropicraft.core.common.dimension.feature.tree.mangrove.MangroveFoliagePlacer;
import net.tropicraft.core.common.dimension.feature.tree.mangrove.SmallMangroveFoliagePlacer;

public final class TropicraftFoliagePlacers {
    public static final DeferredRegister<FoliagePlacerType<?>> FOLIAGE_PLACERS = DeferredRegister.create(ForgeRegistries.FOLIAGE_PLACER_TYPES, Constants.MODID);
    public static RegistryObject<FoliagePlacerType<MangroveFoliagePlacer>> MANGROVE = register("mangrove", MangroveFoliagePlacer.CODEC);
    public static RegistryObject<FoliagePlacerType<SmallMangroveFoliagePlacer>> SMALL_MANGROVE = register("small_mangrove", SmallMangroveFoliagePlacer.CODEC);
    public static RegistryObject<FoliagePlacerType<CitrusFoliagePlacer>> CITRUS = register("citrus", CitrusFoliagePlacer.CODEC);
    public static RegistryObject<FoliagePlacerType<PleodendronFoliagePlacer>> PLEODENDRON = register("pleodendron", PleodendronFoliagePlacer.CODEC);
    public static RegistryObject<FoliagePlacerType<PapayaFoliagePlacer>> PAPAYA = register("papaya", PapayaFoliagePlacer.CODEC);

    private static <T extends FoliagePlacer> RegistryObject<FoliagePlacerType<T>> register(String name, Codec<T> codec) {
        return FOLIAGE_PLACERS.register(name, () -> new FoliagePlacerType<>(codec));
    }
}
