package code.elix_x.excomms.serialization.binary.serializer;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import com.google.common.base.Charsets;
import com.google.common.reflect.TypeToken;

import code.elix_x.excomms.serialization.Serializer;
import code.elix_x.excomms.serialization.binary.BinarySerializerMain;

public class BinaryStringSerializer implements Serializer<Object, String, ByteBuffer, ByteBuffer, BinarySerializerMain> {

	private final Charset charset;

	public BinaryStringSerializer(Charset charset){
		this.charset = charset;
	}

	public BinaryStringSerializer(){
		this(Charsets.UTF_8);
	}

	@Override
	public boolean acceptsS(Object o){
		return o instanceof String;
	}

	@Override
	public boolean acceptsD(ByteBuffer o, TypeToken<Object> type){
		return type.getType() == String.class;
	}

	@Override
	public ByteBuffer serialize(BinarySerializerMain serializerMain, String s){
		byte[] bytes = s.getBytes(charset);
		ByteBuffer buffer = ByteBuffer.allocate(4 + bytes.length);
		buffer.putInt(bytes.length);
		buffer.put(bytes);
		buffer.flip();
		return buffer;
	}

	@Override
	public String deserialize(BinarySerializerMain serializerMain, ByteBuffer o){
		byte[] bytes = new byte[o.getInt()];
		o.get(bytes);
		return new String(bytes, charset);
	}

}
