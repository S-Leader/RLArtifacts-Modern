package artifacts.registry;

import artifacts.Artifacts;
import artifacts.mixins.gamerule.BooleanValueInvoker;
import artifacts.mixins.gamerule.IntegerValueInvoker;
import artifacts.network.BooleanGameRuleChangedPacket;
import artifacts.network.IntegerGameRuleChangedPacket;
import artifacts.network.NetworkHandler;
import com.google.common.base.CaseFormat;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.GameRules;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ModGameRules {

    private static final Map<String, BooleanValue> BOOLEAN_VALUES = new HashMap<>();
    private static final Map<String, IntegerValue> INTEGER_VALUES = new HashMap<>();

    public static final BooleanValue
            ANTIDOTE_VESSEL_ENABLED = booleanValue(createName(ModItems.ANTIDOTE_VESSEL, "enabled")),
            CLOUD_IN_A_BOTTLE_ENABLED = booleanValue(createName(ModItems.CLOUD_IN_A_BOTTLE, "enabled")),
            FLAME_PENDANT_DO_GRANT_FIRE_RESISTANCE = booleanValue(createName(ModItems.FLAME_PENDANT, "doGrantFireResistance")),
            SHOCK_PENDANT_DO_CANCEL_LIGHTNING_DAMAGE = booleanValue(createName(ModItems.SHOCK_PENDANT, "doCancelLightningDamage")),
            STAR_CLOAK_ALLOW_INDIRECT_ATTACKS = booleanValue(createName(ModItems.STAR_CLOAK, "allowIndirectAttacks")),
            UMBRELLA_IS_SHIELD = booleanValue(createName(ModItems.UMBRELLA, "isShield")),
            UMBRELLA_IS_GLIDER = booleanValue(createName(ModItems.UMBRELLA, "isGlider"));

    public static final IntegerValue
            POWER_GLOVE_ATTACK_DAMAGE_BONUS = integerValue(createName(ModItems.POWER_GLOVE, "attackDamageBonus"), 4),
            SACRIFICIAL_AMULET_REQUIRED_KILLS = integerValue(createName(ModItems.SACRIFICIAL_AMULET, "requiredKills"), 99, 10000),
            THORN_PENDANT_MAX_DAMAGE = integerValue(createName(ModItems.THORN_PENDANT, "maxDamage"), 6),
            THORN_PENDANT_MIN_DAMAGE = integerValue(createName(ModItems.THORN_PENDANT, "minDamage"), 2),
            VAMPIRIC_GLOVE_MAX_HEALING_PER_HIT = integerValue(createName(ModItems.VAMPIRIC_GLOVE, "maxHealingPerHit"), 6),
            LUCKY_CLOVER_LUCK_BONUS = integerValue(createName(ModItems.LUCKY_CLOVER, "luckBonus"), 2, 100),
            STAR_CLOAK_COOLDOWN = integerValue(createName(ModItems.STAR_CLOAK, "cooldownTicks"), 20, 60 * 60 * 20),
            STAR_CLOAK_MIN_STARS = integerValue(createName(ModItems.STAR_CLOAK, "minimumStars"), 2, 64),
            STAR_CLOAK_MAX_STARS = integerValue(createName(ModItems.STAR_CLOAK, "maximumStars"), 6, 64),
            STAR_CLOAK_DAMAGE = integerValue(createName(ModItems.STAR_CLOAK, "starDamage"), 8, 100),

    ANTIDOTE_VESSEL_MAX_EFFECT_DURATION = durationSeconds(createName(ModItems.ANTIDOTE_VESSEL, "maxEffectDuration"), 5),
            FIRE_GAUNTLET_FIRE_DURATION = durationSeconds(createName(ModItems.FIRE_GAUNTLET, "fireDuration"), 8),
            FLAME_PENDANT_COOLDOWN = durationSeconds(createName(ModItems.FLAME_PENDANT, "cooldown"), 0),
            FLAME_PENDANT_FIRE_DURATION = durationSeconds(createName(ModItems.FLAME_PENDANT, "fireDuration"), 10),
            MAGMA_STONE_FIRE_DURATION = durationSeconds(createName(ModItems.MAGMA_STONE, "fireDuration"), 4),
            PANIC_NECKLACE_COOLDOWN = durationSeconds(createName(ModItems.PANIC_NECKLACE, "cooldown"), 0),
            PANIC_NECKLACE_SPEED_DURATION = durationSeconds(createName(ModItems.PANIC_NECKLACE, "speedDuration"), 8),
            SHOCK_PENDANT_COOLDOWN = durationSeconds(createName(ModItems.SHOCK_PENDANT, "cooldown"), 0),
            THORN_PENDANT_COOLDOWN = durationSeconds(createName(ModItems.THORN_PENDANT, "cooldown"), 0),

    PANIC_NECKLACE_SPEED_LEVEL = mobEffectLevel(createName(ModItems.PANIC_NECKLACE, "speedLevel"), 1);

    public static final DoubleValue
            CLOUD_IN_A_BOTTLE_SPRINT_JUMP_VERTICAL_VELOCITY = doubleValue(createName(ModItems.CLOUD_IN_A_BOTTLE, "sprintJumpVerticalVelocity"), 50, 100 * 100, 100),
            CLOUD_IN_A_BOTTLE_SPRINT_JUMP_HORIZONTAL_VELOCITY = doubleValue(createName(ModItems.CLOUD_IN_A_BOTTLE, "sprintJumpHorizontalVelocity"), 50, 100 * 100, 100),
            FERAL_CLAWS_ATTACK_SPEED_BONUS = percentage(createName(ModItems.FERAL_CLAWS, "attackSpeedBonus"), 40),
            FLAME_PENDANT_STRIKE_CHANCE = percentage(createName(ModItems.FLAME_PENDANT, "strikeChance"), 40),
            PLASTIC_DRINKING_HAT_DRINKING_DURATION_MULTIPLIER = percentage(createName(ModItems.PLASTIC_DRINKING_HAT, "drinkingDurationMultiplier"), 30),
            PLASTIC_DRINKING_HAT_EATING_DURATION_MULTIPLIER = percentage(createName(ModItems.PLASTIC_DRINKING_HAT, "eatingDurationMultiplier"), 60),
            POCKET_PISTON_KNOCKBACK_STRENGTH = doubleValue(createName(ModItems.POCKET_PISTON, "knockbackStrength"), 15, 10),
            SHOCK_PENDANT_STRIKE_CHANCE = percentage(createName(ModItems.SHOCK_PENDANT, "strikeChance"), 25),
            THORN_PENDANT_STRIKE_CHANCE = percentage(createName(ModItems.THORN_PENDANT, "strikeChance"), 50),
            VAMPIRIC_GLOVE_ABSORPTION_CHANCE = percentage(createName(ModItems.VAMPIRIC_GLOVE, "absorptionChance"), 100),
            VAMPIRIC_GLOVE_ABSORPTION_RATIO = doubleValue(createName(ModItems.VAMPIRIC_GLOVE, "absorptionRatio"), 20, 100),
            WHOOPEE_CUSHION_FART_CHANCE = percentage(createName(ModItems.WHOOPEE_CUSHION, "fartChance"), 12);

    private static String createName(RegistrySupplier<? extends Item> item, String name) {
        return String.format("%s.%s.%s",
                Artifacts.MOD_ID,
                CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, item.getId().getPath()),
                name
        );
    }

    private static BooleanValue booleanValue(String name) {
        return booleanValue(name, true);
    }

    private static BooleanValue booleanValue(String name, boolean defaultValue) {
        BooleanValue result = new BooleanValue();
        result.update(defaultValue);
        GameRules.Type<GameRules.BooleanValue> type = BooleanValueInvoker.invokeCreate(defaultValue, (server, value) -> {
            result.update(value.get());
            NetworkHandler.CHANNEL.sendToPlayers(server.getPlayerList().getPlayers(), new BooleanGameRuleChangedPacket(name, value.get()));
        });
        result.key = GameRules.register(name, GameRules.Category.PLAYER, type);
        BOOLEAN_VALUES.put(name, result);
        return result;
    }

    private static IntegerValue integerValue(String name, int defaultValue) {
        return integerValue(name, defaultValue, Integer.MAX_VALUE);
    }

    private static IntegerValue integerValue(String name, int defaultValue, int maxValue) {
        return integerValue(name, defaultValue, maxValue, 1);
    }

    private static IntegerValue integerValue(String name, int defaultValue, int maxValue, int multiplier) {
        IntegerValue result = new IntegerValue(defaultValue, maxValue, multiplier);
        result.update(defaultValue);
        GameRules.Type<GameRules.IntegerValue> type = IntegerValueInvoker.invokeCreate(defaultValue, (server, value) -> {
            result.update(value.get());
            NetworkHandler.CHANNEL.sendToPlayers(server.getPlayerList().getPlayers(), new IntegerGameRuleChangedPacket(name, value.get()));
        });
        result.key = GameRules.register(name, GameRules.Category.PLAYER, type);

        INTEGER_VALUES.put(name, result);
        return result;
    }

    private static IntegerValue durationSeconds(String name, int defaultValue) {
        return integerValue(name, defaultValue, 20 * 60 * 60, 20);
    }

    private static IntegerValue mobEffectLevel(String name, int defaultValue) {
        return integerValue(name, defaultValue, 128);
    }

    private static DoubleValue doubleValue(String name, int defaultValue, int maxValue, double factor) {
        return new DoubleValue(integerValue(name, defaultValue, maxValue), factor);
    }

    private static DoubleValue doubleValue(String name, int defaultValue, int factor) {
        return doubleValue(name, defaultValue, Integer.MAX_VALUE, factor);
    }

    private static DoubleValue percentage(String name, int defaultValue) {
        return doubleValue(name, defaultValue, 100, 100);
    }

    public static void updateValue(String key, boolean value) {
        BOOLEAN_VALUES.get(key).update(value);
    }

    public static void updateValue(String key, int value) {
        INTEGER_VALUES.get(key).update(value);
    }

    public static void onPlayerJoinLevel(ServerPlayer player) {
        BOOLEAN_VALUES.forEach((key, value) -> NetworkHandler.CHANNEL.sendToPlayer(player, new BooleanGameRuleChangedPacket(key, value.value)));
        INTEGER_VALUES.forEach((key, value) -> NetworkHandler.CHANNEL.sendToPlayer(player, new IntegerGameRuleChangedPacket(key, value.value)));
    }

    public static void onServerStarted(MinecraftServer server) {
        BOOLEAN_VALUES.values().forEach(value -> value.update(server));
        INTEGER_VALUES.values().forEach(value -> value.update(server));
    }

    public static class BooleanValue implements Supplier<Boolean> {

        private Boolean value = true;
        private GameRules.Key<GameRules.BooleanValue> key;

        @Override
        public Boolean get() {
            return value;
        }

        private void update(MinecraftServer server) {
            update(server.getGameRules().getBoolean(key));
        }

        private void update(boolean value) {
            this.value = value;
        }
    }

    public static class IntegerValue implements Supplier<Integer> {

        private final int max;
        private final int multiplier;
        private int value;
        private GameRules.Key<GameRules.IntegerValue> key;

        private IntegerValue(int defaultValue, int max, int multiplier) {
            this.value = defaultValue;
            this.max = max;
            this.multiplier = multiplier;
        }

        @Override
        public Integer get() {
            return Math.min(max, Math.max(0, value)) * multiplier;
        }

        private void update(MinecraftServer server) {
            update(server.getGameRules().getInt(key));
        }

        private void update(int value) {
            this.value = value;
        }
    }

    public record DoubleValue(IntegerValue integerValue, double factor) implements Supplier<Double> {

        @Override
        public Double get() {
            return integerValue.get() / factor;
        }

        public boolean fuzzyEquals(double a) {
            return Math.abs(get() - a) < 1e-10;
        }
    }
}
