# Gamma Shifter 0.2.0
A client-side FabricMC mod that restores the ability to set the gamma (brightness) setting beyond 100% in Minecraft 1.19+

## Features
-Gamma settings can be changed with hotkeys with pausing the game<br>
-Customizable mod properties through ModMenu/Cloth Config API implementation <br>
-Gamma settings are loaded and written from options.txt without being clamped to 100% <br>
-Adds hotkeys to set 100% and 1000% brightness instantly <br>

## Default Keybinds
-Decrease gamma by 25% (default):  `-` <br>
-Increase gamma by 25% (default):  `=` <br>
-Set max vanilla gamma (100%): `[` <br>
-Set max gamma (1000%): `]` <br>
-Toggle effects:  `G`

## Notes
-Gamma value will still be clamped at 1000% <br>
-ModMenu and Cloth Config API are optional, but menu and customizable features will be unavailable without them

### Known Issues
-If the mod is disabled, the custom gamma will not be saved to options.txt and will be reset to 100% the next time the client is launched *(this is arguably
not a bug, but it is not the intended behaviour)* <br>
