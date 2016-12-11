package code.elix_x.excomms.serialization;

import com.google.common.collect.ImmutableList;

public abstract class SerializerMain<GenD, GenS, SerM extends SerializerMain<GenD, GenS, SerM>> {

	private final ImmutableList<Serializer<GenD, ?, GenS, ?, SerM>> visitors;

	public SerializerMain(Serializer<GenD, ?, GenS, ?, SerM>... visitors){
		this.visitors = ImmutableList.copyOf(visitors);
	}

	public abstract <SpS extends GenS, SpD extends GenD, Args> SVisitor<GenD, GenS, SpS, SerM, Args> visitorS(Class<SpD> clas);

	public abstract <SpS extends GenS, SpD extends GenD, Args> DVisitor<GenD, SpD, GenS, SerM, Args> visitorD(Class<SpS> clas);

	public <SpS extends GenS, SpD extends GenD> SpS serialze(GenD t){
		for(Serializer visitor : visitors){
			if(visitor.acceptsS(t)) return (SpS) visitor.serialize(this, t);
		}
		throw new IllegalArgumentException("None of visitors could visit " + t);
	}

	public <SpS extends GenS, SpD extends GenD> SpD deserialize(SpS t, Class<SpD> clas){
		for(Serializer visitor : visitors){
			if(visitor.acceptsD(t, clas)) return (SpD) visitor.deserialize(this, t);
		}
		throw new IllegalArgumentException("None of visitors could visit " + t);
	}

}
