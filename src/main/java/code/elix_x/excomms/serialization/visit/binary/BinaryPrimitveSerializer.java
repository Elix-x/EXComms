package code.elix_x.excomms.serialization.visit.binary;

import java.nio.ByteBuffer;

import code.elix_x.excomms.primitive.Primitive;
import code.elix_x.excomms.primitive.PrimitiveType;
import code.elix_x.excomms.serialization.Serializer;

public class BinaryPrimitveSerializer<P extends Primitive<?>> implements Serializer<Object, P, ByteBuffer, ByteBuffer, BinarySerializerMain> {

	private final PrimitiveType type;

	public BinaryPrimitveSerializer(PrimitiveType type){
		this.type = type;
	}

	@Override
	public boolean acceptsS(Object o){
		return o.getClass() == type.getPrimitiveClass() || o.getClass() == type.getBoxedClass();
	}

	@Override
	public boolean acceptsD(ByteBuffer o, Class<Object> clas){
		return PrimitiveType.getPrimitive(clas) == type;
	}

	@Override
	public ByteBuffer serialize(BinarySerializerMain serializerMain, P o){
		return null;
	}

	@Override
	public P deserialize(BinarySerializerMain serializerMain, ByteBuffer o){
		return null;
	}

}
