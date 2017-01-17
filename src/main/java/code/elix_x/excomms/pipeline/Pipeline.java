package code.elix_x.excomms.pipeline;

import java.util.Collection;

import com.google.common.collect.ImmutableList;

public class Pipeline<I, O> implements PipelineElement<I, O> {

	private final ImmutableList<PipelineElement<?, ?>> elements;

	public Pipeline(PipelineElement<?, ?>... elements){
		this.elements = ImmutableList.copyOf(elements);
	}

	public Pipeline(Collection<PipelineElement<?, ?>> elements){
		this.elements = ImmutableList.copyOf(elements);
	}

	@SuppressWarnings("unchecked")
	@Override
	public O pipe(I i){
		Object o = i;
		for(PipelineElement<?, ?> element : elements){
			o = ((PipelineElement<Object, Object>) element).pipe(o);
		}
		return (O) o;
	}

}
