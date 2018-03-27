package crytec.worldmanagement;

public enum CWorldType {
	
	NORMAL("§eNormal", "§7Normale Welt."),
	FLAT("§eFlachland", "§7Diese Welt ist vollkommen Flach"),
	LARGE_BIOMES("§eGroße Biome", "§7In dieser Welt sind die Biome 16-fach größer"),
	AMPLIFIED("§eAmplified", "§7Erzeugt eine Welt mit sehr großen Biomen und Bergen/Schluchten"),
	VOID("§eVoid", "§7Erstellt eine Welt ohne Blöcke");

	
	
	private String description;
	private String name;
	
	private CWorldType(String name, String description) {
		this.name = name;
		this.description = description;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public String getName() {
		return this.name;
	}
}
