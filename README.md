# Gamma Shifter 1.0.1
A client-side FabricMC mod that restores the ability to set the gamma (brightness) setting beyond 100% in Minecraft 1.19+ and adds other customizable quality-of-life features.

## Features
-Gamma settings can be changed with hotkeys with pausing the game<br>
-Auto Night Mode to automatically toggle FullBright at night <br>
-Customizable mod properties through ModMenu/Cloth Config API implementation <br>
-Adds hotkeys to set 100% and 2000% brightness instantly <br>
-Adds custom presets <br>

### Default Keybinds

<details>
    <summary>Expand Keybinds</summary>

***Decrease brightness (25% by default)***<br>
Keybind: `-` <br>

***Increase brightness (25% by default)***<br>
Keybind: `=`<br>

***Set vanilla max brightness (100%)***<br>
Keybind: `[` <br>

***Set max brightness (2000%)***<br>
Keybind: `]` <br>

***Toggle effects***<br>
Keybind: `G` <br>

***Preset 1 (500% by default)***<br>
Keybind: `;`<br>

***Preset 2 (1000% by default)***<br>
Keybind: `'unbound'`<br>

***Show current gamma***<br>
Keybind: `m`<br>

</details>


## Menu/Config settings
Menu settings are available through ModMenu (see Dependencies below).<br>

<details>
    <summary>Expand Settings</summary>

### General
***Enable mod effects*** <br>
Toggles mod effects on/off<br><br>
***Custom gamma (%)*** <br>
Sets the current gamma<br><br>
***Gamma change per input (%)*** <br>
Sets the amount that the gamma value changes by when the increase/decrease gamma key is pressed<br><br>
***Always start enabled*** <br>
Whether the mod will always start enabled or if it starts in the state the game previously closed in<br><br>
***Snap to change per input*** <br>
Whether gamma changes will snap to the nearest multiple of the current change-per-input<br><br>
***Always save custom gamma*** <br>
Whether the custom gamma (>=100%) will always be saved to options.txt, regardless of if the mod is enabled<br><br>
***Enforce maximum/minimum gamma*** <br>
Whether gamma values are clamped to Gamma Shifter's max/min values (0% - 2000%)<br><br>
***Disable during Darkness effect*** <br>
Temporarily disables gamma effects when the Darkness effect is applied to reduce visual anomalies <br>

### HUD
***Enable Silent Mode***<br>
Disables all HUD elements<br><br>
***Show hotbar message***<br>
Enables/disables hotbar message when the gamma is changed by a keypress. <br><br>
***Display current gamma overlay***<br>
Shows the brightness at all times in the corner of the screen<br><br>
***HUD text colour***<br>
Sets a custom colour for the text in HUD elements<br><br>
***HUD location***<br>
Sets the corner of the screen that the persistent overlay is in<br><br>

### Auto Night Mode
***Enable Auto Night Mode***<br>
Automatically increases and decreases brightness at dusk and dawn<br><br>
***Night Mode Gamma***<br>
The brightness will be set when Night Mode turns on<br><br>

### Presets
***Preset 1 (%)*** <br>
A preset to immediately set a specific gamma value<br><br>
***Preset 2 (%)*** <br>
A preset to immediately set a specific gamma value<br>

</details>

## Dependencies
Required: <br>
[Fabric](https://fabricmc.net/) <br>
[Fabric API](https://modrinth.com/mod/fabric-api) <br>

Optional (for ModMenu support): <br>
[ModMenu](https://modrinth.com/mod/modmenu) <br>
[Cloth Config API](https://modrinth.com/mod/cloth-config) <br>
*ModMenu and Cloth Config API are optional, but all menu features will be unavailable without them.*


## Notes
-ModMenu and Cloth Config API are optional, but menu and customizable features will be unavailable without them <br>
-Gamma Shifter 1.0.0 is anticipated to be the last "active development" version. ie. I do not plan to continue adding features to or working on this mod except to fix bugs and update to new Minecraft versions. <br>