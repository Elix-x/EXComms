package code.elix_x.excomms.pipeline.list;

import java.util.ArrayList;
import java.util.List;

import code.elix_x.excomms.pipeline.PipelineElement;

public interface ListPipelineElement<I, O> extends PipelineElement<List<I>, List<O>> {

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
