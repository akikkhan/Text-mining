/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package naive.bayes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Jayati
 */
public class Document {
    public String topic;
    public Map<String, Integer> tokens= new HashMap<>();
    public int length;
    
    public Document() {
        
    }
    
    public Document buildDocument(ArrayList<String> words,String topic){
        Document doc= new Document();
        doc.topic= topic;
        doc.length= words.size();
        Integer counter;
        for(int i=0;i<words.size();++i) {
            counter = doc.tokens.get(words.get(i));
            if(counter==null) {
                counter=(Integer) 0;
            }
            doc.tokens.put(words.get(i), ++counter);
        }
        //for(Map.Entry<String,Integer> entry: tokens.entrySet()){
        //    System.out.println("token: "+entry.getKey()+" | value: "+entry.getValue());
        //}
        return doc;
    }
}
