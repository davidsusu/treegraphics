package treegraphics.test;

import treegraphics.valuetree.*;
import treegraphics.valuetree.value.PairAverageValue;
import treegraphics.valuetree.value.StaticValue;
import treegraphics.valuetree.value.WeightedAverageValue;

public class ValueTreeTest {
	
	public static void main(String[] args) {
		StaticValue source1 = new StaticValue(10);
		StaticValue source2 = new StaticValue(20);
		
		Value target1 = new PairAverageValue(source1, source2);

		Value target2 = new PairAverageValue(target1, source2);

		System.out.println(source1.get());
		System.out.println(source2.get());
		System.out.println(target1.get());
		System.out.println(target2.get());
		
		System.out.println("---------------");
		source2.set(30);

		System.out.println(source1.get());
		System.out.println(source2.get());
		System.out.println(target1.get());
		System.out.println(target2.get());

		System.out.println("---------------");
		source2.set(7);

		Value target3 = new WeightedAverageValue(
			new WeightedAverageValue.Entry(1, source1),
			new WeightedAverageValue.Entry(2, source2)
		);
		
		System.out.println(target3.get());
	}

}
