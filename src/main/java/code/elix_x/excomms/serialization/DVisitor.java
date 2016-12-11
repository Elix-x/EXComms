package code.elix_x.excomms.serialization;

public interface DVisitor<GenD, SpD extends GenD, GenS, SerM extends SerializerMain<GenD, GenS, SerM>, Args> {

	SpD startVisit(Class<SpD> clas);
	
	GenS visit(Args args);

}
