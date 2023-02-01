package claire;

import java.util.LinkedHashMap;
import java.util.Map;

public class LinkedHashMapTest {

	public static void main(String[] args) {

		LinkedHashMap<Integer, String> lhm = new LinkedHashMap<>() {
			@Override
			protected boolean removeEldestEntry(Map.Entry<Integer, String> eldest) {
				return size() > 3;
			}
		};

		lhm.put(1, "one");
		lhm.put(2, "two");
		lhm.put(3, "three");
		System.out.println(lhm);
		lhm.put(4, "four");
		System.out.println(lhm);
		lhm.put(5, "five");
		System.out.println(lhm);
		
	}

}
