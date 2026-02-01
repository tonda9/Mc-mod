# Create Cannons nefunguje zatim

A Create addon for Minecraft 1.21.1 NeoForge that adds large mechanical cannons powered by kinetic stress.

Inspired by Create Big Cannons.

## Features

- **Kinetic Cannon Block**: The main firing mechanism that consumes Create's stress units
- **Cannon Barrel Block**: Extend your cannon's reach and improve projectile velocity
- **Cannon Breech Block**: The loading mechanism for ammunition
- **Multiple Projectile Types**:
  - Iron Cannonball - Standard ammunition
  - Steel Cannonball - Higher damage and explosion radius
  - Explosive Shell - Maximum area damage

## Requirements

- Minecraft 1.21.1
- NeoForge 21.1.x
- Create 6.x for NeoForge

## Building

```bash
./gradlew build
```

The compiled JAR will be in `build/libs/`.

## Development Setup

1. Clone the repository
2. Import into your IDE as a Gradle project
3. Run `./gradlew genIntellijRuns` (IntelliJ) or `./gradlew genEclipseRuns` (Eclipse)
4. Run the generated run configurations

## Usage

1. Craft a Kinetic Cannon and place it
2. Add Cannon Barrels in front for increased velocity (optional, up to 5)
3. Connect to a Create kinetic network or power with redstone
4. Load a cannonball by right-clicking with the item
5. Load a Gunpowder Charge by right-clicking with it
6. Right-click with empty hand to fire!

## Stress Impact

- Kinetic Cannon: 256 SU base
- Per Barrel Extension: +32 SU
- Firing multiplier: 2x (temporary burst)

## Recipes

All recipes are craftable using standard materials:
- Iron ingots
- Clay (for cannon casting)
- Gunpowder (for charges)
- Paper (for charge wrapping)

## License

MIT License

## Credits

- Inspired by Create Big Cannons
- Built with NeoForge and Create mod APIs
