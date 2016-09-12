package fzi.mottem.runtime.dataexchanger;

public class Signal {
	
	public enum SignalType {
		HW_OUTPUT("Output"), 
		HW_INPUT("Input"), 
		BIDIRECTIONAL("Bidirectional"),
		NONE("None");
		
		public static String[] stringValues(){
			String[] result = new String[values().length];
			int i=0;
			for(SignalType t : values()){
				result[i++] = t.toString();
			}
			return result;
		}
		private String name;
		private SignalType(String name) {
			this.name = name;
			
		}
		
		@Override
		public String toString() {
			return name;
		}
	}

	private String id;
	private String simpleName = "unnamed";
	
	private SignalType type = SignalType.BIDIRECTIONAL;

	public SignalType getType() {
		return type;
	}

	public void setType(SignalType type) {
		this.type = type;
	}

	public String getSimpleName() {
		return simpleName;
	}

	public void setSimpleName(String simpleName) {
		this.simpleName = simpleName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * This constructor sets the id field to the given id parameter and derives
	 * a simple name for this signal from it.
	 * 
	 * @param id
	 *            the unique string id for this signal.
	 */
	public Signal(String id) {
		this.id = id;
		if (id.length() > 8) {
			this.simpleName = id.substring(id.length() - 8);
		} else {
			this.simpleName = id;
		}
	}

	/**
	 * This constructor sets the id and simple name fields from the given
	 * parameters.
	 * 
	 * @param id
	 *            the unique string id for this signal.
	 * @param simpleName
	 *            a simple name for this signal that can be shown to the user if
	 *            needed.
	 */
	public Signal(String id, String simpleName) {
		this.id = id;
		this.simpleName = simpleName;
	}
	
	/**
	 * This constructor sets the id, simple name and type fields from the given
	 * parameters.
	 * 
	 * @param id
	 *            the unique string id for this signal.
	 * @param simpleName
	 *            a simple name for this signal that can be shown to the user if
	 *            needed.
	 * @param type
	 *            SignalType. HW_OUTPUT, HW_INPUT or BIDIRECTIONAL
	 */
	public Signal(String id, String simpleName, SignalType type) {
		this.id = id;
		this.simpleName = simpleName;
		this.type = type;
	}
}
