package adun.arena.sp.data;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Singleton 구현 부분은 그대로 두고 필요 Data를 attribute로 정의해서 getter/setter 구현해서 사용한다.
 * 
 * SampleDto를 수정하거나 SampleDto 대신 다른 DTO Class를 외부에 선언하고 사용할 수 있다.
 * 
 */
public class Singleton {

	//필요 Data 및 Getter/Setter
	private List<SampleDto> list;

	private Map<String, SampleDto> map;

	public List<SampleDto> getList() {
		return list;
	}

	public synchronized void setList(List<SampleDto> list) {
		this.list = list;
	}

	public synchronized void addList(SampleDto dto) {
		if (this.list == null) {
			this.list = new ArrayList<SampleDto>();
		}
		this.list.add(dto);
	}

	public Map<String, SampleDto> getMap() {
		return map;
	}

	public synchronized void setMap(Map<String, SampleDto> map) {
		this.map = map;
	}

	public synchronized void addMap(String key, SampleDto dto) {
		if (this.map == null) {
			this.map = new LinkedHashMap<String, SampleDto>();
		}
		this.map.put(key, dto);
	}

	/////////////////////////////
	///
	/// SampleDto 모양은 수정하던지 외부 DTO를 별도로 정의해서 쓴다. 
	///
	////////////////////////////

	public static class SampleDto {

		public String key;

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

	/////////////////////////////
	///
	/// Singleton 구현 부분. 이 아래는 손댈필요가 없다.
	///
	////////////////////////////
	private static Singleton instance;

	private Singleton() {}

	static {
		try {
			instance = new Singleton();
		} catch (Exception e) {
			throw new RuntimeException("Create instace fail. error msg = " + e.getMessage());
		}
	}

	public static Singleton getInstance() {
		return instance;
	}

}
