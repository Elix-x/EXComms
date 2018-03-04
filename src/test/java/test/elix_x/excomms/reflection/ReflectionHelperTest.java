package test.elix_x.excomms.reflection;

import java.lang.reflect.Method;

import org.apache.commons.lang3.ArrayUtils;

import code.elix_x.excomms.reflection.ReflectionHelper.AClass;
import code.elix_x.excomms.reflection.ReflectionHelper.AClass.AEnum;
import code.elix_x.excomms.reflection.ReflectionHelper.AClass.AInterface;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ReflectionHelperTest {

	private String privateField = "This should not be here!";
	private final Object privateFinalField = "This should not be here!";
	private static final Object privateStaticFinalField = "This should not be here!";
	private boolean called = false;

	@Test
	public void testAccess(){
		String shouldBe = "This should!";
		AClass<ReflectionHelperTest> clas = new AClass<>(ReflectionHelperTest.class);

		clas.getDeclaredField("privateField").orElseThrow(IllegalArgumentException::new).setAccessible(true).set(this, shouldBe);
		assertEquals(shouldBe, privateField, "Private replacement was not successful");

		clas.getDeclaredField("privateFinalField").orElseThrow(IllegalArgumentException::new).setAccessible(true).setFinal(false).set(this, shouldBe);
		assertEquals(shouldBe, privateFinalField, "Private final replacement was not successful");

		clas.getDeclaredField("privateStaticFinalField").orElseThrow(IllegalArgumentException::new).setAccessible(true).setFinal(false).set(null, shouldBe);
		assertEquals(shouldBe, privateStaticFinalField, "Private static final replacement was not successful");

		clas.getDeclaredMethod(new String[]{"privateMethod"}).orElseThrow(IllegalArgumentException::new).setAccessible(true).invoke(this);
		assertTrue(called, "Private method call was successful");
	}

	@SuppressWarnings("unused")
	private void privateMethod(){
		called = true;
	}

	@Test
	public void testEnums(){
		AEnum<Animal> clas = new AClass<>(Animal.class).asEnum();
		Animal BUG = clas.createEnum("BUG").orElse(null);
		assertTrue(BUG.getClass() == Animal.class);
		assertFalse(ArrayUtils.contains(Animal.values(), BUG));
		AEnum<Insect> insect = new AClass<>(Insect.class).asEnum();
		Insect IBUG = insect.addEnum("IBUG", BUG).orElse(null);
		assertTrue(IBUG.getClass() == Insect.class);
		assertTrue(ArrayUtils.contains(Insect.values(), IBUG));
		Insect ANT = Insect.ANT;
		insect.removeEnum(ANT);
		assertTrue(ANT.getClass() == Insect.class);
		assertFalse(ArrayUtils.contains(Insect.values(), ANT));
	}

	enum Animal {

		DOG, CAT

	}

	enum Insect {

		ANT(Animal.DOG), BUTTERFLY(Animal.CAT);

		final Animal predator;

		Insect(Animal predator){
			this.predator = predator;
		}

	}

	@Test
	public void testInterfaces(){
		AInterface<Plant> iface = new AClass<>(Plant.class).asInterface();
		Plant bamboo = iface.proxy((Object proxy, Method method, Object[] args) -> {
			return (boolean) args[0] ? 5 : 1;
		});
		Plant poisonBerries = iface.proxy((Object proxy, Method method, Object[] args) -> {
			boolean sunny = (boolean) args[0];
			switch(method.getName()){
				case "getGrowthRate":
					return sunny ? 2 : 0;
				case "isPoisonous":
					return sunny;
				default:
					return null;
			}
		}, new AClass<>(Food.class).asInterface());
		assertEquals(bamboo.getGrowthRate(true), 5);
		assertEquals(bamboo.getGrowthRate(false), 1);
		assertEquals(poisonBerries.getGrowthRate(true), 2);
		assertEquals(poisonBerries.getGrowthRate(false), 0);
		assertTrue(poisonBerries instanceof Food);
		assertEquals(((Food) poisonBerries).isPoisonous(true), true);
		assertEquals(((Food) poisonBerries).isPoisonous(false), false);
	}

	interface Plant {

		int getGrowthRate(boolean sunny);

	}

	interface Food {

		boolean isPoisonous(boolean sunny);

	}

}
