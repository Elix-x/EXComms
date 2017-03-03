package code.elix_x.excomms.serialization.generic.serializer;

import java.util.Collections;

import com.google.common.collect.ImmutableList;
import com.google.common.reflect.TypeToken;

import code.elix_x.excomms.reflection.ReflectionHelper;
import code.elix_x.excomms.reflection.ReflectionHelper.AClass;
import code.elix_x.excomms.reflection.ReflectionHelper.AField;
import code.elix_x.excomms.reflection.ReflectionHelper.Modifier;
import code.elix_x.excomms.serialization.DVisitor;
import code.elix_x.excomms.serialization.SVisitor;
import code.elix_x.excomms.serialization.Serializer;
import code.elix_x.excomms.serialization.SerializerMain;

public class ObjectSerializer<GenD, GenS, SerM extends SerializerMain<GenD, GenS, SerM>> implements Serializer<GenD, GenD, GenS, GenS, SerM>{

	private static final String CLASS = "#CLASS#";
	
	private final ImmutableList<ReflectionHelper.Modifier> blackModifiers;

	public ObjectSerializer(Modifier... modifiers){
		blackModifiers = ImmutableList.copyOf(modifiers);
	}

	public ObjectSerializer(){
		this(Modifier.STATIC, Modifier.TRANSIENT);
	}

	@Override
	public boolean acceptsS(GenD o){
		return true;
	}

	@Override
	public boolean acceptsD(GenS o, TypeToken<GenD> type){
		return true;
	}

	@Override
	public GenS serialize(SerM serializerMain, GenD o){
		AClass<GenD> clas = new AClass<>((Class<GenD>) o.getClass());
		SVisitor<GenD, GenS, GenS, SerM, String> visitor = serializerMain.visitorS(clas.get());
		visitor.visit(serializerMain.serialize((GenD) clas.get()), CLASS);
		for(AField<? super GenD, ?> field : clas.getFields()){
			if(Collections.disjoint(blackModifiers, field.modifiers())){
				visitor.visit(serializerMain.serialize((GenD) field.setAccessible(true).get(o)), field.get().getName());
			}
		}
		return visitor.endVisit();
	}

	@Override
	public GenD deserialize(SerM serializerMain, GenS o){
		DVisitor<GenD, GenD, GenS, SerM, String> visitor = serializerMain.visitorD((Class<GenS>) o.getClass()); 
		GenD genD = visitor.startVisit(o, (Class<GenD>) serializerMain.deserialize(visitor.visit(CLASS), (Class<GenD>) Class.class));
		AClass<GenD> clas = new AClass<GenD>((Class<GenD>) genD.getClass());
		for(AField<? super GenD, ?> field : clas.getFields()){
			if(Collections.disjoint(blackModifiers, field.modifiers())){
				field.setAccessible(true).setFinal(false).set(genD, serializerMain.deserialize(visitor.visit(field.get().getName()), (Class<GenD>) field.get().getType()));
			}
		}
		return genD;
	}

}
