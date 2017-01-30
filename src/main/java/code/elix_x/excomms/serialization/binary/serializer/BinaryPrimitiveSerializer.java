package code.elix_x.excomms.serialization.binary.serializer;

import java.lang.reflect.ParameterizedType;
import java.nio.ByteBuffer;

import com.google.common.reflect.TypeToken;

import code.elix_x.excomms.primitive.Primitive;
import code.elix_x.excomms.primitive.PrimitiveType;
import code.elix_x.excomms.serialization.Serializer;
import code.elix_x.excomms.serialization.binary.BinarySerializerMain;

public class BinaryPrimitiveSerializer<P> implements Serializer<Object, Primitive<P>, ByteBuffer, ByteBuffer, BinarySerializerMain> {

	private final PrimitiveType type;

	public BinaryPrimitiveSerializer(PrimitiveType type){
		this.type = type;
	}

	@Override
	public boolean acceptsS(Object o){
		return o instanceof Primitive && ((Primitive) o).getType() == type;
	}

	@Override
	public boolean acceptsD(ByteBuffer o, TypeToken type){
		return type.getRawType() == Primitive.class && ((ParameterizedType) type.getType()).getActualTypeArguments()[0] == this.type.getBoxedClass();
	}

	@Override
	public ByteBuffer serialize(BinarySerializerMain serializerMain, Primitive<P> p){
		ByteBuffer buffer = ByteBuffer.allocate(type.getBytes());
		switch(type){
			case BOOLEAN:
				buffer.put((byte) (p.<Boolean> getValueAs() == true ? 1 : 0));
				break;
			case BYTE:
				buffer.put(p.<Byte> getValueAs());
				break;
			case SHORT:
				buffer.putShort(p.getValueAs());
				break;
			case INT:
				buffer.putInt(p.getValueAs());
				break;
			case LONG:
				buffer.putLong(p.getValueAs());
				break;
			case FLOAT:
				buffer.putFloat(p.getValueAs());
				break;
			case DOUBLE:
				buffer.putDouble(p.getValueAs());
				break;
			case CHAR:
				buffer.getChar(p.getValueAs());
				break;
		}
		buffer.flip();
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
			case CHAR:
				p = new Primitive<>(buffer.getChar());
			default:
				p = null;
				break;
		}
		return p;
	}

	public static class ActualTypesWrapper<P> implements Serializer<Object, Object, ByteBuffer, ByteBuffer, BinarySerializerMain> {

		private final PrimitiveType type;

		public ActualTypesWrapper(PrimitiveType type){
			this.type = type;
		}

		@Override
		public boolean acceptsS(Object o){
			return o.getClass() == type.getPrimitiveClass() || o.getClass() == type.getBoxedClass();
		}

		@Override
		public boolean acceptsD(ByteBuffer o, TypeToken type){
			return PrimitiveType.getPrimitive(type.getRawType()) == this.type;
		}

		@Override
		public ByteBuffer serialize(BinarySerializerMain serializerMain, Object o){
			return serializerMain.serialze(new Primitive(o));
		}

		@Override
		public Object deserialize(BinarySerializerMain serializerMain, ByteBuffer o){
			return serializerMain.deserialize(o, new Primitive<>(type).typeToken()).getValue();
		}

	}

}
