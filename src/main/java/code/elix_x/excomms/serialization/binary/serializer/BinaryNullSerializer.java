package code.elix_x.excomms.serialization.binary.serializer;

import java.nio.ByteBuffer;

import code.elix_x.excomms.serialization.binary.BinarySerializerMain;
import code.elix_x.excomms.serialization.generic.serializer.NullSerializer;

public class BinaryNullSerializer<SpD> extends NullSerializer<Object, SpD, ByteBuffer, ByteBuffer, BinarySerializerMain> {

	public BinaryNullSerializer(){
		super(byteBuffer -> byteBuffer.limit() == 0, () -> ByteBuffer.allocate(0));
	}

}
