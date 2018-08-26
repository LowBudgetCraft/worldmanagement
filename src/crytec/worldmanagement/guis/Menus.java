package crytec.worldmanagement.guis;

import net.crytec.api.smartInv.SmartInventory;

public class Menus {

	public static final SmartInventory WORLD_MAIN_MENU = SmartInventory.builder().id("wm.mainmenu")
			.provider(new WorldMainMenu())
			.size(4, 9)
			.title("WorldManager - Hauptmenü")
			.build();
	
	public static final SmartInventory WORLD_SETTINGS = SmartInventory.builder().id("wm.settings")
				.provider(new WorldSettingsGUI())
				.size(4, 9)
				.title("WorldManager - Einstellungen")
				.parent(WORLD_MAIN_MENU)
				.build();
	
	public static final SmartInventory ENV_MENU = SmartInventory.builder().id("wm.envselector")
			.provider(new EnvironmentMenu())
			.size(1, 9)
			.title("Wähle die Umgebung")
			.build();
	
	public static final SmartInventory DELETE_CONFIRM = SmartInventory.builder().id("wm.deleteConfirm")
				.provider(new WorldDeleteConfirm())
				.size(3, 9)
				.title("Endgültiges löschen bestätigen")
				.build();
}
