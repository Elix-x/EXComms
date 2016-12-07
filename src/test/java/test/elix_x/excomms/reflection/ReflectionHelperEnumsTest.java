package test.elix_x.excomms.reflection;

import static org.junit.Assert.*;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;

import code.elix_x.excomms.reflection.ReflectionHelper.AClass;
import code.elix_x.excomms.reflection.ReflectionHelper.AClass.AEnum;

public class ReflectionHelperEnumsTest {

	@Test
	public void test(){
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
