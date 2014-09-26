// For week 5
// sestoft@itu.dk * 2014-09-19

import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.Callable;



public class TestDownload {
  private static final ExecutorService executor = Executors.newWorkStealingPool();

  private static final String[] urls = 
  { "http://www.itu.dk", "http://www.di.ku.dk", "http://www.miele.de",
    "http://www.microsoft.com", "http://www.amazon.com", "http://www.dr.dk",
    "http://www.vg.no", "http://www.tv2.dk", "http://www.google.com",
    "http://www.ing.dk", "http://www.dtu.dk", "http://www.eb.dk", 
    "http://www.nytimes.com", "http://www.guardian.co.uk", "http://www.lemonde.fr",   
    "http://www.welt.de", "http://www.dn.se", "http://www.heise.de", "http://www.wsj.com", 
    "http://www.bbc.co.uk", "http://www.dsb.dk", "http://www.bmw.com", "https://www.cia.gov" 
  };

  public static void main(String[] args) throws IOException {
    String url = "http://www.wikipedia.org/";
    // String page = getPage(url, 10);
    // System.out.printf("%-30s%n%s%n", url, page);
    //Ex 5.3

  //   for(Map.Entry<String, String> ent : getPages(urls, 10).entrySet()){
  //     System.out.println("for " + ent.getKey() + "\n ### " + ent.getValue());
  //   }
  }

  public static String getPage(String url, int maxLines) throws IOException {
    // This will close the streams after use (JLS 8 para 14.20.3):
    try (BufferedReader in 
         = new BufferedReader(new InputStreamReader(new URL(url).openStream()))) {
      StringBuilder sb = new StringBuilder();
      for (int i=0; i<maxLines; i++) {
        String inputLine = in.readLine();
        if (inputLine == null)
          break;
        else
          sb.append(inputLine).append("\n");
      }
      return sb.toString();
    }
  }

  //Returns a unmodifiableMap
  public static Map<String, String> getPages(String[] urls, int maxLines){
    HashMap<String, String> tmpMap = new HashMap<>();
    for(String url: urls){
      try{
      tmpMap.put(url, getPage(url, maxLines));
      }
      catch(IOException e){
        System.out.println("THIS WENT BAD");
      }

    }
    return Collections.unmodifiableMap(tmpMap);
  }

  public static Map<String, String> getPagesParallel(String[] urls, int maxLines){
    HashMap<String, String> tmpMap = new HashMap<>();
    List<Future<String>> futures = new ArrayList<Future<String>>();
    for(String url: urls){
      futures.add( 
        executor.submit(new Callable<String>() {
          public String call(){
            try{ return getPage(url, maxLines);}
            catch(Exception e ) { System.out.println("something went wrong..."); return " ";}

          }
        })
        );
    }
    try{
      int i = 0;
      for(Future<String> f : futures){
        tmpMap.put(urls[i++], f.get());
      }
    }
    catch(Exception e){
      System.out.println("THIS WENT BAD");
    }
    return Collections.unmodifiableMap(tmpMap);    
  }
}

