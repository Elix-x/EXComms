package code.elix_x.excomms.primitive;

public enum Primitive {

	BOOLEAN(boolean.class, Boolean.class, 1), BYTE(byte.class, Byte.class, Byte.SIZE), SHORT(short.class, Short.class, Short.SIZE), INT(int.class, Integer.class, Integer.SIZE), LONG(long.class, Long.class, Long.SIZE), FLOAT(float.class, Float.class, Float.SIZE), DOUBLE(double.class, Double.class, Double.SIZE);

	private final Class<?> primitiveClass;
	private final Class<?> boxedClass;

	private final int bits;

	private Primitive(Class<?> primitiveClass, Class<?> boxedClass, int bits){
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

	public static Primitive getPrimitive(Class<?> clas){
		for(Primitive primitive : values()){
			if(primitive.primitiveClass == clas || primitive.boxedClass == clas) return primitive;
		}
		throw new IllegalArgumentException(String.format("Class %s is neither primitive type, nor boxed primitive type!", clas.getName()));
	}

}
