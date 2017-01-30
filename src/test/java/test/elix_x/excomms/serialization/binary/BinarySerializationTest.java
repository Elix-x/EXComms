package test.elix_x.excomms.serialization.binary;

import static org.junit.Assert.*;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.junit.Test;

import code.elix_x.excomms.primitive.PrimitiveType;
import code.elix_x.excomms.serialization.Serializer;
import code.elix_x.excomms.serialization.binary.BinarySerializerMain;
import code.elix_x.excomms.serialization.binary.serializer.BinaryPrimitiveSerializer;

public class BinarySerializationTest {

	@Test
	public void test(){
		BinarySerializerMain serializer = new BinarySerializerMain(Arrays.stream(PrimitiveType.values()).flatMap(primitiveType -> Arrays.stream(new Serializer[]{new BinaryPrimitiveSerializer<>(primitiveType), new BinaryPrimitiveSerializer.ActualTypesWrapper<>(primitiveType)})).collect(Collectors.toList()));

		int i = 154515;
		ByteBuffer buffer = serializer.serialze(i);
		assertEquals("Serialization - deserialization of an integer failed", i, (int) serializer.deserialize(buffer, int.class));
	}

}
