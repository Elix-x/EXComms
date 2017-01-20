package test.elix_x.excomms.reflection.pipeline;

import static org.junit.Assert.*;

import java.util.stream.Collectors;

import org.apache.commons.lang3.text.StrBuilder;
import org.junit.Test;

import com.google.common.collect.Lists;

import code.elix_x.excomms.pipeline.Pipeline;
import code.elix_x.excomms.pipeline.PipelineElement;
import code.elix_x.excomms.pipeline.SupplierPipelineElement;
import code.elix_x.excomms.pipeline.list.CollectorPipelineElement;
import code.elix_x.excomms.pipeline.list.Junction;
import code.elix_x.excomms.pipeline.list.ListPipelineElement;


public class PipelineTest {

	@Test
	public void test(){
		Pipeline<String, String> pipeline = new Pipeline<String, String>(
			new Junction<Object, Object>(
					SupplierPipelineElement.wrapper(() -> "\"/o\\"),
					PipelineElement.<String, String>wrapper(input -> new StrBuilder("\"").append(input).append("\"").reverse().build()),
					SupplierPipelineElement.wrapper(() -> "Or shall i say"),
					SupplierPipelineElement.wrapper(() -> "!"),
					PipelineElement.wrapper(input -> input),
					SupplierPipelineElement.wrapper(() -> "Hello")
			),
			ListPipelineElement.wrapperF(input -> Lists.reverse(input)),
			CollectorPipelineElement.wrapper(Collectors.joining(" ' ")),
			PipelineElement.<String, String>wrapper(input -> {
				return input.replace("' \"", "'\" ");
			})
		);
		assertEquals("Hello ' world ' ! ' Or shall i say '\" dlrow\" '\" /o\\", pipeline.pipe("world"));
		assertEquals("Hello ' Hello ' Hello ' dlrow ' ! ' Or shall i say '\" world\" '\" /o\\ ' ! ' Or shall i say '\" \\o/ \"'\" dlrow \"' yas i llahs rO ' ! ' world ' olleH\" '\" /o\\ ' ! ' Or shall i say '\" \\o/ \"'\" Hello ' dlrow ' ! ' Or shall i say '\" world \"'\" /o\\ \"' yas i llahs rO ' ! ' \\o/ \"'\" dlrow \"' yas i llahs rO ' ! ' world ' olleH ' olleH\" '\" /o\\", pipeline.pipe(pipeline.pipe(pipeline.pipe("dlrow"))));
	}

}