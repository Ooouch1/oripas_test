package junit.oripa.concurrent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

import org.junit.Test;

import oripa.concurrent.ListParallelProcessor;
import oripa.concurrent.PartialCollectionProcess;
import oripa.concurrent.PartialCollectionProcessFactory;

public class ParallelProcessorTest {

	//----- Factories for test -----
	
	class SummationProcessFactory 
		implements PartialCollectionProcessFactory<Integer, Integer>{
		@Override
		public PartialCollectionProcess<Integer, Integer> create() {
			return new IntegerSummationProcess();
		}
		
	}
	
	class SquareTimeProcessFactory 
		implements PartialCollectionProcessFactory<Integer, Integer>{
		
		@Override
		public PartialCollectionProcess<Integer, Integer> create() {
			return new SquareTimeProcess();
		}
	}

	//---- test code ----
	
	private final int DIV_NUM = 4;
	private final String className = this.getClass().getName();
	
	ListParallelProcessor<Integer, Integer> processor = new ListParallelProcessor<>(
			new SummationProcessFactory());
	
	Logger logger = Logger.getLogger(className);
	
	public ParallelProcessorTest() {
	}
	
	
	@Test
	public void testExecute() {
		
		ArrayList<Integer> values = createSequentialIntegers(0, 10000);

		logger.info("testExecute");
		assertEquals(doTotal(values), parallelSummation(values));
		
	}
	
	@Test
	public void testTimeEfficiency(){
		ArrayList<Integer> values = createSequentialIntegers(0, 10000);

		logger.info("testTimeEfficiency");
		SquareTimeProcessFactory factory = new SquareTimeProcessFactory();
		executeParallelProcessing(factory, values);		
		executeNormalProcessing(factory, values);
	}

	private ArrayList<Integer> executeParallelProcessing(
			PartialCollectionProcessFactory<Integer, Integer> factory, ArrayList<Integer> values){
		
		ListParallelProcessor<Integer, Integer> processor = 
				new ListParallelProcessor<>(new SquareTimeProcessFactory());


		long startTime = System.currentTimeMillis();

		ArrayList<Integer> result = null;

		try{
			result = processor.execute(values, DIV_NUM);	
		}
		catch (Exception e) {
			fail("processor throws error");
		}

		long endTime = System.currentTimeMillis();
		logger.info("the parallel processing took " + Long.toString(endTime - startTime) + "[ms]");
			
		return result;
	}

	private Collection<Integer> executeNormalProcessing(
			PartialCollectionProcessFactory<Integer, Integer> factory, ArrayList<Integer> values){
	
		long startTime = System.currentTimeMillis();
		
		Collection<Integer> result = factory.create().run(values);

		long endTime = System.currentTimeMillis();
		logger.info("the normal processing took " + Long.toString(endTime - startTime) + "[ms]");
		
		return result;
	}
	
	private Integer parallelSummation(ArrayList<Integer> values){
		ArrayList<Integer> summations = 
				executeParallelProcessing(new SummationProcessFactory(), values);		

		Integer total = 0;

		for(Integer sum : summations){
			total += sum;
		}
		
		return total;
	}

	@Test
	public void testSeparate() {
		
		ArrayList<Integer> values = createSequentialIntegers(0, 10);

		ArrayList<Collection<Integer>> separated = processor.separate(values, DIV_NUM);

		
		logger.info("separated into: " + Integer.toString(separated.size()) );
		
		int elementNum = 0;
		for(int div = 0; div < DIV_NUM; div++){
			Collection<Integer> partial = separated.get(div);
			elementNum += partial.size();
		}
		
		assertEquals(values.size(), elementNum);
		
	}
	
	private ArrayList<Integer> createSequentialIntegers(int firstValue, int lastValue){
		ArrayList<Integer> values = new ArrayList<>();
		
		for(int i = firstValue; i <= lastValue; i++){
			values.add(new Integer(i));
		}
		
		return values;
	}
	
	private Integer doTotal(ArrayList<Integer> values){
		return executeNormalProcessing(new SummationProcessFactory(), values).iterator().next();	
	}

}
