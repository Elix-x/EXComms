package code.elix_x.excomms.serialization;

import com.google.common.collect.ImmutableList;

public class Serializer<S, H extends Serializer<S, H>> {

	private final ImmutableList<SerializationVisitor<?, ? extends S, H>> visitors;
	
	public Serializer(SerializationVisitor<?, ? extends S, H> visitors){
		this.visitors = ImmutableList.of(visitors);
	}
	
	public <T, R extends S> R serialze(T t){
		for(SerializationVisitor visitor : visitors){
			if(visitor.acceptsS(t)) return (R) visitor.serialize(this, t);
		}
		throw new IllegalArgumentException("None of visitors could visit " + t);
	}
	
	public <T extends S, R> R deserialize(T t){
		for(SerializationVisitor visitor : visitors){
			if(visitor.acceptsD(t)) return (R) visitor.deserialize(this, t);
		}
		throw new IllegalArgumentException("None of visitors could visit " + t);
	}
	
}
