package code.elix_x.excomms.serialization.binary;

import java.nio.ByteBuffer;
import java.util.Collection;

import com.google.common.reflect.TypeToken;

import code.elix_x.excomms.serialization.DVisitor;
import code.elix_x.excomms.serialization.SVisitor;
import code.elix_x.excomms.serialization.Serializer;
import code.elix_x.excomms.serialization.SerializerMain;
import code.elix_x.excomms.serialization.binary.visitor.BinaryCollectionVisitor;
import code.elix_x.excomms.serialization.binary.visitor.BinaryObjectVisitor;

public class BinarySerializerMain extends SerializerMain<Object, ByteBuffer, BinarySerializerMain> {

	public BinarySerializerMain(Serializer<Object, ?, ByteBuffer, ?, BinarySerializerMain>... serializers){
		super(serializers);
	}

	public BinarySerializerMain(Iterable<Serializer<Object, ?, ByteBuffer, ?, BinarySerializerMain>> serializers){
		super(serializers);
	}

	@Override
	public <SpS extends ByteBuffer, SpD, Args> SVisitor<Object, ByteBuffer, SpS, BinarySerializerMain, Args> visitorS(TypeToken<SpD> clas){
		if(Collection.class.isAssignableFrom(clas.getRawType())){
			return new BinaryCollectionVisitor();
		}
		return new BinaryObjectVisitor(this);
	}

	@Override
	public <SpS extends ByteBuffer, SpD, Args> DVisitor<Object, SpD, ByteBuffer, BinarySerializerMain, Args> visitorD(TypeToken<SpD> clas){
		if(Collection.class.isAssignableFrom(clas.getRawType())){
			return new BinaryCollectionVisitor();
		}
		return new BinaryObjectVisitor(this);
	}

}
