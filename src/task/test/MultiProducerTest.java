package task.test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Iterator;
import java.util.List;
import java.util.function.IntSupplier;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import task.compulsory.*;

public class MultiProducerTest {
	
	public int[] value123A = { 0 };
	public IntSupplier amountLambda123A = () -> ++value123A[0];
	
	public int[] value123B = { 0 };
	public IntSupplier amountLambda123B = () -> ++value123B[0];
	
	public String[] txtA = { "" };
	public Supplier<String> contentLambdaA = () -> txtA[0] += "a";
	
	public String[] txtB = { "" };
	public Supplier<String> contentLambdaB = () -> txtB[0] += "a";
	
	@Test
	public void basicTest() {
		Iterator<Integer> values = List.of(3, 6, -1, -2, 0, 2).iterator();
		Iterator<String> txts = List.of("a", "b", "c", "d", "e", "f").iterator();
		
		assertAll(
				() -> assertEquals("a, a, a",
						MultiProducer.multiProducerFactory.get()
						.apply(() -> values.next(), () -> txts.next())),
				() -> assertEquals("b, b, b, b, b, b",
						MultiProducer.multiProducerFactory.get()
						.apply(() -> values.next(), () -> txts.next())),
				() -> assertEquals("f, f",
						MultiProducer.multiProducerFactory.get()
						.apply(() -> values.next(), () -> txts.next()))
				);
	}
	
	@Test
	public void test() {
		
		String attempt_1 = "a";
		String attempt_2 = "aa, aa";
		String attempt_3 = "aaa, aaa, aaa";
		String attempt_4 = "aaaa, aaaa, aaaa, aaaa";
		String attempt_5 = "aaaaa, aaaaa, aaaaa, aaaaa, aaaaa";
		String attempt_6 = "aaaaaa, aaaaaa, aaaaaa, aaaaaa, aaaaaa, aaaaaa";
		String attempt_7 = "aaaaaaa, aaaaaaa, aaaaaaa, aaaaaaa, aaaaaaa, aaaaaaa, aaaaaaa";
		
		assertAll(
				// A variant, attempt 1
				() -> assertEquals(attempt_1,
						MultiProducer.multiProducerFactory.get()
						.apply(amountLambda123A, contentLambdaA)),
				// B variant, attempt 1
				() -> assertEquals(attempt_1,
						MultiProducer.multiProducerFactory.get()
						.apply(amountLambda123B, contentLambdaB)),
				// A variant, attempt 2
				() -> assertEquals(attempt_2,
						MultiProducer.multiProducerFactory.get()
						.apply(amountLambda123A, contentLambdaA)),
				// B variant, attempt 2
				() -> assertEquals(attempt_2,
						MultiProducer.multiProducerFactory.get()
						.apply(amountLambda123B, contentLambdaB)),
				// A variant, attempt 3
				() -> assertEquals(attempt_3,
						MultiProducer.multiProducerFactory.get()
						.apply(amountLambda123A, contentLambdaA)),
				// B variant, attempt 3
				() -> assertEquals(attempt_3,
						MultiProducer.multiProducerFactory.get()
						.apply(amountLambda123B, contentLambdaB)),
				// A variant, attempt 4
				() -> assertEquals(attempt_4,
						MultiProducer.multiProducerFactory.get()
						.apply(amountLambda123A, contentLambdaA)),
				// B variant, attempt 4
				() -> assertEquals(attempt_4,
						MultiProducer.multiProducerFactory.get()
						.apply(amountLambda123B, contentLambdaB)),
				// A variant, attempt 5
				() -> assertEquals(attempt_5,
						MultiProducer.multiProducerFactory.get()
						.apply(amountLambda123A, contentLambdaA)),
				// B variant, attempt 5
				() -> assertEquals(attempt_5,
						MultiProducer.multiProducerFactory.get()
						.apply(amountLambda123B, contentLambdaB)),
				// A variant, attempt 6
				() -> assertEquals(attempt_6,
						MultiProducer.multiProducerFactory.get()
						.apply(amountLambda123A, contentLambdaA)),
				// B variant, attempt 6
				() -> assertEquals(attempt_6,
						MultiProducer.multiProducerFactory.get()
						.apply(amountLambda123B, contentLambdaB)),
				// A variant, attempt 7
				() -> assertEquals(attempt_7,
						MultiProducer.multiProducerFactory.get()
						.apply(amountLambda123A, contentLambdaA)),
				// B variant, attempt 7
				() -> assertEquals(attempt_7,
						MultiProducer.multiProducerFactory.get()
						.apply(amountLambda123B, contentLambdaB))
				);
	}
	
	@Test
	public void test_v() {
		String attempt_1 = IntStream.rangeClosed(1, 1)
				.mapToObj(i -> IntStream.rangeClosed(1, 1)
						.mapToObj(j -> "a")
						.collect(Collectors.joining()))
				.collect(Collectors.joining(", "));
		//System.out.println(attempt_1);
		
		String attempt_2 = IntStream.rangeClosed(1, 2)
				.mapToObj(i -> IntStream.rangeClosed(1, 2)
						.mapToObj(j -> "a")
						.collect(Collectors.joining()))
				.collect(Collectors.joining(", "));
		//System.out.println(attempt_2);
		
		String attempt_3 = IntStream.rangeClosed(1, 3)
				.mapToObj(i -> IntStream.rangeClosed(1, 3)
						.mapToObj(j -> "a")
						.collect(Collectors.joining()))
				.collect(Collectors.joining(", "));
		//System.out.println(attempt_3);
		
		String attempt_4 = IntStream.rangeClosed(1, 4)
				.mapToObj(i -> IntStream.rangeClosed(1, 4)
						.mapToObj(j -> "a")
						.collect(Collectors.joining()))
				.collect(Collectors.joining(", "));
		//System.out.println(attempt_4);
		
		String attempt_5 = IntStream.rangeClosed(1, 5)
				.mapToObj(i -> IntStream.rangeClosed(1, 5)
						.mapToObj(j -> "a")
						.collect(Collectors.joining()))
				.collect(Collectors.joining(", "));
		//System.out.println(attempt_5);
		
		String attempt_6 = IntStream.rangeClosed(1, 6)
				.mapToObj(i -> IntStream.rangeClosed(1, 6)
						.mapToObj(j -> "a")
						.collect(Collectors.joining()))
				.collect(Collectors.joining(", "));
		//System.out.println(attempt_6);
		
		String attempt_7 = IntStream.rangeClosed(1, 7)
				.mapToObj(i -> IntStream.rangeClosed(1, 7)
						.mapToObj(j -> "a")
						.collect(Collectors.joining()))
				.collect(Collectors.joining(", "));
		//System.out.println(attempt_7);
		
		List<String> attempts = List.of(attempt_1, attempt_2, attempt_3, attempt_4, attempt_5, attempt_6, attempt_7);
		
		attempts.stream().forEach(attempt -> {
			assertEquals(attempt, MultiProducer.multiProducerFactory.get().apply(amountLambda123B, contentLambdaB));
		});
		
	}
}
