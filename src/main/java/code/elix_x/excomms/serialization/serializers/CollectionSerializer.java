package code.elix_x.excomms.serialization.serializers;

import java.util.Collection;

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
	public boolean acceptsD(GenS o, Class<GenS> clas){
		return Collection.class.isAssignableFrom(clas);
	}

	@Override
	public GenS serialize(SerM serializerMain, SpD o){
		AClass<SpD> clas = new AClass<>((Class<SpD>) o.getClass());
		SVisitor<Object, GenS, GenS, SerM, Void> visitor = serializerMain.visitorS(clas.get());
		visitor.visit(serializerMain.serialze(clas.get()), null);
		for(Object oo : o){
			visitor.visit(serializerMain.serialze(oo), null);
		}
		return visitor.endVisit();
	}

	@Override
	public SpD deserialize(SerM serializerMain, GenS o){
		DVisitor<Object, SpD, GenS, SerM, Void> visitor = serializerMain.visitorD((Class<GenS>) o.getClass());
		SpD spD = visitor.startVisit((Class<SpD>) serializerMain.deserialize(visitor.visit(null), (Class<GenD>) Class.class));
		while(true){
			try{
				spD.add(visitor.visit(null));
			} catch(IllegalArgumentException e){
				break;
			}
		}
		return spD;
	}

}
