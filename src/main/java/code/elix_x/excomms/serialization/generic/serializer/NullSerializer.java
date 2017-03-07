package code.elix_x.excomms.serialization.generic.serializer;

import java.util.function.Function;
import java.util.function.Supplier;

import com.google.common.reflect.TypeToken;

import code.elix_x.excomms.serialization.Serializer;
import code.elix_x.excomms.serialization.SerializerMain;

public class NullSerializer<GenD, SpD extends GenD, GenS, SpS extends GenS, SerM extends SerializerMain<GenD, GenS, SerM>> implements Serializer<GenD, SpD, GenS, SpS, SerM> {

	private final Function<GenS, Boolean> serNullCheck;
	private final Supplier<SpS> serNull;

	public NullSerializer(Function<GenS, Boolean> serNullCheck, Supplier<SpS> serNull){
		this.serNullCheck = serNullCheck;
		this.serNull = serNull;
	}

	@Override
	public boolean acceptsS(GenD o){
		return o == null;
	}

	@Override
	public boolean acceptsD(GenS o, TypeToken<GenD> type){
		return serNullCheck.apply(o);
	}

	@Override
	public SpS serialize(SerM serializerMain, SpD o){
		return serNull.get();
	}

	@Override
	public SpD deserialize(SerM serializerMain, SpS o){
		return null;
	}

}