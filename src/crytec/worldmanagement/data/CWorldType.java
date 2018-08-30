package crytec.worldmanagement.data;

public enum CWorldType {
	
	NORMAL("�eNormal", "�7Normale Welt."),
	FLAT("�eFlachland", "�7Diese Welt ist vollkommen Flach"),
	LARGE_BIOMES("�eGro�e Biome", "�7In dieser Welt sind die Biome 16-fach gr��er"),
	AMPLIFIED("�eAmplified", "�7Erzeugt eine Welt mit sehr gro�en Biomen und Bergen/Schluchten");
	
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
