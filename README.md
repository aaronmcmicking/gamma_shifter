# Gamma Shifter 0.3.1-beta.4
A client-side FabricMC mod that restores the ability to set the gamma (brightness) setting beyond 100% in Minecraft 1.19+ and adds other customizable quality-of-life features.

## Features
-Gamma settings can be changed with hotkeys with pausing the game<br>
-Customizable mod properties through ModMenu/Cloth Config API implementation <br>
-Adds hotkeys to set 100% and 1000% brightness instantly <br>
-Adds custom presets <br>

### Default Keybinds

***Decrease brightness (25% by default)***<br>
Keybind: `-` <br>

***Increase brightness (25% by default)***<br>
Keybind: `=`<br>

***Set vanilla max brightness (100%)***<br>
Keybind: `[` <br>

***Set max brightness (1000%)***<br>
Keybind: `]` <br>

***Toggle effects***<br>
Keybind: `G` <br>

***Preset 1***<br>
Keybind: `unbound`<br>

***Preset 2***<br>
Keybind: `unbound`<br>

***Show current gamma***<br>
Keybind: `unbound`<br>

## Menu/Config settings
Menu settings are available through ModMenu (see Dependencies below).<br>

### General
***Toggle mod effects*** <br>
Toggles all mod features on/off<br><br>
***Set gamma*** <br>
Sets the current gamma<br><br>
***Set gamma change-per-input*** <br>
Sets the amount that the gamma value changes by when the increase/decrease gamma key is pressed<br><br>
***Always start enabled*** <br>
Whether the mod will always start enabled or if it starts in the state the game previously closed in<br><br>
***Snap gamma to change-per-input*** <br>
Whether gamma changes will snap to the nearest multiple of the current change-per-input<br><br>
***Always save custom gamma*** <br>
Whether the custom gamma (>= 100%) will always be saved to options.txt, regardless of if the mod is enabled<br>

### HUD
***Enable Silent Mode***<br>
Disables all HUD elements<br><br>
***Show brightness on screen***<br>
Shows the brightness at all times in the corner of the screen<br><br>
***Change HUD text colour***<br>
Sets a custom colour for the text in HUD elements<br><br>
***Overlay location***<br>
Sets the corner of the screen that the persistent overlay is in<br>

### Presets
***Preset 1*** <br>
A preset to immediately set a specific gamma value<br><br>
***Preset 2*** <br>
A preset to immediately set a specific gamma value<br>

## Dependencies
Required: <br>
[Fabric](https://fabricmc.net/) <br>
[Fabric API](https://modrinth.com/mod/fabric-api) <br>

Optional (for ModMenu support): <br>
[ModMenu](https://modrinth.com/mod/modmenu) <br>
[Cloth Config API](https://modrinth.com/mod/cloth-config) <br>
*ModMenu and Cloth Config API are optional, but all menu features will be unavailable without them.*


## Notes
-ModMenu and Cloth Config API are optional, but menu and customizable features will be unavailable without them