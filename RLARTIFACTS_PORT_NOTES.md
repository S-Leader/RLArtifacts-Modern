# Artifacts RLArtifacts Edition (Forge 1.20.1)

This source is based on Artifacts 9.5.19 and adapts its accessory selection to RLArtifacts 1.12.2.

## Retained accessories

- Plastic Drinking Hat (the modern equivalent of Drinking Hat)
- Snorkel
- Night Vision Goggles
- Panic Necklace
- Shock Pendant
- Flame Pendant
- Thorn Pendant
- Cloud in a Bottle (the modern equivalent of Bottled Cloud)
- Obsidian Skull
- Antidote Vessel
- Whoopee Cushion
- Feral Claws
- Power Glove
- Fire Gauntlet
- Pocket Piston
- Vampiric Glove (explicit exception requested for the modern-only item)

## Ported from RLArtifacts 1.12.2

- Bubble Wrap: belt slot, prevents elytra collision damage and retains its pop sound/visual model.
- Lucky Clover: charm slot, grants +2 Luck by default.
- Magma Stone: ring slot, ignites melee targets for 4 seconds by default and renders the old Fire Gauntlet glow layer.
- Star Cloak: body slot, summons 2-6 falling Hallow Stars after damage; 20-tick cooldown and 8 damage per star by default.

The four ported items are included in the Artifacts loot pool. Their settings use the mod's synchronized gamerule system.

## Removed modern-only accessories

Novelty Drinking Hat, Villager Hat, Superstitious Hat, Cowboy Hat, Angler's Hat, Lucky Scarf, Scarf of Invisibility, Cross Necklace, Charm of Sinking, Universal Attractor, Crystal Heart, Helium Flamingo, Chorus Totem, Digging Claws, Golden Hook, Onion Ring, Pickaxe Heater, Aqua Dashers, Bunny Hoppers, Kitty Slippers, Running Shoes, Snowshoes, Steadfast Spikes, Flippers, and Rooted Boots.

Umbrella, Everlasting Beef, Eternal Steak, and the Mimic Spawn Egg remain because they are not accessories.

## Target and dependencies

- Minecraft 1.20.1
- Forge 47.x
- Architectury API
- Cloth Config
- Curios API
- ExpandAbility and MixinExtras are nested in the built Forge JAR.
