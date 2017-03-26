package test.elix_x.excomms.tick;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Rule;
import org.junit.Test;

import code.elix_x.excomms.tick.Ticker;
import test.elix_x.excomms.Repeat;
import test.elix_x.excomms.RepeatRule;

public class TickerTest {

	@Rule
	public RepeatRule repeat = new RepeatRule();

	@Test
	@Repeat(5)
	public void testNormal(){
		Random random = new Random();
		int ticks = 50;

		Ticker ticker = new Ticker().setTickTime(10 + random.nextInt(50)).setCatchUpMaxDeltaDefault().setWarningMessagesConsumer(message -> fail(message));
		ticker.start();
		long startTime = System.currentTimeMillis();
		for(int i = 0; i < ticks; i++){
			ticker.finishTick();
		}
		long endTime = System.currentTimeMillis();
		ticker.stop();
		assertEquals("Ticker did not tick requested amount of ticks", ticks, ticker.getTickCount());
		if(Math.abs((endTime - startTime) - ticks * ticker.getTickTime()) >= ticker.getTickTime())
			assertEquals("Ticker did not tick precisely", ticks * ticker.getTickTime(), endTime - startTime);
	}

	@Test
	@Repeat(5)
	public void testSpike(){
		Random random = new Random();
		int ticks = 50;
		int spike = 10 + random.nextInt(30);

		Ticker ticker = new Ticker().setTickTime(10 + random.nextInt(50)).setCatchUpMaxDeltaDefault().setWarningMessagesConsumer(message -> fail(message));
		ticker.start();
		long startTime = System.currentTimeMillis();
		for(int i = 0; i < ticks; i++){
			if(i == spike) try{
				Thread.sleep(ticker.getTickTime() * 2);
			} catch(InterruptedException e){

			}
			ticker.finishTick();
		}
		long endTime = System.currentTimeMillis();
		ticker.stop();
		assertEquals("Ticker did not tick requested amount of ticks", ticks, ticker.getTickCount());
		if(Math.abs((endTime - startTime) - ticks * ticker.getTickTime()) >= ticker.getTickTime())
			assertEquals("Ticker did not tick precisely", ticks * ticker.getTickTime(), endTime - startTime);
	}

	@Test
	@Repeat(10)
	public void testExtreme(){
		Random random = new Random();
		int ticks = 50;

		Ticker ticker = new Ticker().setTickTimeForce(1 + random.nextInt(4)).setCatchUpMaxDeltaDefault().setWarningMessagesConsumer(message -> fail(message));
		ticker.start();
		long startTime = System.currentTimeMillis();
		for(int i = 0; i < ticks; i++){
			ticker.finishTick();
		}
		long endTime = System.currentTimeMillis();
		ticker.stop();
		assertEquals("Ticker did not tick requested amount of ticks", ticks, ticker.getTickCount());
		if(Math.abs((endTime - startTime) - ticks * ticker.getTickTime()) > ticker.getTickTime() * 2)
			assertEquals("Ticker did not tick precisely", ticks * ticker.getTickTime(), endTime - startTime);
	}

}
