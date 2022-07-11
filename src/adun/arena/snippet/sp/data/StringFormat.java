package adun.arena.snippet.sp.data;

public class StringFormat {

	public static void main(String[] args) {

		System.out.println(String.format("%s,%s", "A", "B"));
		System.out.println(String.format("[%d],[%05d]", 123, 123)); //[123],[00123]
		System.out.println(String.format("[%7d],[%-7d]", 123, 123)); //[    123],[123    ]
		System.out.println(String.format("[%,7d]", 1234)); //[  1,234]
		
		System.out.println(String.format("[%7.1f]", 12.34)); //[   12.3]
		System.out.println(String.format("[%7.2f]", 12.34)); //[  12.34]

	}
}
