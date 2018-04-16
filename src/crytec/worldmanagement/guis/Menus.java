package crytec.worldmanagement.guis;

import net.crytec.api.smartInv.SmartInventory;

public class Menus {

	public static final SmartInventory WORLD_MAIN_MENU = SmartInventory.builder().id("wm.mainmenu")
			.provider(new WorldMainMenu())
			.size(4, 9)
			.title("WM - Hauptmenü")
			.build();
	
	public static final SmartInventory ENV_MENU = SmartInventory.builder().id("wm.envselector")
			.provider(new EnvironmentMenu())
			.size(1, 9)
			.title("Wähle die Umgebung")
			.build();
}
