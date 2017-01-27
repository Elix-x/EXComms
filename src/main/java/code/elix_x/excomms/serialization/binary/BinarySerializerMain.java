package code.elix_x.excomms.serialization.binary;

import java.nio.ByteBuffer;

import code.elix_x.excomms.serialization.DVisitor;
import code.elix_x.excomms.serialization.SVisitor;
import code.elix_x.excomms.serialization.SerializerMain;

public class BinarySerializerMain extends SerializerMain<Object, ByteBuffer, BinarySerializerMain> {

	@Override
	public <SpS extends ByteBuffer, SpD, Args> SVisitor<Object, ByteBuffer, SpS, BinarySerializerMain, Args> visitorS(Class<SpD> clas){
		return null;
	}

	@Override
	public <SpS extends ByteBuffer, SpD, Args> DVisitor<Object, SpD, ByteBuffer, BinarySerializerMain, Args> visitorD(Class<SpS> clas){
		return null;
	}

}
