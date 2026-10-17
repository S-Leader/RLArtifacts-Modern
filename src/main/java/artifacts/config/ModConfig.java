package artifacts.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import org.apache.commons.lang3.tuple.Pair;

public final class ModConfig {

    public static final ForgeConfigSpec COMMON_SPEC;
    public static final ForgeConfigSpec CLIENT_SPEC;

    public static final Common common;
    public static final Client client;

    static {
        Pair<Common, ForgeConfigSpec> commonPair =
                new ForgeConfigSpec.Builder().configure(Common::new);
        common = commonPair.getLeft();
        COMMON_SPEC = commonPair.getRight();

        Pair<Client, ForgeConfigSpec> clientPair =
                new ForgeConfigSpec.Builder().configure(Client::new);
        client = clientPair.getLeft();
        CLIENT_SPEC = clientPair.getRight();
    }

    private ModConfig() {
    }

    public static void register() {
        ModLoadingContext.get().registerConfig(
                net.minecraftforge.fml.config.ModConfig.Type.COMMON,
                COMMON_SPEC
        );

        ModLoadingContext.get().registerConfig(
                net.minecraftforge.fml.config.ModConfig.Type.CLIENT,
                CLIENT_SPEC
        );
    }

    public static final class Common {

        public final ForgeConfigSpec.DoubleValue artifactRarity;
        public final ForgeConfigSpec.DoubleValue entityEquipmentChance;
        public final ForgeConfigSpec.DoubleValue archaeologyChance;
        public final ForgeConfigSpec.DoubleValue unlootedChestMimicChance;
        public final ForgeConfigSpec.BooleanValue modifyHurtSounds;

        public final Campsite campsite;

        Common(ForgeConfigSpec.Builder builder) {
            builder.push("common");

            artifactRarity = builder
                    .comment(
                            "Affects how common artifacts are in chests.",
                            "Values above 1 make artifacts rarer.",
                            "Values between 0 and 1 make artifacts more common.",
                            "Doubling this value makes artifacts approximately twice as hard to find.",
                            "Set this to 10000 to prevent artifacts from appearing as chest loot.",
                            "",
                            "Specific item effects can be changed using gamerules.",
                            "https://github.com/ochotonida/artifacts/wiki"
                    )
                    .defineInRange(
                            "artifactRarity",
                            1.0D,
                            0.0D,
                            Double.MAX_VALUE
                    );

            entityEquipmentChance = builder
                    .comment(
                            "The chance that a skeleton, zombie or piglin",
                            "spawns with an artifact equipped."
                    )
                    .defineInRange(
                            "entityEquipmentChance",
                            1.0D / 1000.0D,
                            0.0D,
                            1.0D
                    );

            archaeologyChance = builder
                    .comment(
                            "The chance that an artifact generates",
                            "in suspicious sand or gravel."
                    )
                    .defineInRange(
                            "archaeologyChance",
                            1.0D / 16.0D,
                            0.0D,
                            1.0D
                    );

            unlootedChestMimicChance = builder
                    .comment(
                            "Chance for an unlooted vanilla chest in the Overworld",
                            "to be replaced by a Mimic when first opened or broken."
                    )
                    .defineInRange(
                            "unlootedChestMimicChance",
                            0.05D,
                            0.0D,
                            1.0D
                    );

            modifyHurtSounds = builder
                    .comment(
                            "Whether the Kitty Slippers and Bunny Hoppers",
                            "change the player's hurt sounds."
                    )
                    .define("modifyHurtSounds", true);

            campsite = new Campsite(builder);

            builder.pop();
        }

        public double getArtifactRarity() {
            return Math.max(0.0D, artifactRarity.get());
        }

        public double getUnlootedChestMimicChance() {
            return Math.max(
                    0.0D,
                    Math.min(1.0D, unlootedChestMimicChance.get())
            );
        }

        public static final class Campsite {

            public final ForgeConfigSpec.IntValue count;
            public final ForgeConfigSpec.IntValue minY;
            public final ForgeConfigSpec.IntValue maxY;
            public final ForgeConfigSpec.BooleanValue useModdedChests;
            public final ForgeConfigSpec.BooleanValue allowLightSources;

            Campsite(ForgeConfigSpec.Builder builder) {
                builder.push("campsite");

                count = builder
                        .comment(
                                "How many times a campsite will attempt",
                                "to generate per chunk.",
                                "Set to 0 to disable campsite generation."
                        )
                        .defineInRange(
                                "count",
                                4,
                                0,
                                Integer.MAX_VALUE
                        );

                minY = builder
                        .comment(
                                "The minimum height campsites can spawn at."
                        )
                        .defineInRange("minY", -60, Integer.MIN_VALUE, Integer.MAX_VALUE);

                maxY = builder
                        .comment(
                                "The maximum height campsites can spawn at."
                        )
                        .defineInRange("maxY", 40, Integer.MIN_VALUE, Integer.MAX_VALUE);

                useModdedChests = builder
                        .comment(
                                "Whether to use wooden chests from other mods",
                                "when generating campsites."
                        )
                        .define("useModdedChests", true);

                allowLightSources = builder
                        .comment(
                                "Whether campsites can contain blocks that emit light."
                        )
                        .define("allowLightSources", true);

                builder.pop();
            }

            public int getCount() {
                return Math.max(0, count.get());
            }
        }
    }

    public static final class Client {

        public final ForgeConfigSpec.BooleanValue showFirstPersonGloves;
        public final ForgeConfigSpec.BooleanValue showTooltips;
        public final ForgeConfigSpec.BooleanValue useModdedMimicTextures;
        public final ForgeConfigSpec.BooleanValue enableCooldownOverlay;
        public final ForgeConfigSpec.IntValue cooldownOverlayOffset;

        Client(ForgeConfigSpec.Builder builder) {
            builder.push("client");

            showFirstPersonGloves = builder
                    .comment(
                            "Whether glove models should be shown in first person."
                    )
                    .define("showFirstPersonGloves", true);

            showTooltips = builder
                    .comment(
                            "Whether artifacts should have tooltips",
                            "explaining their effects."
                    )
                    .define("showTooltips", true);

            useModdedMimicTextures = builder
                    .comment(
                            "Whether mimics can use textures from Lootr or Quark."
                    )
                    .define("useModdedMimicTextures", true);

            enableCooldownOverlay = builder
                    .comment(
                            "Display artifacts on cooldown next to the hotbar."
                    )
                    .define("enableCooldownOverlay", false);

            cooldownOverlayOffset = builder
                    .comment(
                            "Location of the artifact cooldown GUI element.",
                            "Distance from the hotbar measured in pixels.",
                            "Negative values place the element left of the hotbar."
                    )
                    .defineInRange("cooldownOverlayOffset", 10, -255, 255);

            builder.pop();
        }
    }
}
