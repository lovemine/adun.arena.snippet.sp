package adun.arena.spx.collection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * List의 Binary Search 샘플
 * 
 * Collections의 Binary Search를 사용하려면 List는 반드시 사전에 먼저 정렬되어 있어야 한다.
 * 
 * 아래 샘플의 각 데이터는 의미 없는 데이터를 쓰고 있다.
 */
public class BinarySearchSample {

	public static void main(String[] args) {

		// 의미 없지만 list를 만든다.
		List<SampleDto> list = new ArrayList<>();
		list.add(new SampleDto());
		list.add(new SampleDto());
		list.add(new SampleDto());
		list.add(new SampleDto());
		list.add(new SampleDto());
		list.add(new SampleDto());
		list.add(new SampleDto());
		list.add(new SampleDto());
		list.add(new SampleDto());

		//사전 정렬한다.
		list.sort(new Comparator<SampleDto>() {

			@Override
			public int compare(SampleDto o1, SampleDto o2) {
				//				return o1.getKey().compareTo(o2.getKey());
				return o1.hashCode() - o2.hashCode();
			}
		});

		//찾을 놈을 정의한다.
		SampleDto key = list.get(4);

		//binarySearch 한다.
		int index = Collections.binarySearch(list, key, new Comparator<SampleDto>() {

			@Override
			public int compare(SampleDto o1, SampleDto o2) {
				//				return o1.getKey().compareTo(o2.getKey());
				return o1.hashCode() - o2.hashCode();
			}
		});

		//결가는?
		if (index >= 0) {
			SampleDto found = list.get(index);
			System.out.println("found : [" + index + "]  " + found);
		} else {
			//not found
		}

	}

	public static class SampleDto {

		public String key = ""; //NPE 방지

		public String value;

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

	}

}