package adun.arena.sp.data;

public class StringFormatSample {

	/**
	 * String Format 의 여러가지 샘플
	 */
	public static void main(String[] args) {

		//%s : 일반적인 String 값을 치환한다.
		System.out.println(String.format("%s,%s", "A", "B"));
		
		//%d : 숫자를 치환한다.
		//%05d : 5자리를 0으로 패딩하면서 치환한다.
		System.out.println(String.format("[%d],[%05d]", 123, 123)); //[123],[00123]
		
		//%7d : 숫자를 공백으로 패딩하면서 치환한다.
		//%-7d : 숫자를 치환하되 뒤 부분을 공백으로 패딩한다.
		System.out.println(String.format("[%7d],[%-7d]", 123, 123)); //[    123],[123    ]

		//%,7d : 숫자를 공백으로 패딩하면서 치환하되 3자리마다 ,를 찍는다.
		System.out.println(String.format("[%,7d]", 1234)); //[  1,234]

		//7.1f : 숫자를 공백으로 패딩하면서 치환하되 소숫점을 1자리로 한다.
		System.out.println(String.format("[%7.1f]", 12.34)); //[   12.3]
		
		//7.2f : 숫자를 공백으로 패딩하면서 치환하되 소숫점을 2자리로 한다.
		System.out.println(String.format("[%7.2f]", 12.34)); //[  12.34]

	}
}
