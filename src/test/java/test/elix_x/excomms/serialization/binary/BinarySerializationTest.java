package test.elix_x.excomms.serialization.binary;

import static org.junit.Assert.*;

import java.nio.ByteBuffer;
import java.util.Random;

import org.junit.Test;

import code.elix_x.excomms.serialization.binary.BinarySerializerMain;
import code.elix_x.excomms.serialization.binary.serializer.BinaryPrimitiveSerializer;
import code.elix_x.excomms.serialization.binary.serializer.BinaryStringSerializer;
import code.elix_x.excomms.serialization.generic.serializer.ClassAsStringSerializer;

public class BinarySerializationTest {

	@Test
	public void testPrimitives(){
		BinarySerializerMain serializer = new BinarySerializerMain(BinaryPrimitiveSerializer.ALL);

		Random random = new Random();

		boolean b = random.nextBoolean();
		assertEquals("Serialization - deserialization of boolean failed", b, serializer.deserialize(serializer.serialize(b), boolean.class));

		byte by = (byte) random.nextInt(Byte.MAX_VALUE);
		assertEquals("Serialization - deserialization of byte failed", by, (byte) serializer.deserialize(serializer.serialize(by), byte.class));

		short s = (short) random.nextInt(Short.MAX_VALUE);
		assertEquals("Serialization - deserialization of short failed", s, (short) serializer.deserialize(serializer.serialize(s), short.class));

		int i = random.nextInt();
		assertEquals("Serialization - deserialization of integer failed", i, (int) serializer.deserialize(serializer.serialize(i), int.class));

		long l = random.nextLong();
		assertEquals("Serialization - deserialization of long failed", l, (long) serializer.deserialize(serializer.serialize(l), long.class));

		float f = random.nextFloat();
		assertEquals("Serialization - deserialization of float failed", f, (float) serializer.deserialize(serializer.serialize(f), float.class), 0);

		double d = random.nextDouble();
		assertEquals("Serialization - deserialization of double failed", d, (double) serializer.deserialize(serializer.serialize(d), double.class), 0);

		char c = (char) random.nextInt();
		assertEquals("Serialization - deserialization of char failed", c, (char) serializer.deserialize(serializer.serialize(c), char.class));
	}
	
	@Test
	public void testString(){
		BinarySerializerMain serializer = new BinarySerializerMain(new BinaryStringSerializer());
		
		String s1 = "o/";
		ByteBuffer b1 = serializer.serialize(s1);
		byte[] arr = new byte[b1.limit()];
		b1.get(arr);
		assertArrayEquals("Serialization of string failed", new byte[]{0, 0, 0, 2, 0x6f, 0x2f}, arr);
		b1.rewind();
		assertEquals("Deserialization of string failed", s1, serializer.deserialize(b1, String.class));
		
		String s2 = "This IS A load of gibberish text.\nMight as well throw some weird characters in.\nǢȌёΨ֍௵ᔵᜨ\n☭☯☢∞❄♫";
		assertEquals("Serialization - deserialization of a very long gibberish string failed", s2, serializer.deserialize(serializer.serialize(s2), String.class));
	}
	
	@Test
	public void testClass(){
		BinarySerializerMain serializer = new BinarySerializerMain(new BinaryStringSerializer(), new ClassAsStringSerializer<>());
		
		assertEquals("Serialization - deserialization of class failed", this.getClass(), serializer.deserialize(serializer.serialize(this.getClass()), Class.class));
	}

}
