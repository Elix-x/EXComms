package code.elix_x.excomms.pipeline.list;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import code.elix_x.excomms.pipeline.PipelineElement;

/**
 * This is a more advanced element of a pipeline. It allows to pipe multiple elements through at once.
 * 
 * @author elix_x
 *
 * @param <I>
 *            Input(s)
 * @param <O>
 *            Output(s)
 */
public interface ListPipelineElement<I, O> extends PipelineElement<List<I>, List<O>> {

	/**
	 * Wraps the given {@linkplain Function} as a {@linkplain ListPipelineElement}.
	 * 
	 * @param function
	 *            - {@linkplain Function} to wrap
	 * @return {@linkplain ListPipelineElement} wrapper of the function
	 */
	public static <I, O> ListPipelineElement<I, O> wrapper(Function<List<I>, List<O>> function){
		return in -> function.apply(in);
	}

	/**
	 * Wraps the given {@linkplain PipelineElement} into a {@linkplain ListPipelineElement} in order to allow it to accept multiple elements at once. <br>
	 * Note: This method can be used to wrap {@linkplain ListPipelineElement}s and even stack-wrap them. In doing so, you can obtain pipeline elements which accept list of lists of lists of lists of...
	 * 
	 * @param element
	 *            - {@linkplain PipelineElement} to wrap
	 * @return {@linkplain ListPipelineElement} wrapper of the pipeline element
	 */
	public static <I, O> ListPipelineElement<I, O> wrapper(PipelineElement<I, O> element){
		return in -> {
			List<O> out = new ArrayList<>();
			for(I i : in){
				out.add(element.pipe(i));
			}
			return out;
		};
	}

}
