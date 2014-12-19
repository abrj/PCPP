public class CasHistogram implements Histogram{
	public CasHistogram(){

	}
	void increment(int bin);
	int getCount(int bin);
  	int getSpan();
  	int[] getBins();
  	int getAndClear(int bin);
  	void transferBins(Histogram hist);
}