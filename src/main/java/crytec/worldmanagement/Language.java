/*
 *
 *  * This file is part of WorldManagement, licensed under the MIT License.
 *  *
 *  *  Copyright (c) crysis992 <crysis992@gmail.com>
 *  *  Copyright (c) contributors
 *  *
 *  *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  *  of this software and associated documentation files (the "Software"), to deal
 *  *  in the Software without restriction, including without limitation the rights
 *  *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  *  copies of the Software, and to permit persons to whom the Software is
 *  *  furnished to do so, subject to the following conditions:
 *  *
 *  *  The above copyright notice and this permission notice shall be included in all
 *  *  copies or substantial portions of the Software.
 *  *
 *  *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  *  SOFTWARE.
 *
 */

package crytec.worldmanagement;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * An enum for requesting strings from the language file.
 */

public enum Language {
  TITLE("title-name", "&2[&fWorldManagement&2]"),

  ERROR_NO_WORLD_PERM("error.noWorldPermission", "&cYou lack the proper permissions to enter this world."),
  ERROR_ALREADYEXIST("error.exists", "&cThis world already exists"),
  ERROR_WRONG_WORLD("error.wrongworld", "&cYou are in the wrong world for this action."),
  ERROR_WRONG_NAME("error.worldname", "&cThe worldname may only contain alphanumeric characters. (a-z | 0-9)"),
  ERROR_WORLD_UNLOADED("error.worldUnloaded", "&cThe world you were in was unloaded. You've been teleported to the main worlds spawn location"),

  ENVIRONMENT_END("end-environment", "&fThe End"),
  ENVIRONMENT_NETHER("nether-environment", "&fNether"),
  ENVIRONMENT_NORMAL("normal-environment", "&fNormal"),

  GENERAL_GAMEMODE_FORCED("general.gamemode.forced.user", "&fThis world has a gamemode restriction. Your gamemode has been changed to &2%gamemode%"),

  GUI_TITLE_MAIN("gui.main.title", "WorldManager - Main Menu"),
  GUI_TITLE_SETTINGS("gui.settings.title", "WorldManager - Settings"),
  GUI_TITLE_DELETEION("gui.deletion.title", "&4&lConfirm deletion!"),


  GUI_MAIN_DESCRIPTION_ENABLED("gui.main.description-enabled",
      Arrays.asList("&2Left Click &fto open the Settings"
          , ""
          , "&7Worldtype: %worldtype%"
          , "&7Environment: %environment%"
      )),

  GUI_MAIN_DESCRIPTION_DISABLED("gui.main.description-disabled",
      Arrays.asList("&2Left Click &fto open the Settings"
          , ""
          , "&cThis world is deactivated, to change"
          , "&cany settings this world has to be"
          , "&cloaded."
      )),


  GUI_GENERAL_BACK("gui.general.previouspage", "&7Previous page"),
  GUI_GENERAL_NEXT("gui.general.nextpage", "&7Next page"),
  GUI_GENERAL_NEWWORLD("gui.general.createNewWorld", "&2Create a new world"),
  GUI_ACTIVATING_WORLD("gui.general.activateInfo", "&2World %world% has been set to 'active'"),
  GUI_CHATPROMOT_ENTERWORLDNAME("gui.general.enterworldpromt", "&7Please enter the worldname into the chat:"),
  GUI_CHATPROMOT_EXISTS("gui.general.existsinfo", "&cThere is already a configuration for this worldname. Loading the given world..."),

  GUI_CREATOR_ENTER_GENERATOR_BUTTON("path.gui.generate.button", "&7Set World Generator"),
  GUI_CREATOR_ENTER_GENERATOR_BUTTON_DESC("path.gui.generate.buttonDescription", "&7Currently selected: &6%generator%"),
  GUI_CREATOR_ENTER_GENERATOR("path.gui.generate.enterGenerator", "&7Please enter a &cvalid &7 generator:"),
  GUI_CREATOR_ENTER_GENERATOR_ERROR("path.gui.generate.enterGeneratorError", "&cFailed to find a generator with that name."),
  GUI_CREATOR_RANDOMIZE_SEED("path.gui.generate.randomseed", "&7Randomize the seed."),
  GUI_CREATOR_RANDOMIZE_SEED_DESCRIPTION("path.gui.generate.randomseedDesc", Arrays.asList("&7Currently selected:", "&b%seed%")),

  GUI_CREATOR_RANDOMIZE_CREATEWORLD("path.gui.generate.createWorld", "&2Create world %world%"),
  GUI_CREATOR_RANDOMIZE_CREATEWORLD_DESC("path.gui.generate.createWorldDesc", Arrays.asList(
      "&7Worldname:&6 %world%",
      "&7Environment:&6 %environment%",
      "&7WorldType:&6 %type%",
      "&7Generator:&6 %generator%",
      "&7Seed:&6 %seed%")),


  GUI_SETTINGS_TELEPORT("gui.settings.teleport.name", "&2Teleport"),
  GUI_SETTINGS_TELEPORTFEEDBACK("gui.settings.teleport.feedback", "&2You have been teleported to the spawnpoint of %world%"),
  GUI_SETTINGS_TELEPORT_DESC("gui.settings.teleport.description", Arrays.asList("&fTeleport yourself to the", "&fspawnpoint of the selected world")),

  GUI_SETTINGS_SPAWNPOINT("gui.settings.spawnpoint.name", "&2Set worldspawn"),
  GUI_SETTINGS_SPAWNPOINT_DESC("gui.settings.spawnpoint.description", Collections.singletonList("&fSet the spawnpoint for the world")),
  GUI_SETTINGS_SPAWNPOINT_FEEDBACK("gui.settings.spawnpoint.feedback", "&2The spawnpoint has been set to your current location."),

  GUI_SETTINGS_PERMISSIN("gui.settings.permission.name", "&2Set permission"),
  GUI_SETTINGS_PERMISSIN_DESC("gui.settings.permission.description", "&fCurrent permission: &6%permission% (&2Right click to delete)"),
  GUI_SETTINGS_PERMISSIN_CHATPROMPT("gui.settings.permission.chatprompt", "&7Please enter the new permission node:"),

  GUI_SETTINGS_INFORMATION("gui.settings.information.name", "&fInformations:"),
  GUI_SETTINGS_INFORMATION_DESCRIPTION("gui.settings.information.description", Arrays.asList("&fWorld type: &e%worldtype%"
      , "&fEnvironment: &e%environment%"
      , "&fSeed: &e%seed%"
      , "&fEntities: &e%entities%"
      , "&fLoaded chunks: &e%chunks%")),

  GUI_SETTINGS_MOBSPAWN("gui.settings.mobspawn.name", "&2Monster spawning"),
  GUI_SETTINGS_MOBSPAWN_DESC("gui.settings.mobspawn.description", Arrays.asList("&fMonsters are allowed to spawn: %status%"
      , ""
      , "&fToggels wether mobs can spawn or not."
      , "&cThis will not remove existing mobs.")),

  GUI_SETTINGS_ANIMALSPAWN("gui.settings.animalspawn.name", "&2Animal spawning"),
  GUI_SETTINGS_ANIMALSPAWN_DESC("gui.settings.animalspawn.description", Arrays.asList("&fAnimals are allowed to spawn: %status%"
      , ""
      , "&fToggels wether animals can spawn or not."
      , "&cThis will not remove existing mobs.")),

  GUI_SETTINGS_DIFFICULTY("gui.settings.difficulty.name", "&2Difficulty"),
  GUI_SETTINGS_DIFFICULTY_DESC("gui.settings.difficulty.description", Arrays.asList("&fCurrent difficulty: %status%"
      , ""
      , "&fChanges the difficulity.")),

  GUI_SETTINGS_PVP("gui.settings.pvp.name", "&2PvP"),
  GUI_SETTINGS_PVP_DESC("gui.settings.pvp.description", Arrays.asList("&fPvP enabled: %status%"
      , ""
      , "&fAllow/Disallow PvP in this world")),

  GUI_SETTINGS_SPAWNCHUNKS("gui.settings.spawnchunks.name", "&2Keep Spawn loaded"),
  GUI_SETTINGS_SPAWNCHUNKS_DESC("gui.settings.spawnchunks.description", Arrays.asList("&fKeep spawn loaded: %status%"
      , ""
      , "&fKeeps the chunks arround the world spawnpoint"
      , "&fpermanently loaded."
      , "&fCan be disabled for rarely visited worlds"
      , "&7Default: on"
  )),

  GUI_SETTINGS_FIRESPREAD("gui.settings.firespread.name", "&2Toggle firespread"),
  GUI_SETTINGS_FIRESPREAD_DESC("gui.settings.firespread.description", Arrays.asList("&fFire will spread: %status%"
      , ""
      , "&fToggle the status of firespread."
      , "&fWhen disabled fire will not spread and"
      , "&fnot cause any damage to blocks."
      , "&7Default: on"
  )),

  GUI_SETTINGS_TIME("gui.settings.time.name", "&2Freeze time"),
  GUI_SETTINGS_TIME_DESC("gui.settings.time.description", Arrays.asList("&fTime is frozen: %status%"
      , ""
      , "&fIf enabled, the world time will not"
      , "&ftick. Meaning the time in this world is"
      , "&ffrozen. Unless changed by plugins or commands."
      , "&7Default: off"
  )),

  GUI_SETTINGS_WEATHER("gui.settings.weather.name", "&2Toggle weather"),
  GUI_SETTINGS_WEATHER_DESC("gui.settings.weather.description", Arrays.asList("&fWeather disabled: %status%"
      , ""
      , "&fIf disabled the weather will not change"
      , "&7Default: off"
  )),

  GUI_SETTINGS_ACTIVATEWORLD("gui.settings.activate.name", "&aActivate world"),
  GUI_SETTINGS_ACTIVATEWORLD_DESC("gui.settings.activate.description", Arrays.asList("&fActivates and loads the given"
      , "&fworld into memory."
      , "&fThe world will be loaded automatically on"
      , "&fserver startup"
  )),

  GUI_SETTINGS_DISABLEWORLD("gui.settings.disable.name", "&cDisable world"),
  GUI_SETTINGS_DISABLEWORLD_DESC("gui.settings.disable.description", Arrays.asList("&cDisables and unloads the given"
      , "&fworld from memory."
      , "&fThe world will &cNO longer&f loaded automatically on"
      , "&fserver startup"
      , ""
      , "&7The world can be activated at any time again"
      , "&7from this menu."
  )),
  GUI_SETTINGS_DISABLE_FEEDBACK("gui.settings.disable.feedback", "&7The world has been unloaded in %time%"),


  GUI_SETTINGS_DELETION("gui.settings.deletion.name", "&4&lDelete world"),
  GUI_SETTINGS_DELETION_DESCRIPTION("gui.settings.deletion.description", Arrays.asList("&cDeletes the world permanently"
      , "&cfrom the disk."
      , "&cThis action cannot be undone!"
      , "&cYour world will be gone forever.")),

  GUI_SETTINGS_GAMEMODE("gui.settings.gamemode.name", "Default Gamemode"),
  GUI_SETTINGS_GAMEMODE_DESCRIPTION("gui.settings.gamemode.description", Arrays.asList("&7Set the default gamemode"
      , "&7Players are forced into this"
      , "&7gamemode when entering the world"
      , "&7Current gamemode: &6%gamemode%")),


  GUI_DELETION_CONFIRM("gui.deletion.confirm", "&2Confirm"),
  GUI_DELETION_FEEDBACK("gui.deletion.feedback", "&7The world has been deleted in %time%"),
  GUI_DELETION_ABORT("gui.deletion.abort", "&cAbort"),

  WORLDTYPE_NORMAL("worldtype-normal", "&fNormal"),
  WORLDTYPE_FLATWORLD("worldtype-flat", "&fFlatworld"),
  WORLDTYPE_AMPLIFIED("worldtype-amplified", "&fAmplifield"),
  WORLDTYPE_LARGE_BIOMES("worldtype-large_biomes", "&fLarge Biomes");


  private final String path;
  private String def;
  private boolean isArray = false;

  private List<String> defArray;
  private static YamlConfiguration LANG;

  /**
   * Lang enum constructor.
   *
   * @param path  The string path.
   * @param start The default string.
   */
  private Language(String path, String start) {
    this.path = path;
    def = start;
  }

  private Language(String path, List<String> start) {
    this.path = path;
    defArray = start;
    isArray = true;
  }

  /**
   * Set the {@code YamlConfiguration} to use.
   *
   * @param config The config to set.
   */
  private static void setFile(YamlConfiguration config) {
    Language.LANG = config;
  }

  public static YamlConfiguration getFile() {
    return Language.LANG;
  }

  @Override
  public String toString() {
    if (this == Language.TITLE) {
      return ChatColor.translateAlternateColorCodes('&', Language.LANG.getString(path, def)) + " ";
    }
    return ChatColor.translateAlternateColorCodes('&', Language.LANG.getString(path, def));
  }

  /**
   * Get the String with the TITLE
   */
  public String toChatString() {
    return Language.TITLE.toString() + ChatColor.translateAlternateColorCodes('&', Language.LANG.getString(path, def));
  }

  public List<String> getDescriptionArray() {
    return Language.LANG.getStringList(path).stream().map(x -> ChatColor.translateAlternateColorCodes('&', x)).collect(Collectors.toList());
  }

  public boolean isArray() {
    return isArray;
  }

  public List<String> getDefArray() {
    return defArray;
  }

  /**
   * Get the default value of the path.
   *
   * @return The default value of the path.
   */
  public String getDefault() {
    return def;
  }

  /**
   * Get the path to the string.
   *
   * @return The path to the string.
   */
  public String getPath() {
    return path;
  }

  private static boolean isValidPath(String path) {
    return Arrays.stream(values()).anyMatch(lang -> lang.getPath().equals(path));
  }

  public static void initialize(JavaPlugin plugin) throws IOException {
    File languageFile = new File(plugin.getDataFolder(), "language.yml");
    if (!languageFile.exists() && !languageFile.createNewFile()) {
      plugin.getLogger().severe("Failed to create language.yml");
      return;
    }

    YamlConfiguration langCfg = YamlConfiguration.loadConfiguration(languageFile);
    setFile(langCfg);

    int updated = 0;
    for (Language entry : values()) {
      if (!langCfg.isSet(entry.getPath())) {
        langCfg.set(entry.getPath(), entry.isArray ? entry.getDefArray() : entry.getDefault());
        updated++;
      }
    }

    if (updated > 0) {
      langCfg.save(languageFile);
      plugin.getLogger().info("Updated language.yml with " + updated + " new entries!");
    }

    int removed = 0;
    for (String key : langCfg.getRoot().getKeys(true)) {

      if (!langCfg.isConfigurationSection(key) && !isValidPath(key)) {
        plugin.getLogger().info(key + " is no longer a valid language translation...removing");
        langCfg.set(key, null);
        removed++;
      }
    }

    if (removed > 0) {
      plugin.getLogger().info("Removed " + removed + " old language entries from your language.yml");
      langCfg.save(languageFile);
    }
  }
}