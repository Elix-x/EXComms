package code.elix_x.excomms.serialization;

public interface SerializationVisitor<T, S, H extends Serializer<S, H>> {

	boolean acceptsS(Object o);

	<Ss extends S> boolean acceptsD(Ss r, Class clas);

	<Ss extends S> Ss serialize(H h, T t);

	<Tt extends T> Tt deserialize(H h, S s, Class<Tt> clas, Class... classes);

}
