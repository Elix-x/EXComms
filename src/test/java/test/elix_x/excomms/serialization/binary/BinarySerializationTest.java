package test.elix_x.excomms.serialization.binary;

import static org.junit.Assert.*;

import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.reflect.TypeToken;

import code.elix_x.excomms.primitive.PrimitiveType;
import code.elix_x.excomms.serialization.binary.BinarySerializerMain;
import code.elix_x.excomms.serialization.binary.serializer.BinaryNullSerializer;
import code.elix_x.excomms.serialization.binary.serializer.BinaryPrimitiveSerializer;
import code.elix_x.excomms.serialization.binary.serializer.BinaryStringSerializer;
import code.elix_x.excomms.serialization.generic.serializer.ClassAsStringSerializer;
import code.elix_x.excomms.serialization.generic.serializer.CollectionSerializer;
import code.elix_x.excomms.serialization.generic.serializer.ObjectSerializer;

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
	public void testNull(){
		BinarySerializerMain serializer = new BinarySerializerMain(new BinaryNullSerializer<>());

		ByteBuffer buffer = serializer.serialize(null);
		assertTrue("Serialization of null gone wrong", buffer != null && buffer.limit() == 0);
		assertNull("Deserialization of null failed", serializer.deserialize(buffer, TypeToken.of(Object.class)));
	}

	@Test
	public void testClass(){
		BinarySerializerMain serializer = new BinarySerializerMain(new BinaryStringSerializer(), new ClassAsStringSerializer<>());

		assertEquals("Serialization - deserialization of class failed", this.getClass(), serializer.deserialize(serializer.serialize(this.getClass()), Class.class));
	}

	@Test
	public void testSimpleObjects(){
		BinarySerializerMain serializer = new BinarySerializerMain(new BinaryStringSerializer(), new ClassAsStringSerializer<>(), new ObjectSerializer<>());

		SimpleObject object = new SimpleObject();
		assertEquals("Serialization - deserialization of simple object failed", object, serializer.deserialize(serializer.serialize(object), SimpleObject.class));
	}

	@Test
	public void testCollections(){
		BinarySerializerMain serializer = new BinarySerializerMain(new BinaryPrimitiveSerializer<>(PrimitiveType.INT), new BinaryPrimitiveSerializer.ActualTypesWrapper<>(PrimitiveType.INT), new BinaryStringSerializer(), new ClassAsStringSerializer<>(), new CollectionSerializer<>());

		List<Integer> list = Lists.newArrayList(5, 3, 1);
		Object result = serializer.deserialize(serializer.serialize(list), new TypeToken<List<Integer>>(){});
		assertEquals("Serialization - deserialization of list failed", list, result);
		assertTrue("Serialization - deserialization of list messed the class up", list.getClass() == result.getClass());

		Set<Integer> set = Sets.newHashSet(4, 0, 2);
		result = serializer.deserialize(serializer.serialize(set), new TypeToken<Set<Integer>>(){});
		assertEquals("Serialization - deserialization of set failed", set, result);
		assertTrue("Serialization - deserialization of set messed the class up", set.getClass() == result.getClass());

		Queue<Integer> queue = new LinkedList<Integer>();
		result = serializer.deserialize(serializer.serialize(queue), new TypeToken<Queue<Integer>>(){});
		assertEquals("Serialization - deserialization of queue failed", queue, result);
		assertTrue("Serialization - deserialization of queue messed the class up", queue.getClass() == result.getClass());
	}

	public static class SimpleObject {

		private String aString = "this is a string";

		@Override
		public int hashCode(){
			final int prime = 31;
			int result = 1;
			result = prime * result + ((aString == null) ? 0 : aString.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj){
			if(this == obj) return true;
			if(obj == null) return false;
			if(getClass() != obj.getClass()) return false;
			SimpleObject other = (SimpleObject) obj;
			if(aString == null){
				if(other.aString != null) return false;
			} else if(!aString.equals(other.aString)) return false;
			return true;
		}

	}

}
