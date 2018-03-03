package test.elix_x.excomms.pipeline;

import code.elix_x.excomms.pipeline.Pipeline;
import code.elix_x.excomms.pipeline.PipelineElement;
import code.elix_x.excomms.pipeline.SupplierPipelineElement;
import code.elix_x.excomms.pipeline.list.CollectorPipelineElement;
import code.elix_x.excomms.pipeline.list.Junction;
import code.elix_x.excomms.pipeline.list.ListPipelineElement;
import code.elix_x.excomms.pipeline.list.ToListTransformersPipelineElement;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.text.StrBuilder;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

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

		assertEquals("Where am i?", new Pipeline<>(new ToListTransformersPipelineElement.Iterator<>(), CollectorPipelineElement.wrapper(Collectors.joining(" "))).pipe(Iterators.forArray("Where", "am", "i?")));
		assertEquals("I am back.", new Pipeline<>(new ToListTransformersPipelineElement.Iterable<>(), ListPipelineElement.wrapperE(PipelineElement.wrapper(o -> o.toString())), CollectorPipelineElement.wrapper(Collectors.joining(" "))).pipe(new File("I/am/back.").toPath()));
	}

}
