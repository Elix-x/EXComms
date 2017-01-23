package code.elix_x.excomms.primitive;

public final class Primitive<P> {

	private final PrimitiveType type;
	private P value;

	public Primitive(PrimitiveType type){
		this.type = type;
	}

	public Primitive(P value){
		this(PrimitiveType.getPrimitive(value.getClass()));
		this.value = value;
	}

	public P getValue(){
		return value;
	}

	public void setValue(P value){
		this.value = value;
	}

	public PrimitiveType getType(){
		return type;
	}

}
