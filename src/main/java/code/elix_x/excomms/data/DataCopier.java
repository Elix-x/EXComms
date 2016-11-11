package code.elix_x.excomms.data;

import code.elix_x.excomms.reflection.ReflectionHelper.AClass;
import code.elix_x.excomms.reflection.ReflectionHelper.AField;
import code.elix_x.excomms.reflection.ReflectionHelper.Modifier;

/**
 * Allows to perform a copy (shallow) of all data from one object to another object of the same type.
 * 
 * @author Elix_x
 *
 */
public class DataCopier {

	/**
	 * Copies all data from one object to another object with the same super type. Supports private and final values.
	 * 
	 * @param from
	 *            original object
	 * @param to
	 *            where to copy data
	 * @return to (where data was copied)
	 */
	@SuppressWarnings("unchecked")
	public static <O, O1 extends O, V> O copy(O from, O1 to){
		AClass<O> clas = new AClass(from.getClass());
		for(AField<? super O, ?> field : clas.getFields()){
			if(!field.is(Modifier.STATIC)){
				field.setAccessible(true).setFinal(false).set(to, field.get(from));
			}
		}
		return to;
	}

}
