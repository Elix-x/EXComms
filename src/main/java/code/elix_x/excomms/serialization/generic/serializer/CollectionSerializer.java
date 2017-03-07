package code.elix_x.excomms.serialization.generic.serializer;

import java.util.Collection;

import com.google.common.reflect.TypeToken;

import code.elix_x.excomms.reflection.ReflectionHelper.AClass;
import code.elix_x.excomms.serialization.DVisitor;
import code.elix_x.excomms.serialization.SVisitor;
import code.elix_x.excomms.serialization.Serializer;
import code.elix_x.excomms.serialization.SerializerMain;

public class CollectionSerializer<GenD extends Object, SpD extends Collection, GenS, SerM extends SerializerMain<Object, GenS, SerM>> implements Serializer<Object, SpD, GenS, GenS, SerM> {

	@Override
	public boolean acceptsS(Object o){
		return o instanceof Collection<?>;
	}

	@Override
	public boolean acceptsD(GenS o, TypeToken<Object> type){
		return Collection.class.isAssignableFrom(type.getRawType());
	}

	@Override
	public GenS serialize(SerM serializerMain, SpD o){
		AClass<SpD> clas = new AClass<>((Class<SpD>) o.getClass());
		SVisitor<Object, GenS, GenS, SerM, Void> visitor = serializerMain.visitorS(TypeToken.of(clas.get()));
		visitor.visit(serializerMain.serialize(clas.get()), null);
		for(Object oo : o){
			visitor.visit(serializerMain.serialize(oo.getClass()), null);
			visitor.visit(serializerMain.serialize(oo), null);
		}
		return visitor.endVisit();
	}

	@Override
	public SpD deserialize(SerM serializerMain, GenS o){
		DVisitor<Object, SpD, GenS, SerM, Void> visitor = serializerMain.visitorD(new TypeToken<SpD>(getClass()){});
		SpD spD = visitor.startVisit(o, () -> TypeToken.of((Class<SpD>) serializerMain.deserialize(visitor.visit(null), (Class<GenD>) Class.class)));
		while(true){
			try{
				TypeToken type = TypeToken.of(serializerMain.deserialize(visitor.visit(null), Class.class));
				spD.add(serializerMain.deserialize(visitor.visit(null), type));
			} catch(RuntimeException e){
				break;
			}
		}
		return spD;
	}

}
