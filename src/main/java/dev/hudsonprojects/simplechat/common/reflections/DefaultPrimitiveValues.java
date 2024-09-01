package dev.hudsonprojects.simplechat.common.reflections;

public class DefaultPrimitiveValues {
	
	private static final DefaultPrimitiveValues SINGLETON  = new DefaultPrimitiveValues();
	
	private DefaultPrimitiveValues() {}
	
	private byte defaultByte;
	private short defaultShort;
	private int defaultInt;
	private long defaultLong;
	private float defaultFloat;
	private double defaultDouble;
	private char defaultChar;
	private boolean defaultBoolean;
	
	public byte getByte() {
		return defaultByte;
	}
	public short getShort() {
		return defaultShort;
	}
	public int getInt() {
		return defaultInt;
	}
	public long getLong() {
		return defaultLong;
	}
	public float getFloat() {
		return defaultFloat;
	}
	public double getDouble() {
		return defaultDouble;
	}
	public char getChar() {
		return defaultChar;
	}
	public boolean getBoolean() {
		return defaultBoolean;
	}
	
	public static final DefaultPrimitiveValues getDefaults() {
		return SINGLETON;
	}

}
