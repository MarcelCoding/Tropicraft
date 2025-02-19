package net.tropicraft.core.common.dimension.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.feature.structures.JigsawPlacement;
import net.minecraft.world.level.levelgen.feature.structures.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.tropicraft.Constants;
import net.tropicraft.core.common.dimension.feature.jigsaw.piece.NoRotateSingleJigsawPiece;
import net.tropicraft.core.common.dimension.feature.jigsaw.piece.PieceWithGenerationBounds;

public class HomeTreeStructure extends StructureFeature<JigsawConfiguration> {
    public HomeTreeStructure(Codec<JigsawConfiguration> codec) {
        super(codec);
    }

    @Override
    protected boolean isFeatureChunk(ChunkGenerator generator, BiomeSource biomes, long seed, WorldgenRandom random, ChunkPos chunkPos, Biome biome, ChunkPos potentialPos, JigsawConfiguration config, LevelHeightAccessor level) {
        BlockPos pos = new BlockPos((chunkPos.x << 4) + 8, 0, (chunkPos.z << 4) + 8);
        int centerY = generator.getBaseHeight(pos.getX(), pos.getZ(), Heightmap.Types.WORLD_SURFACE_WG, level);
        return isValid(generator, pos.offset(-4, 0, -4), centerY, level) &&
                isValid(generator, pos.offset(-4, 0, 4), centerY, level) &&
                isValid(generator, pos.offset(4, 0, 4), centerY, level) &&
                isValid(generator, pos.offset(4, 0, -4), centerY, level);
    }

    private boolean isValid(ChunkGenerator generator, BlockPos pos, int startY, final LevelHeightAccessor level) {
        int y = generator.getBaseHeight(pos.getX(), pos.getZ(), Heightmap.Types.WORLD_SURFACE_WG, level);
        return y >= generator.getSeaLevel()
                && Math.abs(y - startY) < 10
                && y < 150
                && y > generator.getSeaLevel() + 2;
    }

    @Override
    public StructureStartFactory<JigsawConfiguration> getStartFactory() {
        return Start::new;
    }

    private static final StructurePieceType TYPE = StructurePieceType.setPieceId(Piece::new, Constants.MODID + ":home_tree");

    public static class Start extends StructureStart<JigsawConfiguration> {
        public Start(StructureFeature<JigsawConfiguration> structure, ChunkPos pos, int references, long seed) {
            super(structure, pos, references, seed);
        }

        @Override
        public void generatePieces(RegistryAccess registries, ChunkGenerator generator, StructureManager templates, ChunkPos chunkPos, Biome biome, JigsawConfiguration config, LevelHeightAccessor pLevel) {
            BlockPos pos = chunkPos.getWorldPosition();
            JigsawPlacement.addPieces(registries, config, Piece::new, generator, templates, pos, this, this.random, true, true, pLevel);
        }
    }

    public static class Piece extends PoolElementStructurePiece {
        public Piece(StructureManager templates, StructurePoolElement piece, BlockPos pos, int groundLevelDelta, Rotation rotation, BoundingBox bounds) {
            super(templates, piece, pos, groundLevelDelta, rotation, bounds);
            this.boundingBox = this.fixGenerationBoundingBox(templates);
        }

        public Piece(ServerLevel level, CompoundTag data) {
            super(level, data);
            this.boundingBox = this.fixGenerationBoundingBox(level.getStructureManager());
        }

        private BoundingBox fixGenerationBoundingBox(StructureManager templates) {
            if (this.element instanceof PieceWithGenerationBounds piece) {
                return piece.getGenerationBounds(templates, this.position, Rotation.NONE);
            } else {
                return boundingBox;
            }
        }

        @Override
        public Rotation getRotation() {
            if (this.element instanceof NoRotateSingleJigsawPiece) {
                return Rotation.NONE;
            }
            return super.getRotation();
        }

        @Override
        public StructurePieceType getType() {
            return TYPE;
        }
    }
}
