public class Mark{
	public static double multiply(int i) {
		double x = 1.1 * (double)(i & 0xFF);
		return x * x * x * x * x * x * x * x * x * x
		* x * x * x * x * x * x * x * x * x * x;
	}
	
	public static void Mark0() { // USELESS
		Timer t = new Timer();
		double dummy = multiply(10);
		double time = t.check() * 1e9;
		System.out.printf("%6.1f ns%n", time);
	}
	
	public static void Mark1() { // NEARLY USELESS
		Timer t = new Timer();
		Integer count = 1_000_000;
		for (int i=0; i<count; i++) {
			double dummy = multiply(i);
		}
		double time = t.check() * 1e9 / count;
		System.out.printf("%6.1f ns%n", time);
	}
	
	public static double Mark2() {
		Timer t = new Timer();
		int count = 100_000_000;
		double dummy = 0.0;
		for (int i=0; i<count; i++)
			dummy += multiply(i);
		double time = t.check() * 1e9 / count;
		System.out.printf("%6.1f ns%n", time);
		return dummy;
	}
	
	public static double Mark3() {
		int n = 10;
		int count = 100_000_000;
		double dummy = 0.0;
		for (int j=0; j<n; j++) {
			Timer t = new Timer();
			for (int i=0; i<count; i++)
				dummy += multiply(i);
			double time = t.check() * 1e9 / count;
			System.out.printf("%6.1f ns%n", time);
		}
		return dummy;
	}
	
	public static double Mark4() {
		int n = 10;
		int count = 100_000_000;
		double dummy = 0.0;
		double st = 0.0, sst = 0.0;
		for (int j=0; j<n; j++) {
			Timer t = new Timer();
			for (int i=0; i<count; i++)
				dummy += multiply(i);
			double time = t.check() * 1e9 / count;
			st += time;
			sst += time * time;
		}
		double mean = st/n, sdev = Math.sqrt(sst/n - mean*mean);
		System.out.printf("%6.1f ns +/- %6.3f %n", mean, sdev);
		return dummy;
	}

	
	public static double Mark5() {
		int n = 10, count = 1, totalCount = 0;
		double dummy = 0.0, runningTime = 0.0;
		do {
			count *= 2;
			double st = 0.0, sst = 0.0;
			for (int j=0; j<n; j++) {
				Timer t = new Timer();
				for (int i=0; i<count; i++)
				dummy += multiply(i);
				runningTime = t.check();
				double time = runningTime * 1e9 / count;
				st += time;
				sst += time * time;
				totalCount += count;
			}
			double mean = st/n, sdev = Math.sqrt(sst/n - mean*mean);
			System.out.printf("%6.1f ns +/- %8.2f %10d%n", mean, sdev, count);
		} while (runningTime < 0.25 && count < Integer.MAX_VALUE/2);
		return dummy / totalCount;
	}

	public static double Mark6(String msg, IntToDouble f) {
		int n = 10, count = 1, totalCount = 0;
		double dummy = 0.0, runningTime = 0.0, st = 0.0, sst = 0.0;
		do {
			count *= 2;
			st = sst = 0.0;
			for (int j=0; j<n; j++) {
			Timer t = new Timer();
			for (int i=0; i<count; i++)
				dummy += f.call(i);
				runningTime = t.check();
				double time = runningTime * 1e9 / count;
				st += time;
				sst += time * time;
				totalCount += count;
			}
			double mean = st/n, sdev = Math.sqrt(sst/n - mean*mean);
			System.out.printf("%-25s %15.1f %10.2f %10d%n", msg, mean, sdev, count);
		} while (runningTime < 0.25 && count < Integer.MAX_VALUE/2);
		return dummy / totalCount;
	}

	
	public static double Mark7(String msg, IntToDouble f) {
		int n = 10, count = 1, totalCount = 0;
		double dummy = 0.0, runningTime = 0.0, st = 0.0, sst = 0.0;
		do {
			count *= 2;
			st = sst = 0.0;
			for (int j=0; j<n; j++) {
			Timer t = new Timer();
			for (int i=0; i<count; i++)
				dummy += f.call(i);
				runningTime = t.check();
				double time = runningTime * 1e9 / count;
				st += time;
				sst += time * time;
				totalCount += count;
			}
			double mean = st/n, sdev = Math.sqrt(sst/n - mean*mean);
			System.out.printf("%-25s %15.1f %10.2f %10d%n", msg, mean, sdev, count);
		} while (runningTime < 0.25 && count < Integer.MAX_VALUE/2);
		double mean = st/n, sdev = Math.sqrt(sst/n - mean*mean);
		System.out.printf("%-25s %15.1f %10.2f %10d%n", msg, mean, sdev, count);
		return dummy / totalCount;
	}
	
	public static void SystemInfo() { 
		 System.out.printf("# OS: %s; %s; %s%n", 
		 System.getProperty("os.name"), 
		 System.getProperty("os.version"), 
		 System.getProperty("os.arch")); 
		 System.out.printf("# JVM: %s; %s%n", 
		 System.getProperty("java.vendor"), 
		 System.getProperty("java.version")); 
		 // This line works only on MS Windows: 
		 System.out.printf("# CPU: %s%n", System.getenv("PROCESSOR_IDENTIFIER")); 
		 java.util.Date now = new java.util.Date(); 
		 System.out.printf("# Date: %s%n", 
		 new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(now)); 
	} 
	
}