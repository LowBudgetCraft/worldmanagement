package crytec.worldmanagement;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
 
/**
* An enum for requesting strings from the language file.
*/

public enum Language {
    TITLE("title-name", "&2[&fWorldManagement&2]"),
    
    ERROR_NO_PERM("error.noPermission", "&cYou lack the proper permissions to use this command."),
    ERROR_NO_WORLD("error.noWorld", "&cThis world does not exist"),
    ERROR_ALREADYEXIST("error.exists", "&cThis world already exists"),
    ERROR_WRONG_WORLD("error.wrongworld", "&cYou are in the wrong world for this action."),
	
    ENVIRONMENT_END("end-environment", "&fThe End"),
    ENVIRONMENT_NETHER("nether-environment", "&fNether"),
    ENVIRONMENT_NORMAL("normal-environment", "&fNormal"),
    
    GUI_TITLE_MAIN("gui.main.title", "WorldManager - Main Menu"),
    GUI_TITLE_SETTINGS("gui.settings.title", "WorldManager - Settings"),
    GUI_TITLE_ENVIRONMENT("gui.environment.title", "Select a environment"),
    GUI_TITLE_WORLDTYPE("gui.worldtype.title", "Select a world type"),
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
    GUI_CHATPROMOT_ENTERWORLDNAME("gui.general.enterworldpromt", "&7Please enter the worldname into the chat:"),
    GUI_CHATPROMOT_EXISTS("gui.general.existsinfo", "&cThere is already a configuration for this worldname. Loading the given world..."),

    GUI_WORLDTYPE_GENERATEINFO("gui.generate.info", "&7Generating a new world..."),
    
    
    
    
    
    GUI_SETTINGS_TELEPORT("gui.settings.teleport.name", "&2Teleport"),
    GUI_SETTINGS_TELEPORTFEEDBACK("gui.settings.teleport.feedback", "&2You have been teleported to the spawnpoint of %world%"),
    GUI_SETTINGS_TELEPORT_DESC("gui.settings.teleport.description", Arrays.asList("&fTeleport yourself to the", "&fspawnpoint of the selected world")),
    
    GUI_SETTINGS_SPAWNPOINT("gui.settings.spawnpoint.name", "&2Set worldspawn"),
    GUI_SETTINGS_SPAWNPOINT_DESC("gui.settings.spawnpoint.description", Arrays.asList("&fSet the spawnpoint for the world")),
    GUI_SETTINGS_SPAWNPOINT_FEEDBACK("gui.settings.spawnpoint.feedback", "&2The spawnpoint has been set to your current location."),
    
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
    GUI_SETTINGS_WEATHER_DESC("gui.settings.weather.description", Arrays.asList("&fWeather enabled: %status%"
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
    
    GUI_DELETION_CONFIRM("gui.deletion.confirm", "&2Confirm"),
    GUI_DELETION_FEEDBACK("gui.deletion.feedback", "&7The world has been deleted in %time%"),
    GUI_DELETION_ABORT("gui.deletion.abort", "&cAbort"),
    
    WORLDTYPE_NORMAL("worldtype-normal", "&fNormal"),
    WORLDTYPE_FLATWORLD("worldtype-flat", "&fFlatworld"),
    WORLDTYPE_AMPLIFIED("worldtype-amplified", "&fAmplifield"),
    WORLDTYPE_LARGE_BIOMES("worldtype-large_biomes", "&fLarge Biomes");
	
	
    private String path;
    private String def;
    private boolean isArray = false;
    
    private List<String> defArray;
    private static YamlConfiguration LANG;
 
    /**
    * Lang enum constructor.
    * @param path The string path.
    * @param start The default string.
    */
    private Language(String path, String start) {
        this.path = path;
        this.def = start;
    }
    
    private Language(String path, List<String> start) {
        this.path = path;
        this.defArray = start;
        this.isArray = true;
    }
    
    /**
    * Set the {@code YamlConfiguration} to use.
    * @param config The config to set.
    */
    public static void setFile(YamlConfiguration config) {
        LANG = config;
    }
    
    public static YamlConfiguration getFile() {
    	return LANG;
    }
    
    @Override
    public String toString() {
        if (this == TITLE) return ChatColor.translateAlternateColorCodes('&', LANG.getString(this.path, def)) + " ";
        return ChatColor.translateAlternateColorCodes('&', LANG.getString(this.path, def));
    }
    
    /**
     * Get the String with the TITLE
     * @return
     */
    public String toChatString() {
    	return TITLE.toString() + ChatColor.translateAlternateColorCodes('&', LANG.getString(this.path, def));
    }
    
    public List<String> getDescriptionArray() {
    	return LANG.getStringList(this.path).stream().map(x -> ChatColor.translateAlternateColorCodes('&', x)).collect(Collectors.toList());
    }
    
    public boolean isArray() {
    	return this.isArray;
    }
    
    public List<String> getDefArray() {
    	return this.defArray;
    }
     
    /**
    * Get the default value of the path.
    * @return The default value of the path.
    */
    public String getDefault() {
        return this.def;
    }
 
    /**
    * Get the path to the string.
    * @return The path to the string.
    */
    public String getPath() {
        return this.path;
    }
}
