package test.elix_x.excomms.tick;

import code.elix_x.excomms.tick.Ticker;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class TickerTest {

	@RepeatedTest(5)
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
		assertEquals(ticks, ticker.getTickCount(), "Ticker did not tick requested amount of ticks");
		if(Math.abs((endTime - startTime) - ticks * ticker.getTickTime()) >= ticker.getTickTime())
			assertEquals(ticks * ticker.getTickTime(), endTime - startTime, "Ticker did not tick precisely");
	}

	@Test
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
		assertEquals(ticks, ticker.getTickCount(), "Ticker did not tick requested amount of ticks");
		if(Math.abs((endTime - startTime) - ticks * ticker.getTickTime()) >= ticker.getTickTime())
			assertEquals(ticks * ticker.getTickTime(), endTime - startTime, "Ticker did not tick precisely");
	}

	@Test
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
		assertEquals(ticks, ticker.getTickCount(), "Ticker did not tick requested amount of ticks");
		if(Math.abs((endTime - startTime) - ticks * ticker.getTickTime()) > ticker.getTickTime() * 2)
			assertEquals(ticks * ticker.getTickTime(), endTime - startTime, "Ticker did not tick precisely");
	}

}
