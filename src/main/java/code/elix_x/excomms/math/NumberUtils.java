package code.elix_x.excomms.math;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;

import com.google.common.primitives.Bytes;
import com.google.common.primitives.Doubles;
import com.google.common.primitives.Floats;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import com.google.common.primitives.Shorts;

import code.elix_x.excomms.reflection.ReflectionHelper.AClass;

public class NumberUtils {

	public static BigDecimal toBigDecimal(Number number){
		return new BigDecimal(number.toString());
	}

	public static <N extends Number> N fromBigDecimal(BigDecimal number, Class<N> n){
		return new AClass<N>(n).getDeclaredConstructor(String.class).newInstance(number.toString());
	}

	public static byte min(byte... ints){
		return minComparable(Bytes.asList(ints));
	}

	public static byte max(byte... ints){
		return maxComparable(Bytes.asList(ints));
	}

	public static short min(short... shorts){
		return minComparable(Shorts.asList(shorts));
	}

	public static short max(short... shorts){
		return maxComparable(Shorts.asList(shorts));
	}

	public static int min(int... ints){
		return minComparable(Ints.asList(ints));
	}

	public static int max(int... ints){
		return maxComparable(Ints.asList(ints));
	}

	public static long min(long... longs){
		return minComparable(Longs.asList(longs));
	}

	public static long max(long... longs){
		return maxComparable(Longs.asList(longs));
	}

	public static float min(float... floats){
		return minComparable(Floats.asList(floats));
	}

	public static float max(float... floats){
		return maxComparable(Floats.asList(floats));
	}

	public static double min(double... doubles){
		return minComparable(Doubles.asList(doubles));
	}

	public static double max(double... doubles){
		return maxComparable(Doubles.asList(doubles));
	}

	@SafeVarargs
	public static <N extends Number & Comparable<N>> N minComparable(N... numbers){
		return minComparable(Arrays.stream(numbers));
	}

	@SafeVarargs
	public static <N extends Number & Comparable<N>> N maxComparable(N... numbers){
		return maxComparable(Arrays.stream(numbers));
	}

	public static <N extends Number & Comparable<N>> N minComparable(Collection<N> numbers){
		return minComparable(numbers.stream());
	}

	public static <N extends Number & Comparable<N>> N maxComparable(Collection<N> numbers){
		return maxComparable(numbers.stream());
	}

	public static <N extends Number & Comparable<N>> N minComparable(Stream<N> numbers){
		return numbers.min((N n1, N n2) -> n1.compareTo(n2)).get();
	}

	public static <N extends Number & Comparable<N>> N maxComparable(Stream<N> numbers){
		return numbers.max((N n1, N n2) -> n1.compareTo(n2)).get();
	}

	@SafeVarargs
	public static <N extends Number> N min(N... numbers){
		return min(Arrays.stream(numbers));
	}

	@SafeVarargs
	public static <N extends Number> N max(N... numbers){
		return max(Arrays.stream(numbers));
	}

	public static <N extends Number> N min(Collection<N> numbers){
		return min(numbers.stream());
	}

	public static <N extends Number> N max(Collection<N> numbers){
		return max(numbers.stream());
	}

	@SuppressWarnings("unchecked")
	public static <N extends Number> N min(Stream<N> numbers){
		N number = numbers.findAny().get();
		if(number instanceof Comparable) return (N) minComparable((Stream) numbers);
		else return (N) fromBigDecimal(minComparable(numbers.map(n -> toBigDecimal(n))), number.getClass());
	}

	@SuppressWarnings("unchecked")
	public static <N extends Number> N max(Stream<N> numbers){
		N number = numbers.findAny().get();
		if(number instanceof Comparable) return (N) maxComparable((Stream) numbers);
		else return (N) fromBigDecimal(maxComparable(numbers.map(n -> toBigDecimal(n))), number.getClass());
	}

}
