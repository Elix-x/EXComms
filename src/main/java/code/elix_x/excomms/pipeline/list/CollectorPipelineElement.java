package code.elix_x.excomms.pipeline.list;

import java.util.List;
import java.util.stream.Collector;

import code.elix_x.excomms.pipeline.PipelineElement;

/**
 * A mutable reduction operation that accumulates input elements into a mutable result container, optionally transforming the accumulated result into a final representation after all input elements have been processed. By itself it is <i>similar</i> to {@linkplain Collector}, but cannot be used as one. Along with default collector purposes, it can also be used to terminate the list pipeline (piping multiple elements at once) and return to the basic pipeline (piping single elements).
 * 
 * @author elix_x
 *
 * @param <I>
 * @param <O>
 */
public interface CollectorPipelineElement<I, O> extends PipelineElement<List<I>, O> {

	/**
	 * Wraps the given {@linkplain Collector} as a {@linkplain CollectorPipelineElement}.
	 * 
	 * @param collector
	 *            - {@linkplain Collector} to wrap
	 * @return {@linkplain CollectorPipelineElement} wrapper of the collector
	 */
	public static <I, O> CollectorPipelineElement<I, O> wrapper(Collector<I, ?, O> collector){
		return in -> in.stream().collect(collector);
	}

}
