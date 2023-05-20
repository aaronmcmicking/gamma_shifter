# Gamma Shifter 0.3.1

### Additions
-Added toggleable persistent overlay in corner of screen (location can be customized in ModMenu)<br>
-Added silent mode to disable all HUD elements<br>
-Added "General", "HUD", and "Presets" categories to ModMenu <br>
-Added option to enable/disable hotbar message when gamma changed by keypress. <br>

### Changes
-ModMenu description now matches Modrinth blurb <br>
-HUD text colour can now be customized <br>
-Refactored HUD implementation <br>
-Changed gamma change text from "Gamma = 100%" to "Gamma: 100%" <br>
-Changed default preset values to 250% and 500% <br>
-Config menu and HUD now support translation keys (English is still the only supported language) <br>

### Fixes
-Fixed bug where gamma values >1.0 could not be set when the language was not set to English <br>
-Fixed bug where gamma values from options.txt would not be initialized if `alwaysStartEnabled` was set to `false` <br>
-Fixed bug where the `Show Current Gamma` key would be silenced when `showMessageOnGammaChange` was set to `false`<br>

### Notes
-New options are available through ModMenu (requires ModMenu and Cloth Config API)<br>
-GitHub repository now includes program to automatically build mod JARs for 1.19-1.19.4 (`buildAllVersions.jar`)<br>