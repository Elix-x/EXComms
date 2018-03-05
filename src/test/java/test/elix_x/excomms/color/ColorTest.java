package test.elix_x.excomms.color;

import code.elix_x.excomms.color.HSBA;
import code.elix_x.excomms.color.RGBA;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class ColorTest {

	private Random random = new Random();

	@Test
	public void testRGBAFI(){
		RGBA rgbaf = new RGBA(0.5f, 0.5f, 1f, 0.75f);
		rgbaf = rgbaf.setGF(0.6371f);
		assertEquals(0.6371f, rgbaf.getGF(), "Set F failed");
		rgbaf.setRF(0.6f);
		assertEquals(153, rgbaf.getRI(), "Get I failed");
		rgbaf.setAI(204);
		assertEquals(0.8f, rgbaf.getAF(), "Set I failed");

		assertEquals(new RGBA(0f, 0.4f, 0.8f, 1f), new RGBA(-2, 102, 204, 279), "Int capping failed");
	}

	@Test
	public void testColorConversion(){
		for(int i = 0; i < 100; i++){
			RGBA rgba = new RGBA(random.nextFloat(), random.nextFloat(), random.nextFloat(), random.nextFloat());
			assertEquals(rgba, rgba.toHSBA().toRGBA(), "Color conversion failed");
		}
	}

	@Test
	public void testHSBAH(){
		for(int i = 0; i < 100; i++){
			double hr = random.nextDouble();
			assertEquals(hr, new HSBA(0, 0, 0).setHRad(hr + (-10 + random.nextInt(21)) * Math.PI * 2).getHRad(), 1E-5,"H Wrap (radians) failed");
		}
	}

}
