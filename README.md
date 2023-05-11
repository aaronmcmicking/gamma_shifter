# Gamma Shifter 0.3.0
A client-side FabricMC mod that restores the ability to set the gamma (brightness) setting beyond 100% in Minecraft 1.19+ and adds other customizable quality-of-life features.

## Features
-Gamma settings can be changed with hotkeys with pausing the game<br>
-Customizable mod properties through ModMenu/Cloth Config API implementation <br>
-Adds hotkeys to set 100% and 1000% brightness instantly <br>
-Adds custom presets <br>

## Default Keybinds
-Decrease gamma (25% by default):  `-` <br>
-Increase gamma (25% by default):  `=` <br>
-Set max vanilla gamma (100%): `[` <br>
-Set max gamma (1000%): `]` <br>
-Toggle effects:  `G` <br>
-Preset 1: `unbound` <br>
-Preset 2: `unbound` <br>
-Show current gamma: `unbound` <br>

## Menu settings
***Toggle mod effects*** <br>
Toggles all other mod features (ie. gamma >= 100%, hotkeys, etc)<br><br>
***Set gamma*** <br>
Sets the current gamma<br><br>
***Set gamma change-per-input*** <br>
Sets the amount that the gamma value changes by when the increase/decrease gamma key is pressed<br><br>
***Always start enabled*** <br>
Whether the mod will always start enabled or if it starts in the state the game previously closed in<br><br>
***Snap gamma to change-per-input*** <br>
Whether gamma changes will snap to the nearest multiple of the current change-per-input<br><br>
***Always save custom gamma*** <br>
Whether the custom gamma (>= 100%) will always be saved to options.txt, regardless of if the mod is enabled<br><br>
***Preset 1*** <br>
A preset to immediately set a specific gamma value<br><br>
***Preset 2*** <br>
A preset to immediately set a specific gamma value<br>

## Notes
-ModMenu and Cloth Config API are optional, but menu and customizable features will be unavailable without them