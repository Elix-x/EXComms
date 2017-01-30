package code.elix_x.excomms.primitive;

import com.google.common.reflect.TypeParameter;
import com.google.common.reflect.TypeToken;

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

	@SuppressWarnings("unchecked")
	public <T> T getValueAs(){
		return (T) value;
	}

	public void setValue(P value){
		this.value = value;
	}

	public PrimitiveType getType(){
		return type;
	}

	@SuppressWarnings("unchecked")
	public TypeToken<Primitive<P>> typeToken(){
		return (TypeToken<Primitive<P>>) new TypeToken<Primitive<P>>(){}.where(new TypeParameter<P>(){}, (Class<P>) type.getBoxedClass());
	}

}
