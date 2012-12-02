package junit.oripa.concurrent;

import java.util.Collection;

import oripa.concurrent.PartialCollectionProcess;

public class SquareTimeProcess extends PartialCollectionProcess<Integer, Integer> {

	@Override
	public Collection<Integer> run(Collection<Integer> values) {
		
		int square = 0;
		for(int i = 0; i < values.size(); i++){
			for(int j = 0; j < values.size(); j++){
				square++;
			}
		}
		
		
		return values;
	}

}
