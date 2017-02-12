package code.elix_x.excomms.pipeline;

import java.util.function.Function;

/**
 * A basic element of a pipeline. By itself it acts as a function (and can be used as one). But it is intended to be used in a {@linkplain Pipeline}.
 * 
 * @author elix_x
 *
 * @param <I>
 *            Input
 * @param <O>
 *            Output
 */
public interface PipelineElement<I, O> extends Function<I, O> {

	/**
	 * Pipes the input through this element resulting in output. Action is equivalent to {@link Function#apply(Object)};
	 * 
	 * @param in
	 *            - input
	 * @return output
	 */
	public O pipe(I in);

	@Override
	default O apply(I in){
		return pipe(in);
	}

	/**
	 * Wraps the given {@linkplain Function} as a {@linkplain PipelineElement}.
	 * 
	 * @param function
	 *            - {@linkplain Function} to wrap
	 * @return {@linkplain PipelineElement} wrapper of the function
	 */
	public static <I, O> PipelineElement<I, O> wrapper(Function<I, O> function){
		return in -> function.apply(in);
	}

}
