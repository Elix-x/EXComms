package code.elix_x.excomms.serialization;

public interface Serializer<GenD, SpD extends GenD, GenS, SpS extends GenS, SerM extends SerializerMain<GenD, GenS, SerM>> {

	boolean acceptsS(GenD o);

	boolean acceptsD(GenS o, Class<GenS> clas);

	SpS serialize(SerM serializerMain, SpD o);

	SpD deserialize(SerM serializerMain, SpS o);

}
