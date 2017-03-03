package code.elix_x.excomms.serialization.generic.serializer;

import com.google.common.reflect.TypeToken;

import code.elix_x.excomms.reflection.ReflectionHelper.AClass;
import code.elix_x.excomms.serialization.Serializer;
import code.elix_x.excomms.serialization.SerializerMain;

public class ClassAsStringSerializer<GenS, SerM extends SerializerMain<Object, GenS, SerM>> implements Serializer<Object, Class<?>, GenS, GenS, SerM> {

	@Override
	public boolean acceptsS(Object o){
		return o instanceof Class;
	}

	@Override
	public boolean acceptsD(GenS o, TypeToken<Object> type){
		return type.getType() == Class.class;
	}

	@Override
	public GenS serialize(SerM serializerMain, Class<?> o){
		return serializerMain.serialize(o.getName());
	}

	@Override
	public Class<?> deserialize(SerM serializerMain, GenS o){
		return new AClass<>(serializerMain.deserialize(o, String.class)).get();
	}

}
