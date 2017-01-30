package code.elix_x.excomms.primitive;

public enum PrimitiveType {

	BOOLEAN(boolean.class, Boolean.class, 1), BYTE(byte.class, Byte.class, Byte.SIZE), SHORT(short.class, Short.class, Short.SIZE), INT(int.class, Integer.class, Integer.SIZE), LONG(long.class, Long.class, Long.SIZE), FLOAT(float.class, Float.class, Float.SIZE), DOUBLE(double.class, Double.class, Double.SIZE), CHAR(char.class, Character.class, Character.SIZE);

	private final Class<?> primitiveClass;
	private final Class<?> boxedClass;

	private final int bits;

	private PrimitiveType(Class<?> primitiveClass, Class<?> boxedClass, int bits){
		this.primitiveClass = primitiveClass;
		this.boxedClass = boxedClass;
		this.bits = bits;
	}

	public Class<?> getPrimitiveClass(){
		return primitiveClass;
	}

	public Class<?> getBoxedClass(){
		return boxedClass;
	}

	public int getBits(){
		return bits;
	}

	public int getBytes(){
		return (int) Math.ceil(bits / 8f);
	}

	public static PrimitiveType getPrimitive(Class<?> clas){
		for(PrimitiveType primitive : values()){
			if(primitive.primitiveClass == clas || primitive.boxedClass == clas) return primitive;
		}
		return null;
	}

}
