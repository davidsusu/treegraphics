package treegraphics.test;

import treegraphics.valuetree.*;

public class ValueTreeTest {
	
	public static void main(String[] args) {
		StaticValue source1 = new StaticValue(10);
		StaticValue source2 = new StaticValue(20);
		
		Value target1 = new AverageValue(source1, source2);

		Value target2 = new AverageValue(target1, source2);

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
	}

	public static class AverageValue extends AbstractValue {
		
		final Value source1;
		
		final Value source2;
		
		public AverageValue(Value source1, Value source2) {
			this.source1 = source1;
			this.source2 = source2;
			source1.registerDependent(this);
			source2.registerDependent(this);
		}

		@Override
		protected void reload() {
			cachedValue = (source1.get()+source2.get())/2;
		}

		@Override
		public void freeFromDependecies() {
			source1.unregisterDependent(this);
			source2.unregisterDependent(this);
		}
		
	}
	
}
