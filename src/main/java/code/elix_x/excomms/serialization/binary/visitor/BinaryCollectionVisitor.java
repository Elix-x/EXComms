package code.elix_x.excomms.serialization.binary.visitor;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.function.Supplier;

import com.google.common.reflect.TypeToken;

import code.elix_x.excomms.reflection.ReflectionHelper.AClass;
import code.elix_x.excomms.serialization.DVisitor;
import code.elix_x.excomms.serialization.SVisitor;
import code.elix_x.excomms.serialization.binary.BinarySerializerMain;

public class BinaryCollectionVisitor<T, SpS extends ByteBuffer, SpD extends Collection<T>> implements SVisitor<Object, ByteBuffer, SpS, BinarySerializerMain, Void>, DVisitor<Object, SpD, ByteBuffer, BinarySerializerMain, Void> {

	/*
	 * Deserialization
	 */

	private ByteBuffer workBuffer;
	private Queue<Integer> posQueue;

	@Override
	public SpD startVisit(ByteBuffer buffer, Supplier<TypeToken<SpD>> clas){
		this.workBuffer = buffer;
		this.posQueue = new LinkedList<>();
		int size = workBuffer.getInt();
		for(int i = 0; i < size; i++){
			int length = workBuffer.getInt();
			posQueue.add(workBuffer.position());
			workBuffer.position(workBuffer.position() + length);
		}
		return new AClass<>((Class<SpD>) clas.get().getRawType()).getDeclaredConstructor().setAccessible(true).newInstance();
	}

	@Override
	public ByteBuffer visit(Void args){
		workBuffer.position(posQueue.remove());
		return workBuffer;
	}

	/*
	 * Serialization
	 */

	private List<ByteBuffer> buffersList;

	@Override
	public void visit(ByteBuffer object, Void args){
		if(buffersList == null) buffersList = new ArrayList<>();
		buffersList.add(object);
	}

	@Override
	public SpS endVisit(){
		ByteBuffer buffer = ByteBuffer.allocate(4 + buffersList.stream().mapToInt(buff -> 4 + buff.limit()).sum());
		buffer.putInt(buffersList.size());
		for(ByteBuffer buff : buffersList){
			buffer.putInt(buff.limit());
			buffer.put(buff);
		}
		buffer.flip();
		return (SpS) buffer;
	}

}
