package crossover;

// (c) 1998 Robert W. Harrison
//

public class rrandom extends Object{
	private float buff[]=  new float[55];
	private int ip,jp,kp;

	public rrandom( int seed)
	{
	 	int i;

		ip = 24; jp=21; kp = 0;

	for( i=0; i < 55; i++)
	    { seed = (seed*2349+14867)%32767;
		buff[i] = (float)seed/(float)32767.;
		if( buff[i] > (float)1.) buff[i] = buff[i]-(float)1.;
		if( buff[i] < (float)0.) buff[i] = buff[i]+(float)1.;
		}
	}

	public float nextFloat()
	{
	float x;
	x = buff[ip] + buff[jp] ;
	if( x > 1. ) x = x-(float)1.;
	buff[kp] = x;
	ip++;
	jp++;
	kp++;
	if( ip == 55) ip = 0;
	if( jp == 55) jp = 0;
	if( kp == 55) kp = 0;
	return x;
	}
//  
//  weird java idiom 
// main exists in the class that it instanciates during testing.
// Of course, the real main will not conflict with this.
//
	public static void main(String argv[])
	{
		rrandom r = new rrandom(10101);
		for( int i=0; i< 100;i++)
			System.out.println(String.valueOf(r.nextFloat()));
		float bins[] = new float[100];
		for( int i=0; i< 10000000; i++)
		{
			float x;
			int ix;
			x = (float)100.*r.nextFloat();
			ix = (int)x;
			if( ix < 0) ix = 0;
			if( ix >99) ix =99;
			bins[ix]+=1;
		}
	
	for( int i=0; i< 100; i++)
	  System.out.println(String.valueOf(i)+" "+String.valueOf(bins[i]));
	
	}
}// end of class rRandom
