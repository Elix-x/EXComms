package code.elix_x.excomms.serialization;

public interface SVisitor<GenD, GenS, SpS extends GenS, SerM extends SerializerMain<GenD, GenS, SerM>, Args> {

	void visit(GenS object, Args args);

	SpS endVisit();

}
