package crytec.worldmanagement.data;

public enum CWorldType {
	
	NORMAL("ßeNormal", "ß7Normale Welt."),
	FLAT("ßeFlachland", "ß7Diese Welt ist vollkommen Flach"),
	LARGE_BIOMES("ßeGroﬂe Biome", "ß7In dieser Welt sind die Biome 16-fach grˆﬂer"),
	AMPLIFIED("ßeAmplified", "ß7Erzeugt eine Welt mit sehr groﬂen Biomen und Bergen/Schluchten");
	
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
