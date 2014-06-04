package control;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int a = 8;
		int b = 12;
		int c = a & b;
		
		System.out.println("c = " + c);
		if (((int) a & b) > 0) {
			System.out.println("result ok");
		} else {
			System.out.println("result bad");
		}
	}

}
