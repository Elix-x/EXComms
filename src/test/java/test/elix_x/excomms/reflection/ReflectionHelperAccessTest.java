package test.elix_x.excomms.reflection;

import static org.junit.Assert.*;

import org.junit.Test;

import code.elix_x.excomms.reflection.ReflectionHelper.AClass;

public class ReflectionHelperAccessTest {

	private String privateField = "This should not be here!";
	private final Object privateFinalField = "This should not be here!";
	private boolean called = false;

	@Test
	public void test(){
		String shouldBe = "This should!";
		AClass<ReflectionHelperAccessTest> clas = new AClass<>(ReflectionHelperAccessTest.class);

		clas.getDeclaredField("privateField").setAccessible(true).set(this, shouldBe);
		assertEquals("Private replacement was not successful", shouldBe, privateField);

		clas.getDeclaredField("privateFinalField").setAccessible(true).setFinal(false).set(this, shouldBe);
		assertEquals("Private final replacement was not successful", shouldBe, privateFinalField);

		clas.getDeclaredMethod(new String[]{"privateMethod"}).setAccessible(true).invoke(this);
		assertTrue("Private method call was successful", called);
	}

	@SuppressWarnings("unused")
	private void privateMethod(){
		called = true;
	}

}
