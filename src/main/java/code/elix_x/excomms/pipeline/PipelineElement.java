package code.elix_x.excomms.pipeline;

import java.util.function.Function;

public interface PipelineElement<I, O> extends Function<I, O> {

	public O pipe(I i);

	@Override
	default O apply(I t){
		return pipe(t);
	}

	public static <I, O> PipelineElement<I, O> wrapper(Function<I, O> function){
		return in -> function.apply(in);
	}

}
