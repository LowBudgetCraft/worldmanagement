package crytec.worldmanagement.guis;

import crytec.worldmanagement.Language;
import net.crytec.api.smartInv.SmartInventory;

public class Menus {

	public static final SmartInventory WORLD_MAIN_MENU = SmartInventory.builder()
			.provider(new WorldMainMenu())
			.size(4, 9)
			.title(Language.GUI_TITLE_MAIN.toString())
			.build();
	
	public static final SmartInventory WORLD_SETTINGS = SmartInventory.builder()
				.provider(new WorldSettingsGUI())
				.size(5, 9)
				.title(Language.GUI_TITLE_SETTINGS.toString())
				.parent(WORLD_MAIN_MENU)
				.build();
	
	public static final SmartInventory ENV_MENU = SmartInventory.builder()
			.provider(new EnvironmentMenu())
			.size(1, 9)
			.title(Language.GUI_TITLE_ENVIRONMENT.toString())
			.build();
	
	public static final SmartInventory TYPE_MENU = SmartInventory.builder()
				.provider(new WorldTypeMenu())
				.size(1, 9)
				.title(Language.GUI_TITLE_WORLDTYPE.toString())
				.build();
	
	public static final SmartInventory DELETE_CONFIRM = SmartInventory.builder()
				.provider(new WorldDeleteConfirm())
				.size(3, 9)
				.title(Language.GUI_TITLE_DELETEION.toString())
				.build();
}
