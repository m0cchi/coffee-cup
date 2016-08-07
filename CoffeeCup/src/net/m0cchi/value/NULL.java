package net.m0cchi.value;

public class NULL {
	private static final NULL OWN = new NULL();

	public static class NIL extends Value<NULL> {

		public NIL() {
			super(AtomicType.NIL, OWN);
		}

	}

}
