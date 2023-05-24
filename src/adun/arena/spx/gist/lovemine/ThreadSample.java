package adun.arena.spx.gist.lovemine;

public class ThreadSample {

	public static void main(String[] args) {
		MyRun mr1 = new MyRun();
		Thread t1 = new Thread(mr1);
		MyThread t2 = new MyThread();
		t1.start();
		t2.start();
		for (int i = 0; i < 100; i++) {
			System.out.print("M");
		}
	}

	public static class MyRun implements Runnable {

		public void run() {
			show();
		}

		public void show() {
			for (int i = 0; i < 100; i++) {
				System.out.print("S");
			}
		}
	}

	public static class MyThread extends Thread {

		public void run() {
			for (int i = 0; i < 100; i++) {
				System.out.print("T");
			}
		}
	}

}
