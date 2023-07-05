# Gamma Shifter
A client-side FabricMC mod that restores the ability to set the gamma (brightness) setting beyond 100% in Minecraft
1.19+ and adds other customizable quality-of-life features.

<span style="color: red;">IMPORTANT: </span>
The `main` branch on GitHub contains an obsolete Minecraft 1.19 version of the mod. For the most recent mod version,
see the branch corresponding to the latest major release of Minecraft.

Gamma Shifter is no longer in active development and I do not plan to continue adding features to or working on this mod
except to fix bugs and update to new Minecraft versions.

## Features
-Gamma settings can be changed with hotkeys without pausing the game<br>
-Auto Night Mode to automatically toggle FullBright at night <br>
-Customizable mod properties through ModMenu/Cloth Config API implementation <br>
-Adds hotkeys to set 100% and 2000% brightness instantly <br>
-Adds custom presets <br>

For a full list of features and keybinds, see [Modrinth](https://www.mondrinth.com/mod/gamma_shifter).

## Quick Start
### Download Pre-Built
To download a pre-built version of the mod, see the [Modrinth downloads page](https://www.modrinth.com/mod/gamma_shifter/versions).

### Build Single Version from Source
Make sure `fabric.mod.json` and `gradle.properties` have proper dependency versions for the MC version you want to build for. <br>
For updated dependency versions, see `Build Dependencies` below.
<br>

To build, run: <br>

    ./gradlew build

Gradle will create `./build/` if it does not exist. Artifacts will be generated in `./build/libs`.

### Build All Versions from Source
If you wish to build a `.jar` for each supported mod version, then run: <br>

    ./buildAllVersions.jar

When prompted, enter the new mod version (not MC version) you wish to build. All mod versions will be built: this may
take moment, especially if you are building the mod for the first time. A popup will appear when building finishes.

Gradle will create `./build/` if it does not exist. Artifacts will be generated in `./build/libs`.

## Build Dependencies
[Fabric / Fabric API](https://fabricmc.net/develop) <br>
[ModMenu](https://modrinth.com/mod/modmenu/versions) <br>
[Cloth Config API](https://modrinth.com/mod/cloth-config/versions) <br>

*Fabric/Fabric API should be used to build, but the mod made be used with Quilt Loader/Quilted Standard Library* <br>
*ModMenu and Cloth Config API are required to build, but the mod may be used without them.*