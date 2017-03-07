package code.elix_x.excomms.serialization.binary.visitor;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import com.google.common.reflect.TypeToken;

import code.elix_x.excomms.reflection.ReflectionHelper.AClass;
import code.elix_x.excomms.serialization.DVisitor;
import code.elix_x.excomms.serialization.SVisitor;
import code.elix_x.excomms.serialization.binary.BinarySerializerMain;

public class BinaryObjectVisitor<SpS extends ByteBuffer, SpD extends Object> implements SVisitor<Object, ByteBuffer, SpS, BinarySerializerMain, String>, DVisitor<Object, SpD, ByteBuffer, BinarySerializerMain, String> {

	private final BinarySerializerMain main;

	public BinaryObjectVisitor(BinarySerializerMain main){
		this.main = main;
	}

	/*
	 * Deserialization
	 */

	private ByteBuffer workBuffer;
	private Map<String, Integer> namePosMap;

	@Override
	public SpD startVisit(ByteBuffer buffer, Supplier<TypeToken<SpD>> clas){
		this.workBuffer = buffer;
		this.namePosMap = new HashMap<>();
		int size = workBuffer.getInt();
		for(int i = 0; i < size; i++){
			String name = main.deserialize(workBuffer, String.class);
			int length = workBuffer.getInt();
			namePosMap.put(name, workBuffer.position());
			workBuffer.position(workBuffer.position() + length);
		}
		return new AClass<SpD>((Class<SpD>) clas.get().getRawType()).getDeclaredConstructor().setAccessible(true).newInstance();
	}

	@Override
	public ByteBuffer visit(String args){
		workBuffer.position(namePosMap.get(args));
		return workBuffer;
	}

	/*
	 * Serialization
	 */

	private List<ByteBuffer> buffersList;

	@Override
	public void visit(ByteBuffer object, String args){
		if(buffersList == null) buffersList = new ArrayList<>();
		ByteBuffer name = main.serialize(args);
		buffersList.add((ByteBuffer) ByteBuffer.allocate(name.limit() + 4 + object.limit()).put(name).putInt(object.limit()).put(object).flip());
	}

	@Override
	public SpS endVisit(){
		ByteBuffer buffer = ByteBuffer.allocate(4 + buffersList.stream().mapToInt(buff -> buff.limit()).sum());
		buffer.putInt(buffersList.size());
		for(ByteBuffer buff : buffersList){
			buffer.put(buff);
		}
		buffer.flip();
		return (SpS) buffer;
	}

}
