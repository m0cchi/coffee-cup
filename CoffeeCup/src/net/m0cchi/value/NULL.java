package net.m0cchi.value;

public class NULL {
	private static final NULL OWN = new NULL();

	public static class NIL extends Value<NULL> {
		private static final long serialVersionUID = 6231554955129135752L;

		public NIL() {
			super(AtomicType.NIL, OWN);
		}

	}

}
