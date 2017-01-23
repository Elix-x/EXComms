package code.elix_x.excomms.serialization.visit.binary;

import java.nio.ByteBuffer;

import code.elix_x.excomms.primitive.Primitive;
import code.elix_x.excomms.primitive.PrimitiveType;
import code.elix_x.excomms.serialization.Serializer;

public class BinaryPrimitveSerializer<P> implements Serializer<Object, Primitive<P>, ByteBuffer, ByteBuffer, BinarySerializerMain> {

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
	public ByteBuffer serialize(BinarySerializerMain serializerMain, Primitive<P> p){
		ByteBuffer buffer = ByteBuffer.allocate(type.hashCode());
		switch(type){
			case BOOLEAN:
				buffer.put((byte) (((Boolean) p.getValue()) == true ? 1 : 0));
				break;
			case BYTE:
				buffer.put((byte) p.getValue());
				break;
			case SHORT:
				buffer.putShort((short) p.getValue());
				break;
			case INT:
				buffer.putInt((int) p.getValue());
				break;
			case LONG:
				buffer.putLong((long) p.getValue());
				break;
			case FLOAT:
				buffer.putFloat((float) p.getValue());
				break;
			case DOUBLE:
				buffer.putDouble((double) p.getValue());
				break;
		}
		return buffer;
	}

	@Override
	public Primitive<P> deserialize(BinarySerializerMain serializerMain, ByteBuffer buffer){
		Primitive p;
		switch(type){
			case BOOLEAN:
				p = new Primitive<>(buffer.get() == 1);
				break;
			case BYTE:
				p = new Primitive<>(buffer.get());
				break;
			case SHORT:
				p = new Primitive<>(buffer.getShort());
				break;
			case INT:
				p = new Primitive<>(buffer.getInt());
				break;
			case LONG:
				p = new Primitive<>(buffer.getLong());
				break;
			case FLOAT:
				p = new Primitive<>(buffer.getFloat());
				break;
			case DOUBLE:
				p = new Primitive<>(buffer.getDouble());
				break;
			default:
				p = null;
				break;
		}
		return p;
	}

}
