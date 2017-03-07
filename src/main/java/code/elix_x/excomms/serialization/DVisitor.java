package code.elix_x.excomms.serialization;

import java.util.function.Supplier;

import com.google.common.reflect.TypeToken;

public interface DVisitor<GenD, SpD extends GenD, GenS, SerM extends SerializerMain<GenD, GenS, SerM>, Args> {

	SpD startVisit(GenS object, Supplier<TypeToken<SpD>> clas);

	GenS visit(Args args);

}
