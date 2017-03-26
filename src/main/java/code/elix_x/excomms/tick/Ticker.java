package code.elix_x.excomms.tick;

import java.lang.ref.WeakReference;
import java.util.function.Consumer;

/**
 * Ticker allows precise sequencing (ticking) for code running certain action with precise frequency - for example, executing an action every 20 ms. The time of the action can very and ticker will make sure it begins every 20 ms.<br>
 * If a spike occurs (action takes too much time), ticker will catch up by increasing tick frequency and reducing period in between. In the event, where for a long time the action takes longer then expected (by default, actual time is 3 seconds ahead of execution), ticker will skip required number of ticks to return to normal frequency.<br>
 * <br>
 * At any given moment, unless a spike has occurred a few ticks back, difference between ticker execution time and actual time will not exceed the tick time.<br>
 * <br>
 * By default ticker does not accept tick times lower than 4 ms, due to precision loss. But you can force ticker to accept it. In this case, previous rule no longer applies and instead, following rule will be true: At any given moment, unless a spike has occurred a few ticks back, difference between ticker execution time and actual time will not exceed double the tick time.<br>
 * <br>
 * In any case, ticker does not accept tick time equal to 0 or below.
 * 
 * @author Elix_x
 *
 */
public class Ticker {

	private static void sleep(long millis){
		if(millis <= 1) return;
		try{
			Thread.sleep(millis);
		} catch(InterruptedException e){

		}
	}

	private static final Thread tickerGranularityHack;

	static{
		//@formatter:off
		tickerGranularityHack = new Thread(() -> { while(true) sleep(Long.MAX_VALUE); }, "EXComms Ticker Granularity Hack Thread");
		//@formatter:on
		tickerGranularityHack.setDaemon(true);
	}

	private static void onTickerStart(){
		if(!tickerGranularityHack.isAlive()) tickerGranularityHack.start();
	}

	private long tickTime;
	private long catchUpMaxDelta = 3000;

	private Consumer<String> warningMessagesConsumer = message -> {};

	private WeakReference<Thread> thread;
	private long startTime;
	private long tickCount;

	/**
	 * Checks whether ticker is theoretically running.
	 * 
	 * @return is ticker theoretically running
	 */
	public boolean isRunning(){
		return thread != null;
	}

	private void assertNotRunning(){
		if(isRunning()) throw new IllegalStateException("Invalid operation. Ticker is running!");
	}

	/**
	 * Checks whether ticker is actually running (is it theoretically running and is thread, it ticks on, alive).
	 * 
	 * @return is ticker actually running
	 */
	public boolean isActuallyRunning(){
		return isRunning() && !thread.isEnqueued() && thread.get() != null && thread.get().isAlive();
	}

	/**
	 * Returns current tick time.
	 * 
	 * @return current tick time
	 */
	public long getTickTime(){
		return tickTime;
	}

	/**
	 * Sets tick time of this ticker. Cannot be changed while ticker is running.
	 * 
	 * @param tickTime
	 *            new tick time
	 * @return <tt>this</tt>
	 * @throws IllegalStateException
	 *             if the ticker is running
	 * @throws IllegalArgumentException
	 *             if the tick time is too low or negative
	 */
	public Ticker setTickTime(long tickTime) throws IllegalStateException, IllegalArgumentException{
		if(tickTime < 4)
			throw new IllegalArgumentException(String.format("Tick time requested (%s) may be too low for precision ticking. Either make it higher than 3 seconds or use #setTickTimeForce if you want to force usage of that tick time.", tickTime));
		return setTickTimeForce(tickTime);
	}

	/**
	 * Forces setting tick time of this ticker. Cannot be changed while ticker is running.
	 * 
	 * @param tickTime
	 *            new tick time
	 * @return <tt>this</tt>
	 * @throws IllegalStateException
	 *             if the ticker is running
	 * @throws IllegalArgumentException
	 *             if the tick time is 0 or negative
	 */
	public Ticker setTickTimeForce(long tickTime) throws IllegalStateException, IllegalArgumentException{
		assertNotRunning();
		if(tickTime <= 0) throw new IllegalArgumentException("Tick time cannot be zero or negative.");
		this.tickTime = tickTime;
		return this;
	}

	/**
	 * Returns current catch up max time difference.
	 * 
	 * @return current catch up max time difference
	 */
	public long getCatchUpMaxDelta(){
		return catchUpMaxDelta;
	}

	/**
	 * Sets new catch up max time difference of this ticker. Set to 0 or below not disable catching up and always skip ticks instead. Can be changed while ticker is running.
	 * 
	 * @param catchUpMaxDelta
	 *            new catch up max time difference
	 * @return <tt>this</tt>
	 */
	public Ticker setCatchUpMaxDelta(long catchUpMaxDelta){
		this.catchUpMaxDelta = catchUpMaxDelta;
		return this;
	}

	/**
	 * Sets catch up max time difference to default value - 3000 ms.
	 * 
	 * @return <tt>this</tt>
	 */
	public Ticker setCatchUpMaxDeltaDefault(){
		return setCatchUpMaxDelta(3000);
	}

	/**
	 * Disables catching up.
	 * 
	 * @return <tt>this</tt>
	 */
	public Ticker setNoCatchUp(){
		return setCatchUpMaxDelta(0);
	}

	/**
	 * Sets warning messages consumer. It will be fed warn messages when ticks are skipped or time goes backwards.
	 * 
	 * @param warningMessagesConsumer
	 *            new warning messages consumer
	 * @return <tt>this</tt>
	 */
	public Ticker setWarningMessagesConsumer(Consumer<String> warningMessagesConsumer){
		this.warningMessagesConsumer = warningMessagesConsumer;
		return this;
	}

	/**
	 * Retrieves current or previous sequence's start time.
	 * 
	 * @return current or previous sequence's start time
	 */
	public long getStartTime(){
		return startTime;
	}

	/**
	 * Retrieves current or previous sequence's total tick count.
	 * 
	 * @return current or previous sequence's total tick count
	 */
	public long getTickCount(){
		return tickCount;
	}

	/**
	 * Starts (new sequence of) the ticker. Must not be already running.
	 *
	 * @throws IllegalStateException
	 *             if the ticker is running
	 */
	public void start() throws IllegalStateException{
		assertNotRunning();
		onTickerStart();
		this.thread = new WeakReference<Thread>(Thread.currentThread());
		this.startTime = System.currentTimeMillis();
		this.tickCount = 0;
	}

	/**
	 * Finishes the tick. Executing wait, catch up or skip logic in required situations.
	 * 
	 * @throws IllegalStateException
	 *             if this method is called not on the same thread the ticker was started on
	 */
	public void finishTick() throws IllegalStateException{
		if(!isRunning()) return;
		if(!isActuallyRunning()){
			stop();
			return;
		}
		if(Thread.currentThread() != thread.get())
			throw new IllegalStateException("Finish tick must be called from the thread which started the ticker.");
		long time = System.currentTimeMillis() - startTime;
		long afterStart = time % tickTime;
		if(afterStart == 0){
			time += 1;
			afterStart = 1;
		}
		long actualTicksElapsed = (time - afterStart) / tickTime;
		if(actualTicksElapsed == tickCount){
			while(afterStart > 0){
				sleep((tickTime - afterStart) / 2);
				long currentTime = System.currentTimeMillis();
				afterStart = currentTime % tickTime;
			}
			tickCount++;
		} else if(actualTicksElapsed > tickCount){
			long timeDelta = time - tickCount * tickTime;
			if(timeDelta >= getCatchUpMaxDelta()){
				warningMessagesConsumer.accept("Could not catch up. Skipping " + (actualTicksElapsed - tickCount) + " ticks.");
				tickCount = actualTicksElapsed;
				finishTick();
			}
			tickCount++;
		} else{
			if(tickCount - actualTicksElapsed < 2){
				sleep(tickTime == 1 ? 2 : tickTime);
				finishTick();
			} else{
				warningMessagesConsumer.accept("Time went backwards. Restarting.");
				stop();
				start();
			}
		}
	}

	/**
	 * Stops (current sequence of) the ticker. Now you can change tick time and restart the ticker if you wish.
	 */
	public void stop(){
		if(isRunning()){
			this.thread = null;
		}
	}

}
