package task.test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import task.extension.MultiProducer2;
import task.extension.MultiProducer2State;

import static task.extension.MultiProducer2State.KEEP;
import static task.extension.MultiProducer2State.EXTEND;;

public class MultiProducerTest2 {
	
	private static Supplier<MultiProducer2State> flipDecisionLambda;
	private static Supplier<MultiProducer2State> bunchDecisionLambda;
	
	@BeforeAll
	public static void setup() {

		flipDecisionLambda = new Supplier<>() {
			boolean prev = true;
			@Override
			public MultiProducer2State get() {
				prev = !prev;
				if(prev) return KEEP;
				return EXTEND;
			}
		};
		
		bunchDecisionLambda = new Supplier<>() {
			boolean prev = true;
			int count = 1;
			LinkedList<MultiProducer2State> keeps;
			@Override
			public MultiProducer2State get() {
				if(prev) {
					keeps = new LinkedList<>(IntStream.range(0, count).mapToObj(n -> KEEP).toList());
					prev = false;
					return EXTEND;
				}
				if(keeps.size() == 1) {
					++count;
					prev = true;
				}
				return keeps.remove();
			}
		};
		
	}
	
	@ParameterizedTest
	@CsvSource(textBlock = """
			ccc, 12, 12
			bb, 9, 6
			a, 6, 3
			, 3, 1
			""")
	public void testConstant(String appendTxt, Integer decisionCount, int testCount) {
		
		String result = IntStream.range(0, testCount)
				.mapToObj(n -> MultiProducer2.cachedMultiProducer
						.apply(decisionCount, () -> KEEP)
						.apply(appendTxt).get()).toList().get(testCount - 1);
		
		assertEquals("", result);
	}
	
	@Test
	public void flip20() {
		
		String expectedOutput = 
				", a, a,"
				+ " aa, aa,"
				+ " aaa, aaa,"
				+ " aaaa, aaaa,"
				+ " aaaaa, aaaaa,"
				+ " aaaaaa, aaaaaa,"
				+ " aaaaaaa, aaaaaaa,"
				+ " aaaaaaaa, aaaaaaaa,"
				+ " aaaaaaaaa, aaaaaaaaa,"
				+ " aaaaaaaaaa";
		
		String actualOutput = IntStream.range(0, 20)
				.mapToObj(n -> MultiProducer2.cachedMultiProducer
						.apply(10, flipDecisionLambda)
						.apply("a").get()).collect(Collectors.joining(", "));
		
		assertEquals(expectedOutput, actualOutput);
		
	}
	
	@Test
	public void bunch20() {
		String expectedOutput = 
				", b, b,"
				+ " bb, bb, bb,"
				+ " bbb, bbb, bbb, bbb,"
				+ " bbbb, bbbb, bbbb, bbbb, bbbb,"
				+ " bbbbb, bbbbb, bbbbb, bbbbb, bbbbb";
		
		String actualOutput = IntStream.range(0, 20)
				.mapToObj(n -> MultiProducer2.cachedMultiProducer
						.apply(10, bunchDecisionLambda)
						.apply("b").get()).collect(Collectors.joining(", "));
		
		assertEquals(expectedOutput, actualOutput);
				
	}
	
	@Test
	public void oneOfAB() {
		List<String> expectedOutput = List.of("a", "b", "a", "b", "a", "b");
		
		List<String> actualOutput = IntStream.range(0, 6)
				.mapToObj(n -> MultiProducer2.oneFromEach().apply(() -> "a", () -> "b").get())
				.map(Object::toString)
				.toList();
		
		assertArrayEquals(expectedOutput.toArray(), actualOutput.toArray());

	}
	
	@ParameterizedTest
	@ValueSource(ints = {1, 3, 10})
	public void cachedMultiProducerTest(int decisionCount) {
		List<String> expectedOutput = List.of("","", "a", "b", "a", "b", "aa", "bb", "aa", "bb", "aaa", "bb", "aaa", "bbb",
				"aaaa", "bbb", "aaaa", "bbb", "aaaaa", "bbb", "aaaaa", "bbbb", "aaaaaa", "bbbb", "aaaaaa", "bbbb",
				"aaaaaaa", "bbbb", "aaaaaaa", "bbbb", "aaaaaaaa", "bbbbb", "aaaaaaaa", "bbbbb", "aaaaaaaaa",
				"bbbbb", "aaaaaaaaa", "bbbbb", "aaaaaaaaaa", "bbbbb");
		
//		List<String> actualOutput = IntStream.range(0, 40)
//				.mapToObj(n -> MultiProducer2.oneFromEach().apply(
//						MultiProducer2.cachedMultiProducer
//						.apply(decisionCount, flipDecisionLambda)
//						.apply("a").get(),
//						MultiProducer2.cachedMultiProducer
//						.apply(decisionCount, bunchDecisionLambda)
//						.apply("b").get()).get())
//				.map(Object::toString)
//				.toList();
		
//		assertArrayEquals(expectedOutput.toArray(), actualOutput.toArray());
	}
	
}
