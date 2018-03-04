package test.elix_x.excomms.optional;

import code.elix_x.excomms.optional.NullableOptional;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class NullableOptionalTest {

	@Test
	public void testBasicOps(){
		NullableOptional empty = NullableOptional.empty();
		assertFalse(empty.isPresent(), "Empty nullable optional was not empty");
		NullableOptional ofValue = NullableOptional.of("Hi!");
		assertTrue(ofValue.isPresent(), "NOpt of value was empty");
		assertEquals(ofValue.get(), "Hi!", "Value of NOpt of value did not match the original value");
		NullableOptional ofNull = NullableOptional.ofNull();
		assertTrue(ofNull.isPresent(), "NOpt of null was empty");
		assertEquals(ofNull.get(), null, "NOpt of null did not contain null value");

		assertEquals(NullableOptional.empty(), NullableOptional.ofNullable(null), "Empty did not match of nullable null");
		assertEquals(NullableOptional.ofNull(), NullableOptional.ofNullable(Optional.empty()), "Of null did not match of nullable empty");
		assertEquals(NullableOptional.of("Hi!"), NullableOptional.ofNullable(Optional.of("Hi!")), "Of [obj] did not match of nullable [obj]");
	}

}
