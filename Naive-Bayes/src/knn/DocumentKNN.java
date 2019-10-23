
package knn;

import java.util.*;

public class DocumentKNN {
     
	public String topic;
        public HashMap<String, Integer> hm_freq = new HashMap<String, Integer>();   // contains the frequency of each word
       // HashMap<String, Integer> hm_boolean = new HashMap<String, Integer>();// contains the boolean vector
	public DocumentKNN (ArrayList<String> line,String type) {
		this.topic= new String(type);
                for (int i=0;i<line.size();i++){  
                    if(!hm_freq.containsKey(line.get(i))){  
                          hm_freq.put(line.get(i),1 );
                          
                          
                    }
                    else{  
                        int a=hm_freq.get(line.get(i))+1;
                        hm_freq.replace(line.get(i), a);
                        
                    }
                
                }
                
        }
       
}
