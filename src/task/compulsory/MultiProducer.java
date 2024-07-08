package task.compulsory;

import java.util.Collections;
import java.util.function.BiFunction;
import java.util.function.IntSupplier;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class MultiProducer {
	
public static Supplier<BiFunction<IntSupplier, Supplier<String>, String>> multiProducerFactory = new Supplier<>() {
	@Override
	public BiFunction<IntSupplier, Supplier<String>, String> get(){
		return new BiFunction<>() {
			@Override
			public String apply(IntSupplier amountLambda, Supplier<String> contentLambda){
					int amount = amountLambda.getAsInt();
					String content = contentLambda.get();
					
					if(amount <= 0) return this.apply(amountLambda, contentLambda);
					
					return Collections.nCopies(amount, content).stream().collect(Collectors.joining(", "));
			};
		};
	};
};

}