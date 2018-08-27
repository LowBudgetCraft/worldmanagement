package crytec.worldmanagement.data;

import lombok.Getter;

public enum CEnvironment {
	
	NORMAL("Normal"),
	NETHER("Nether"),
	THE_END("Endwelt"),
	VOID("Voidwelt");
	
	@Getter
	private String displayname;

	private CEnvironment(String name) {
		this.displayname = name;
	}
	
}
