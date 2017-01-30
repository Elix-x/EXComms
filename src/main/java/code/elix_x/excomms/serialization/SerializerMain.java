package code.elix_x.excomms.serialization;

import com.google.common.collect.ImmutableList;
import com.google.common.reflect.TypeToken;

public abstract class SerializerMain<GenD, GenS, SerM extends SerializerMain<GenD, GenS, SerM>> {

	private final ImmutableList<Serializer<GenD, ?, GenS, ?, SerM>> serializers;

	public SerializerMain(Serializer<GenD, ?, GenS, ?, SerM>... serializers){
		this.serializers = ImmutableList.copyOf(serializers);
	}

	public SerializerMain(Iterable<Serializer<GenD, ?, GenS, ?, SerM>> serializers){
		this.serializers = ImmutableList.copyOf(serializers);
	}

	public abstract <SpS extends GenS, SpD extends GenD, Args> SVisitor<GenD, GenS, SpS, SerM, Args> visitorS(Class<SpD> clas);

	public abstract <SpS extends GenS, SpD extends GenD, Args> DVisitor<GenD, SpD, GenS, SerM, Args> visitorD(Class<SpS> clas);

	public <SpS extends GenS, SpD extends GenD> SpS serialze(GenD t){
		for(Serializer serializer : serializers){
			if(serializer.acceptsS(t)) return (SpS) serializer.serialize(this, t);
		}
		throw new IllegalArgumentException("None of visitors could visit " + t);
	}
	
	public <SpS extends GenS, SpD extends GenD> SpD deserialize(SpS t, TypeToken<SpD> type){
		for(Serializer serializer : serializers){
			if(serializer.acceptsD(t, type)) return (SpD) serializer.deserialize(this, t);
		}
		throw new IllegalArgumentException("None of visitors could visit " + t);
	}
	
	public <SpS extends GenS, SpD extends GenD> SpD deserialize(SpS t, Class<SpD> clas){
		return deserialize(t, TypeToken.of(clas));
	}

}
