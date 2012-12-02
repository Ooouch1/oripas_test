package junit.oripa.concurrent;

import java.util.ArrayList;
import java.util.Collection;

import oripa.concurrent.PartialCollectionProcess;;

public class IntegerSummationProcess extends PartialCollectionProcess<Integer, Integer> {

	@Override
	public Collection<Integer> run(Collection<Integer> values) {
		ArrayList<Integer> result = new ArrayList<>();

		Integer sum = new Integer(0);
		
		for(Integer value : values){
			sum += value;
		}
		
		result.add(sum);
		
		return result;
		
	}
	
}
