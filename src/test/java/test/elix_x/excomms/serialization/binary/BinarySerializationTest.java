package test.elix_x.excomms.serialization.binary;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;

import org.junit.Test;

import code.elix_x.excomms.primitive.PrimitiveType;
import code.elix_x.excomms.serialization.Serializer;
import code.elix_x.excomms.serialization.binary.BinarySerializerMain;
import code.elix_x.excomms.serialization.binary.serializer.BinaryPrimitiveSerializer;

public class BinarySerializationTest {

	@Test
	public void testPrimitives(){
		BinarySerializerMain serializer = new BinarySerializerMain(Arrays.stream(PrimitiveType.values()).flatMap(primitiveType -> Arrays.stream(new Serializer[]{new BinaryPrimitiveSerializer<>(primitiveType), new BinaryPrimitiveSerializer.ActualTypesWrapper<>(primitiveType)})).collect(Collectors.toList()));

		Random random = new Random();

		boolean b = random.nextBoolean();
		assertEquals("Serialization - deserialization of boolean failed", b, serializer.deserialize(serializer.serialze(b), boolean.class));

		byte by = (byte) random.nextInt(Byte.MAX_VALUE);
		assertEquals("Serialization - deserialization of byte failed", by, (byte) serializer.deserialize(serializer.serialze(by), byte.class));

		short s = (short) random.nextInt(Short.MAX_VALUE);
		assertEquals("Serialization - deserialization of short failed", s, (short) serializer.deserialize(serializer.serialze(s), short.class));

		int i = random.nextInt();
		assertEquals("Serialization - deserialization of integer failed", i, (int) serializer.deserialize(serializer.serialze(i), int.class));

		long l = random.nextLong();
		assertEquals("Serialization - deserialization of long failed", l, (long) serializer.deserialize(serializer.serialze(l), long.class));

		float f = random.nextFloat();
		assertEquals("Serialization - deserialization of float failed", f, (float) serializer.deserialize(serializer.serialze(f), float.class), 0);

		double d = random.nextDouble();
		assertEquals("Serialization - deserialization of double failed", d, (double) serializer.deserialize(serializer.serialze(d), double.class), 0);

		char c = (char) random.nextInt();
		assertEquals("Serialization - deserialization of char failed", c, (char) serializer.deserialize(serializer.serialze(c), char.class));
	}

}
