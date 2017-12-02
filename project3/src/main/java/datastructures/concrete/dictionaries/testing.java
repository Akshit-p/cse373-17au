package datastructures.concrete.dictionaries;

public class testing {

	public static void main(String[] args) {
		AvlTreeDictionary<Integer, Integer> lol = new  AvlTreeDictionary<>();
		lol.put(1, 1);
		lol.put(2, 2);
		System.out.print(lol.size());
		System.out.print(lol.get(1));
		System.out.print(lol.get(2));

	}

}
