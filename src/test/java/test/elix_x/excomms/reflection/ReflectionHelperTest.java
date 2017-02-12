package test.elix_x.excomms.reflection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;

import code.elix_x.excomms.reflection.ReflectionHelper.AClass;
import code.elix_x.excomms.reflection.ReflectionHelper.AClass.AEnum;

public class ReflectionHelperTest {

	private String privateField = "This should not be here!";
	private final Object privateFinalField = "This should not be here!";
	private static final Object privateStaticFinalField = "This should not be here!";
	private boolean called = false;

	@Test
	public void testAccess(){
		String shouldBe = "This should!";
		AClass<ReflectionHelperTest> clas = new AClass<>(ReflectionHelperTest.class);

		clas.getDeclaredField("privateField").setAccessible(true).set(this, shouldBe);
		assertEquals("Private replacement was not successful", shouldBe, privateField);

		clas.getDeclaredField("privateFinalField").setAccessible(true).setFinal(false).set(this, shouldBe);
		assertEquals("Private final replacement was not successful", shouldBe, privateFinalField);

		clas.getDeclaredField("privateStaticFinalField").setAccessible(true).setFinal(false).set(null, shouldBe);
		assertEquals("Private static final replacement was not successful", shouldBe, privateStaticFinalField);

		clas.getDeclaredMethod(new String[]{"privateMethod"}).setAccessible(true).invoke(this);
		assertTrue("Private method call was successful", called);
	}

	@SuppressWarnings("unused")
	private void privateMethod(){
		called = true;
	}

	@Test
	public void testEnums(){
		AEnum<Animal> clas = new AClass<>(Animal.class).asEnum();
		Animal BUG = clas.createEnum("BUG");
		assertTrue(BUG.getClass() == Animal.class);
		assertFalse(ArrayUtils.contains(Animal.values(), BUG));
		AEnum<Insect> insect = new AClass<>(Insect.class).asEnum();
		Insect IBUG = insect.addEnum("IBUG", BUG);
		assertTrue(IBUG.getClass() == Insect.class);
		assertTrue(ArrayUtils.contains(Insect.values(), IBUG));
		Insect ANT = Insect.ANT;
		insect.removeEnum(ANT);
		assertTrue(ANT.getClass() == Insect.class);
		assertFalse(ArrayUtils.contains(Insect.values(), ANT));
	}

	enum Animal {

		DOG, CAT;

	}

	enum Insect {

		ANT(Animal.DOG), BUTTERFLY(Animal.CAT);

		final Animal predator;

		private Insect(Animal predator){
			this.predator = predator;
		}

	}

}
