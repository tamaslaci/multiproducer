package task.extension;

import java.util.LinkedList;
import java.util.List;
//import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import static task.extension.MultiProducer2State.EXTEND;
//import static task.extension.MultiProducer2State.KEEP;

public class MultiProducer2 {
	public static BiFunction<Integer, Supplier<MultiProducer2State>, Function<String, Supplier<String>>> cachedMultiProducer =
		new BiFunction<>() {
			LinkedList<String> cache = new LinkedList<>(List.of(""));
			String prev = "";
		
			@Override
			public Function<String, Supplier<String>> apply(Integer decisionCount, Supplier<MultiProducer2State> decisionLambda){
				return new Function<>() {					
					@Override
					public Supplier<String> apply(String appendTxt){
						if(appendTxt == null || !appendTxt.equals(prev)) {
							cache = new LinkedList<>(List.of(""));
							prev = appendTxt;
						}
						
						if(cache.size() == 1) {
							IntStream.range(0, decisionCount).mapToObj(n -> decisionLambda.get()).forEach(state -> {
								if(state == EXTEND) {
									cache.add(cache.get(cache.size() - 1).concat(appendTxt));
								} else {
									cache.add(cache.get(cache.size() - 1));
								}
							});
						}
						
						return new Supplier<>() {
							@Override
							public String get() {
								return cache.remove();
							}
						};
					}
				};
			}
		};
		
	
	public static <T> BiFunction<Supplier<T>, Supplier<T>, Supplier<T>> oneFromEach(){
		return new BiFunction<>() {
			@Override
			public Supplier<T> apply(Supplier<T> fst, Supplier<T> snd){
				return new Supplier<>() {
					static boolean prev = true;
					@Override
					public T get() {
						prev = !prev;
						if(prev) return snd.get();
						return fst.get();
					}
				};
			};
		};
	}
			
	//private Supplier<MultiProducer2State> decisionLambda = () -> {
	//	return ThreadLocalRandom.current().nextBoolean() ? KEEP : EXTEND;
	//};
}
