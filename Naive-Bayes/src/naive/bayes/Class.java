/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package naive.bayes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Jayati
 */
public class Class {
    String topic;
    int wordcount=0;
    double prior;
    double _alpha = 0.1;
    Map<String,Integer> wordmap = new HashMap();
    Map<String,Double> probability= new HashMap();
    
    public Class(){
        
    }
    
    public static Class buildClass(List<Document> documents,String topic){
        Class cs = new Class();
        cs.topic= topic;
        for(int i=0;i<documents.size();i++){
            Document doc= documents.get(i);
            cs.wordcount += doc.length;
            for(Map.Entry<String,Integer> entry: doc.tokens.entrySet()){
                String word = entry.getKey();
                Integer count = entry.getValue();
                Integer wcount = cs.wordmap.get(word);
                if(wcount == null){
                    wcount = (Integer) 0;
                }
                cs.wordmap.put(word,(Integer) wcount+count);
            }
        }
        for(Map.Entry<String,Integer> entry: cs.wordmap.entrySet()){
            String word = entry.getKey();
            Integer count = entry.getValue();
            cs.probability.put(word, Math.log10((double) (count+cs._alpha) / (double) (cs.wordcount+cs.wordmap.size()*cs._alpha)));
        } 
        return cs;
    }
    
}
