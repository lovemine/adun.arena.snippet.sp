package adun.arena.spx.array;

public class ArrayUtil {

	/**
	 * 숫자를 각 자릿수를 가지는 Array로 변환한다. (Integer.toString을 사용하지 않는다.)
	 * eg. 1259873 => {1,2,5,9,8,7,3}
	 * 
	 */
	public static int[] toArray(int num) {
		int len = ((int)Math.log10((double)num)) + 1;
		int[] buffer = new int[len];
		for (int idx = 0; idx < len; idx++) {
			buffer[len - idx - 1] = num % 10;
			num = num / 10;
		}
		return buffer;
	}

}
