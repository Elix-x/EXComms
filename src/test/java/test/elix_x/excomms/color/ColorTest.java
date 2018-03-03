package test.elix_x.excomms.color;

import code.elix_x.excomms.color.HSBA;
import code.elix_x.excomms.color.RGBA;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class ColorTest {

	private Random random = new Random();

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
