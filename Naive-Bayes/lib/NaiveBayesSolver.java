/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package naive.bayes;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Jayati
 */
public class NaiveBayesSolver {
    
    List<Document> documents = new ArrayList<>();
    List<Class> classes = new ArrayList<>();
    Map<String,Integer> classCounts = new HashMap();
    double _alpha = 0.5;
    
    public NaiveBayesSolver(){
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        NaiveBayesSolver nbs= new NaiveBayesSolver();
        File trainDataPath = new File("./Data/Data/Training");
        File testFile = new File("./Data/Data/Test/3d_Printer.xml");
        File [] files = trainDataPath.listFiles();
        List<ArrayList<String>> l;
        ReadXMLFile rxf= new ReadXMLFile();
        System.out.println(files.length);
        for (int i = 0; i < files.length; i++){
                if (files[i].isFile()){
                    System.out.println(files[i].getName());
                    l= rxf.readXml(files[i].getAbsolutePath());
                    
                   // ArrayList<String> ass= l.get(2);
                   // for(int j=0;j<ass.size();j++){
                   //     System.out.println(ass.get(j));
                    //}
                    String name=files[i].getName();
                    // String name = "Arduino";
                   name = name.replace(".xml", "");
                    nbs.listDocuments(l, name);
                    
                    
                }
                
            }
        for(Map.Entry<String, Integer> entry : nbs.classCounts.entrySet()){
            System.out.println("class: "+entry.getKey()+" | Value: "+entry.getValue());
            
        }
        nbs.listClasses();
        
        /*
        for(int i=0;i<nbs.classes.size();i++){
            System.out.println("class: "+nbs.classes.get(i).topic+" | prior: "+nbs.classes.get(i).prior);
            for(Map.Entry<String,Integer> entry : nbs.classes.get(i).wordmap.entrySet()){
                String word = entry.getKey();
                Integer count = entry.getValue();
                System.out.println("word : "+word+" | count: "+count);
            }
        }*/
        
        l=rxf.readXml(testFile.getAbsolutePath());
        List<Document> docs;
        docs= nbs.listDocuments(l);
        for(int i=0;i<docs.size();i++){
            String topic = nbs.checkDocument(docs.get(i));
            
            System.out.println("document : "+i+" | topic: "+topic+" | length : "+docs.get(i).length);
        }
        
    }
    
    
    public void listDocuments(List<ArrayList<String>> l,String topic){
      
        for(int i=0; i<l.size();i++){
            ArrayList<String> as= l.get(i);
            Document doc= new Document();
            doc= doc.buildDocument(as, topic);
            documents.add(doc);
            Integer count = classCounts.get(topic);
            if(count == null){
                count = (Integer) 0;
            }
            
            classCounts.put(topic, ++count);
            
        }
        
    }
    
    public List<Document> listDocuments(List<ArrayList<String>> l){
        List<Document> docs = new ArrayList<>();
        for(int i=0; i<l.size();i++){
            ArrayList<String> as= l.get(i);
            Document doc= new Document();
            doc= doc.buildDocument(as, null);
            docs.add(doc);
        }
        return docs;
    }
    
    public void listClasses(){
        for(Map.Entry<String,Integer> entry: classCounts.entrySet()){
            String topic = entry.getKey();
            Integer count = entry.getValue();
            List<Document> docs;
            docs = classDocs(topic);
            Class cs = Class.buildClass(docs, topic);
            cs.prior = (double) docs.size()/(double) documents.size();
            classes.add(cs);
            
        }
    }
    
    public List<Document> classDocs(String topic){
        List<Document> docs= new ArrayList<>();
        for(int i=0;i<this.documents.size();i++){
            if(documents.get(i).topic==topic){
                docs.add(documents.get(i));
            }
        }
        return docs;
    }
    
    public String checkDocument(Document doc){
        String topic=null;
        double probability = Double.NEGATIVE_INFINITY;
        double temp1,tempp=0.0;
        for(int i=0;i<classes.size();i++){
            Class cs = new Class();
            cs= classes.get(i);
            temp1 = cs.prior;
            for(Map.Entry<String,Integer> entry : doc.tokens.entrySet()){
                String word = entry.getKey();
                Integer count = cs.wordmap.get(word);
                if(count==null){
                    tempp = Math.log10(cs._alpha/(double)(cs.wordcount+cs.wordmap.size()*cs._alpha));
                    temp1+=tempp;
                }
                else
                
                    temp1 += cs.probability.get(word);
               
            }
            
           // System.out.println(temp1);
            if(temp1> probability){
                probability = temp1;
                topic = cs.topic;
            }
        }
        return topic;
    }
}
