package code.elix_x.excomms.data;

import code.elix_x.excomms.reflection.ReflectionHelper.AClass;
import code.elix_x.excomms.reflection.ReflectionHelper.AField;
import code.elix_x.excomms.reflection.ReflectionHelper.Modifier;

public class DataCopier {

	public static <O> O copyData(O from, O to){
		return copyData(from, to, false);
	}

	@SuppressWarnings("unchecked")
	public static <O, V> O copyData(O from, O to, boolean propagateExceptions){
		AClass<O> clas = new AClass(to.getClass());
		for(AField<? super O, ?> field : clas.getFields()){
			if(!field.is(Modifier.STATIC)){
				((AField<? super O, V>) field).setAccessible(true).setFinal(false).set(to, ((AField<? super O, V>) field).get(from));
			}
		}
		return to;
	}

}
