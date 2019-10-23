package knn;

import java.io.*;

import java.util.*;
import java.util.Map.Entry;
import java.util.regex.*;

public class KNN {

    static int no_of_classes = 0;
    static ArrayList<DocumentKNN> trainingDocs = new ArrayList<>();
    static HashMap<String, Integer> wordInDoc = new HashMap<>();
    static ArrayList<DocumentKNN> testDocs = new ArrayList<>();
    static double[] accuracy = new double[50];

    static int testDocsFromEachType = 50;//
  //  static int trainingDocsFromEachType = 500; // for selecting best similarity measure
    static int trainingDocInEachRun = 100;
    
    // double[] accuracy = new double();
    static ArrayList<Double> varArray = new ArrayList<>();

  
    public static void main(String[] args) throws IOException {

        String topicFile = "topics.txt";// this is the file containing the names of the topics
        File topics = new File(topicFile);
        
        File file = new File("Accuracies.txt");
        if(file.exists()){file.delete(); };
        if(!file.exists()){file.createNewFile(); }
        FileWriter fw = new FileWriter(file,true);
        

        try (BufferedReader br = new BufferedReader(new FileReader(topics))) {
            //read the names of the topics from the topicFile        

            while (br.readLine() != null) {
                no_of_classes++;
                
            }
            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] topicnames = new String[no_of_classes];
        //print("no1"+no_of_classes);
        try (BufferedReader br1 = new BufferedReader(new FileReader(new File(topicFile)))) {

            for (int i = 0; i < no_of_classes; i++) {
                topicnames[i] = br1.readLine();

            }
            for (int i = 0; i < no_of_classes; i++) {
                String trFilename = "./Data/Data/Training/" + topicnames[i] + ".xml";
            
                createDocs(topicnames[i], trFilename,0,500);
                //ReadXMLFile x = new ReadXMLFile();
                //x.readXml(trFilename, 0);

            }
            for (int i = 0; i < no_of_classes; i++) {

                String ttFilename = "./Data/Data/Test/" + topicnames[i] + ".xml";
                createTestDocs(topicnames[i], ttFilename);
               // ReadXMLFile x = new ReadXMLFile();
                //x.readXml(ttFilename, 0);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //print("no of tr data" + trainingDocs.size());

       
       // Collections.shuffle(trainingDocs);
        
       // print("                         Hamming                                                                               Eucledean                                                         Original Topic ");
       // print(" k=1                       k=3                              k=5                           k=1                    k=3                         k=5          ");

        
        int hk1p = 0;
        int hk1n = 0;
        int hk3p = 0;
        int hk3n = 0;
        int hk5p = 0;
        int hk5n = 0;
        
        int ek1p = 0;
        int ek1n = 0;
        int ek3p = 0;
        int ek3n = 0;
        int ek5p = 0;
        int ek5n = 0;
        
        int ck1p = 0;
        int ck1n = 0;
        int ck3p = 0;
        int ck3n = 0;
        int ck5p = 0;
        int ck5n = 0;
        //test
        for (int i = 0; i < testDocs.size(); i++) {
            
          String hk1 = new String(classifyDocHamming(testDocs.get(i), 1));
         /* String hk3 = new String(classifyDocHamming(testDocs.get(i), 3));
           String hk5 = new String(classifyDocHamming(testDocs.get(i), 5));*/
            
            String ek1 = new String(classifyDocEucledean(testDocs.get(i), 1));
           /* String ek3 = new String(classifyDocEucledean(testDocs.get(i), 3));
            String ek5 = new String(classifyDocEucledean(testDocs.get(i), 5));*/
           
            String ck1 = new String(classifyDocCosSim(testDocs.get(i), 1));
           /* String ck3 = new String(classifyDocCosSim(testDocs.get(i), 3));
           String ck5 = new String(classifyDocCosSim(testDocs.get(i), 5));*/

            String pre1 = new String(testDocs.get(i).topic);
            
            if(pre1.equals(hk1)) { hk1p++; }
            else { hk1n++;}
            
          /*  if(pre1.equals(hk3)) { hk3p++; }
            else { hk3n++;}
            
            if(pre1.equals(hk5)) { hk5p++; }
            else { hk5n++;}*/
            
            if(pre1.equals(ek1)) { ek1p++; }
            else { ek1n++;}
            
           /* if(pre1.equals(ek3)) { ek3p++; }
            else { ek3n++;}
            
            if(pre1.equals(ek5)) { ek5p++; }
            else { ek5n++;}*/
            
            if(pre1.equals(ck1)) { ck1p++; }
            else { ck1n++;}
            
            /*if(pre1.equals(ck3)) { ck3p++; }
            else { ck3n++;}
            
            if(pre1.equals(ck5)) { ck5p++; }
            else { ck5n++;}*/
            
            
           //  print("k=1:"+hk1 + "              " +"k=3:"+ hk3 + "                " + "k=5:"+hk5 + "        ||            " + "  k=1:"+ek1 + "           " + "k=3:"+ek3 + "             " + "k=5:"+ek5 + "              ||             " + pre1);
            //print(hk1 + "              " + hk3 + "                " + hk5 + "        ||            " + ek1 + "           " + ek3 + "             " + ek5 + "              ||             " + pre1);
         // print( " k=1:"+ck1 + "           " + "k=3:"+ck3 + "             " + "k=5:"+ck5 + "              ||             " + pre1);
        }
       
       // print("Accuracy hamming : k=1 "+ (1.00*hk1p)/(hk1p+hk1n));//+ " ,k=3 " + (1.00*hk3p)/(hk3p+hk3n) + " ,k= 5 " + (1.00*hk5p)/(hk5p+hk5n));
       // print("Accuracy euclid  : k=1 "+ (1.00*ek1p)/(ek1p+ek1n));//+ " ,k=3 " + (1.00*ek3p)/(ek3p+ek3n) + " ,k= 5 " + (1.00*ek5p)/(ek5p+ek5n));
       // print("Accuracy cosim   : k=1 "+ (1.00*ck1p)/(ck1p+ck1n));//+ " ,k=3 " + (1.00*ck3p)/(ck3p+ck3n) + " ,k= 5 " + (1.00*ck5p)/(ck5p+ck5n));
       
        int bestK=41;
        /*
        double acc = 0.0;
        for (int k=1;k<=50;k++) {
              int pos=0;
              int neg=0;
              for (int i = 0; i < testDocs.size(); i++) {
                   String ck1 = new String(classifyDocCosSim(testDocs.get(i), k));
                   String pre1 = new String(testDocs.get(i).topic);
                   if(pre1.equals(ck1)) { pos++; }
                   else { neg++;}             
                  
              }
              double curAcc= (double)pos/(pos+neg);
              print(" k = " + k + " acc = " +curAcc );
              if(curAcc>acc) {  
                     acc= curAcc;
                     bestK = k;
              }
        }*/
        
       // print("best k = " +bestK + " with accuracy : "+ acc );
        
        
        for (int i=0;i<trainingDocs.size();i++){  
              trainingDocs.remove(i);
        
        }
       // print("doc deleted");
        
        for (int r= 0;r<50 ; r++) { 
           // print("inside " + r);
            int pos=0;
            int neg=0;
            try (BufferedReader br1 = new BufferedReader(new FileReader(new File(topicFile)))) {
                //print("inside try"+r);
                for (int i = 0; i < no_of_classes; i++) {
                String trFilename = "./Data/Data/Training/" + topicnames[i] + ".xml";
            
                createDocs(topicnames[i], trFilename,r,100);
                
            
            }
            br1.close();
            for (int l = 0; l < testDocs.size(); l++) {
                   String ck1 = new String(classifyDocCosSim(testDocs.get(l), bestK));
                   String pre1 = new String(testDocs.get(l).topic);
                   if(pre1.equals(ck1)) { pos++; }
                   else { neg++;}             
                  
                }
            
            //print("in run " + r);
            double curAcc= (double)pos*100/(pos+neg);
            accuracy[r] =curAcc;
            fw.write(Double.toString(accuracy[r]));
            fw.write("\n");
            fw.flush();
             print("acc in run"+ r + " is =" +curAcc);
             
             for (int i=0;i<trainingDocs.size();i++){  
              trainingDocs.remove(i);
        
            }
           // print("doc removed inside " +r);
            } catch (IOException e) {            
            e.printStackTrace();
            
            
            
            
            
            
            
        }
      }
    
    }

    static void createDocs(String topic, String filename,int r,int trainingDocsFromEachType ) {
        /*
        File f1 = new File(filename);
        int totalData = 0;
        String line = new String();
        try (BufferedReader br1 = new BufferedReader(new FileReader(f1))) {
            br1.readLine();// for escaping the first line
            while ((line = br1.readLine()) != null) {
                totalData++;
            }
            // System.out.println("Total data =  " + totalData);
            br1.close();

        } catch (IOException e1) {
            e1.printStackTrace();
        }

        try (BufferedReader br1 = new BufferedReader(new FileReader(f1))) {
            String input= br1.readLine();
                for(int g=0;g<r*100;g++){ 
                    input =br1.readLine(); 
               }

            for (int m = 0; m < trainingDocsFromEachType ; m++) {
                
                 input = br1.readLine();
                if (input.contains("<row")) {

                    String pattern1 = "Body=";
                    String pattern2 = "OwnerUserId=";

                    List<String> strings = Arrays.asList(input.replaceAll("^.*?pattern1", "")
                            .split("pattern2.*?(pattern1|$)"));

                    String s = "";
                    Pattern p = Pattern.compile(Pattern.quote(pattern1) + "(.*?)" + Pattern.quote(pattern2));
                    Matcher k = p.matcher(input);
                    while (k.find()) {
                        //System.out.println(k.group(1));
                        s = s + k.group(1);
                    }
                    //System.out.println(  s);

                    String s1 = "";
                    StringTokenizer st = new StringTokenizer(s.toLowerCase(), " ");
                    String starr[] = new String[st.countTokens()];
                    String exclude[] = new String[]{"lt", "p", "gt", "href", "rel", "quot", "http", "www", "the", "a", "an", "i", "you", "she", "he", "it", "its", "they", "am", "is", "are", "was", "were", "been", "being", "have", "has", "had", "from", "to", "in", "on", "of", "or", "and", "but", "if", "into", "there", "this", "that" };//+ "myself" + "our" + "ours" + "ourselves" + "you" + "your" + "yours" + "yourself" + "youselves" + "him" + "his" + "himself" + "her" + "herself" + "its" + "itself" + "them" + "their" + "theirselves" + "what" + "which" + "how", "who" + "whom"};
                    ArrayList<String> docWords = new ArrayList<String>();
                    while (st.hasMoreTokens()) {
                        String check = new String(st.nextToken());
                        if (check.matches("[A-Za-z0-9]+") && !Arrays.asList(exclude).contains(check)) {
                            docWords.add(check);
                        } else if (check.matches("[A-Za-z0-9]+[^A-Za-z0-9]") && !Arrays.asList(exclude).contains(check)) {
                            check = check.substring(0, check.length() - 1);
                            docWords.add(check);
                        }

                    }

                //String exclude[] = new String[]{"lt", "p", "gt", "href", "rel", "quot", "xa", "http", "www", "a", "an", "the", "i", "you", "he", "she", "they", "it", "am", "is", "are", "was", "were", "have", "has", "had", "from", "to", "in", "on", "of", "or", "and", "but", "if", "into", "there", "this", "that"+ "myself"+"our"+"ours"+"ourselves"+"you"+"your"+"yours"+"yourself"+"youselves"+"him"+"his"+"himself"+"her"+"herself"+"its"+"itself"+"them"+"their"+"theirselves"+"what"+"which"+"who"+"whom"};
                    // print("******************");
                    //for (String e : docWords) {
                        // System.out.println(e);
                   // }
                           //print("******************");

                    if (docWords.size() > 0) {
                        DocumentKNN d = new DocumentKNN(docWords, topic);

                        trainingDocs.add(d);
                    }// else {
                                 // print("small found");

                   // }
                    

                }

            }
        br1.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        for (DocumentKNN d:trainingDocs) {  
               Set m1 = d.hm_freq.entrySet();    
               Iterator it1 = m1.iterator();
               //print("inside loop1");
        // ...
                while (it1.hasNext()) {
                    Map.Entry pair = (Map.Entry) it1.next();
                    if (!wordInDoc.containsKey(pair.getKey())) {
                              wordInDoc.put((String)pair.getKey(), 1);
                             // print((String)pair.getKey());

                    }
                    else {  
                 
                       int a=wordInDoc.get((String)pair.getKey())+1;
                        wordInDoc.put((String)pair.getKey(),a);
                       // print((String)pair.getKey());
            
                    }
                   // print("inside loop2");
               }
        
        }*/
        ReadXMLFile rxf = new ReadXMLFile();
        List<ArrayList<String>> l = new ArrayList<>();
        l = rxf.readXml(filename, r);
        for(int i=0;i<l.size();i++){
            DocumentKNN d = new DocumentKNN(l.get(i), topic);

            trainingDocs.add(d);
        }
        
    }

    static void createTestDocs(String topic, String filename) {
        /*
        File f1 = new File(filename);
        int totalData = 0;

        try (BufferedReader br1 = new BufferedReader(new FileReader(f1))) {
            
            for (int m = 0; m < testDocsFromEachType; m++) {//**********************8
                String input = br1.readLine();
                if (input.contains("<row")) {

                    String pattern1 = "Body=";
                    String pattern2 = "OwnerUserId=";

                    List<String> strings = Arrays.asList(input.replaceAll("^.*?pattern1", "")
                            .split("pattern2.*?(pattern1|$)"));

                    String s = "";
                    Pattern p = Pattern.compile(Pattern.quote(pattern1) + "(.*?)" + Pattern.quote(pattern2));
                    Matcher k = p.matcher(input);
                    while (k.find()) {
                        //System.out.println(k.group(1));
                        s = s + k.group(1);
                    }
                    //System.out.println(  s);

                    String s1 = "";
                    StringTokenizer st = new StringTokenizer(s.toLowerCase(), " ");
                    String starr[] = new String[st.countTokens()];
                    String exclude[] = new String[]{"lt", "p", "gt", "href", "rel", "quot", "http", "www", "the", "a", "an", "i", "you", "she", "he", "it", "its", "they", "am", "is", "are", "was", "were", "been", "being", "have", "has", "had", "from", "to", "in", "on", "of", "or", "and", "but", "if", "into", "there", "this", "that" };//+ "myself" + "our" + "ours" + "ourselves" + "you" + "your" + "yours" + "yourself" + "youselves" + "him" + "his" + "himself" + "her" + "herself" + "its" + "itself" + "them" + "their" + "theirselves" + "what" + "which" + "how", "who" + "whom"};
                    ArrayList<String> docWords = new ArrayList<String>();
                    while (st.hasMoreTokens()) {
                        String check = new String(st.nextToken());
                        if (check.matches("[A-Za-z0-9]+") && !Arrays.asList(exclude).contains(check)) {
                            docWords.add(check);
                        } else if (check.matches("[A-Za-z0-9]+[^A-Za-z0-9]") && !Arrays.asList(exclude).contains(check)) {
                            check = check.substring(0, check.length() - 1);
                            docWords.add(check);
                        }

                    }

                //String exclude[] = new String[]{"lt", "p", "gt", "href", "rel", "quot", "xa", "http", "www", "a", "an", "the", "i", "you", "he", "she", "they", "it", "am", "is", "are", "was", "were", "have", "has", "had", "from", "to", "in", "on", "of", "or", "and", "but", "if", "into", "there", "this", "that"+ "myself"+"our"+"ours"+"ourselves"+"you"+"your"+"yours"+"yourself"+"youselves"+"him"+"his"+"himself"+"her"+"herself"+"its"+"itself"+"them"+"their"+"theirselves"+"what"+"which"+"who"+"whom"};
                    // print("******************");
                    for (String e : docWords) {
                        //System.out.println(e);
                    }
                           //print("******************");

                    if (docWords.size() > 0) {
                        DocumentKNN d = new DocumentKNN(docWords, topic);

                        testDocs.add(d);
                    } else {
                                 // print("small found");

                    }

                }

            }

        } catch (IOException e1) {
            e1.printStackTrace();
        }
                */
        ReadXMLFile rxf = new ReadXMLFile();
        List<ArrayList<String>> l = new ArrayList<>();
        l = rxf.readXml(filename, 0);
        for(int i=0;i<l.size();i++){
            DocumentKNN d = new DocumentKNN(l.get(i), topic);

            testDocs.add(d);
        }
    }

    static int findHammingDis(DocumentKNN training, DocumentKNN test) {
        int d = 0;

        Set m1 = training.hm_freq.entrySet();
        Set m2 = test.hm_freq.entrySet();
        Iterator it1 = m1.iterator();
        Iterator it2 = m2.iterator();
        // ...
        while (it1.hasNext()) {
            Map.Entry pair = (Map.Entry) it1.next();
            if (!test.hm_freq.containsKey(pair.getKey())) {
                d++;

            }
        }

        while (it2.hasNext()) {
            Map.Entry pair1 = (Map.Entry) it2.next();
            if (!training.hm_freq.containsKey(pair1.getKey())) {
                d++;

            }
        }

        return d;

    }
    

    static double findEucledeanDis(DocumentKNN training, DocumentKNN test) {

        double d = 0.0;

        Set set1 = training.hm_freq.entrySet();
        Set set2 = test.hm_freq.entrySet();
        Iterator iterator = set1.iterator();
        Iterator iterator1 = set2.iterator();
        while (iterator.hasNext()) {
            Map.Entry pair = (Map.Entry) iterator.next();
            int diff = 0;
            if (!test.hm_freq.containsKey(pair.getKey())) {
                diff = (Integer) pair.getValue();
                d = d + Math.pow(diff, 2);
                // print("not present");

            } else {
                diff = (Integer) pair.getValue() - test.hm_freq.get(pair.getKey());
                d = d + Math.pow(diff, 2);
                // print(" present");
            }

        }
        while (iterator1.hasNext()) {
            Map.Entry pair1 = (Map.Entry) iterator1.next();
            int diff = 0;
            if (!training.hm_freq.containsKey(pair1.getKey())) {
                diff = (Integer) pair1.getValue();
                d = d + Math.pow(diff, 2);

            } 

            
        }

        return Math.sqrt(d);

    }
    
static double findCosSim(DocumentKNN training, DocumentKNN test) { 
    
       

       double cosSim = 0.0;
       int totalWordsTraining =0;
       int totalWordsTest =0;
       
       Set set1 = training.hm_freq.entrySet();
        Set set2 = test.hm_freq.entrySet();
        Iterator iterator = set1.iterator();
        Iterator iterator1 = set2.iterator();
        
        //find total words of each document
        while (iterator.hasNext()) {
            Map.Entry pair = (Map.Entry) iterator.next();
            totalWordsTraining = totalWordsTraining + training.hm_freq.get((String)pair.getKey());
            // print("inside loop3");
        }
       
        while (iterator1.hasNext()) {
            Map.Entry pair1 = (Map.Entry) iterator1.next();
            totalWordsTest = totalWordsTest + test.hm_freq.get((String)pair1.getKey());
            // print("inside loop4");
            
        }
       
       
        // create two hashmaps to contain the corresponding TF-IDF value of a word
         HashMap<String, Double> trainingTfIdf = new HashMap<>();
         HashMap<String, Double> testTfIdf = new HashMap<>();
         
         double modTest = 0.0;
         double modTraining  = 0.0;
         
         // find TF IDF value of each word in training document and put it in the hashmap
         Set set3 = training.hm_freq.entrySet();
         Iterator iterator3 = set3.iterator();
         while (iterator3.hasNext()) {
            Map.Entry pair = (Map.Entry) iterator3.next();
            //totalWordsTraining = totalWordsTraining + training.hm_freq.get((String)pair.getKey());
            double tf = (double)training.hm_freq.get((String)pair.getKey())/totalWordsTraining;
            double m=0.0;
            if(!wordInDoc.containsKey((String)pair.getKey())){ 
                    m=(double)(trainingDocs.size());
            }
            else { 
                    m=(double)trainingDocs.size()/(1.00+wordInDoc.get((String)pair.getKey()));
            }
            
            //print("m"+m);
           
            double idf = Math.log10(m);
           // testTfIdf.put((String)pair.getKey(), tf*idf);
           // double idf = Math.log10((double)(trainingDocs.size())/(1.00+wordInDoc.get((String)pair.getKey())));
            trainingTfIdf.put((String)pair.getKey(), tf*idf);
            modTraining = modTraining+Math.pow(tf*idf, 2);
            // print("inside loop5");
        
         }
         
         // find TF IDF value of each word in test document and put it in the hashmap
         Set set4 = test.hm_freq.entrySet();
         Iterator iterator4 = set4.iterator();
         while (iterator4.hasNext()) {
            Map.Entry pair = (Map.Entry) iterator4.next();
            //totalWordsTraining = totalWordsTraining + training.hm_freq.get((String)pair.getKey());
            double tf = (double)test.hm_freq.get((String)pair.getKey())/totalWordsTraining;
            //print("tf"+tf);
            double m=0.0;
            if(!wordInDoc.containsKey((String)pair.getKey())){ 
                    m=(double)(trainingDocs.size());
            }
            else { 
                    m=(double)trainingDocs.size()/(1.00+wordInDoc.get((String)pair.getKey()));
            }
            
            //print("m"+m);
           
            double idf = Math.log10(m);
            testTfIdf.put((String)pair.getKey(), tf*idf);
            modTest = modTest+Math.pow(tf*idf, 2);
            // print("inside loop6");
        
         }
         modTraining = Math.sqrt(modTraining);
         modTest  = Math.sqrt(modTest);
         
         
         double temp = 0.0;
         Set set5= testTfIdf.entrySet();
         Iterator iterator5 = set5.iterator();
         while (iterator5.hasNext()) {
            Map.Entry pair = (Map.Entry) iterator5.next();
            
            // print("inside loop7");
            
            if (trainingTfIdf.containsKey(pair.getKey())) {
                temp = temp + testTfIdf.get((String)pair.getKey())*trainingTfIdf.get((String)pair.getKey());
                // print("not present");

            } 

        }
                
         
      cosSim =  temp/(modTraining*modTest);
        //print("cosSim is :" + cosSim );
       
       return cosSim;

}

    
    static String classifyDocHamming(DocumentKNN test, int k) { // k is the number of nearest neighbours we need
        String preClass = new String();
        HashMap< Integer, String> hm_dis = new HashMap<>();
        // create our map
        Map<Integer, List<String>> hm_dis1 = new HashMap<Integer, List<String>>();    
        

        for (DocumentKNN training : trainingDocs) {
            //hm_dis.put(findHammingDis(training, test), training.topic);
            int d = findHammingDis(training, test);
            List<String> topics = new ArrayList<String>();
            List<String> temp = new ArrayList<String>();
            if(hm_dis1.containsKey(d)){
            topics =  hm_dis1.get(d);
             topics.add(training.topic) ;
             hm_dis1.put(d, topics);
            }
            else{ 
                topics.add(training.topic);
                 hm_dis1.put(d, topics);
                  
            }
                              

        }

        List sortedKeys = new ArrayList(hm_dis1.keySet());
        Collections.sort(sortedKeys);
        if (k == 1) {
            //preClass = (String) hm_dis.get(sortedKeys.get(0));
            preClass = (String) hm_dis1.get(sortedKeys.get(0)).get(0);
            return preClass;

        } else {
            ArrayList<String> list = new ArrayList<>(k);
            int m=0;
            
            for(int i = 0 ;i< k ;i++ ) {   
                  for (int j =0 ;j<hm_dis1.get((Integer) sortedKeys.get(i)).size();j++) { 
                        list.add(hm_dis1.get((Integer) sortedKeys.get(i)).get(j));
                        m++;
                        if(m==k) { 
                            preClass = mostOccured(list);
                            return preClass;
                        }
                  
                  }
                  
            
            
            }
                 
            

            preClass = mostOccured(list);
            return preClass;
        }

        
    }
    static String classifyDocEucledean(DocumentKNN test, int k) { // k is the number of nearest neighbours we need
        String preClass = new String();
       
        Map<Double, List<String>> e_dis1 = new HashMap<Double, List<String>>();    
        

        for (DocumentKNN training : trainingDocs) {
            //hm_dis.put(findHammingDis(training, test), training.topic);
            double d = findEucledeanDis(training, test);
            List<String> topics = new ArrayList<String>();
            List<String> temp = new ArrayList<String>();
            if(e_dis1.containsKey(d)){
            topics =  e_dis1.get(d);
             topics.add(training.topic) ;
             e_dis1.put(d, topics);
            }
            else{ 
                topics.add(training.topic);
                 e_dis1.put(d, topics);
                  
            }
                              

        }

        List sortedKeys = new ArrayList(e_dis1.keySet());
        Collections.sort(sortedKeys);
        if (k == 1) {
            //preClass = (String) hm_dis.get(sortedKeys.get(0));
            preClass = (String) e_dis1.get(sortedKeys.get(0)).get(0);
            return preClass;

        } else {
            ArrayList<String> list = new ArrayList<>(k);
            int m=0;
            
            for(int i = 0 ;i< k ;i++ ) {   
                  for (int j =0 ;j<e_dis1.get((Double) sortedKeys.get(i)).size();j++) { 
                        list.add(e_dis1.get((Double) sortedKeys.get(i)).get(j));
                        m++;
                        if(m==k) { 
                            preClass = mostOccured(list);
                            return preClass;
                        }
                  
                  }
                  
            
            
            }
                 
            

            preClass = mostOccured(list);
            return preClass;
        }

        
    }
    static String classifyDocCosSim(DocumentKNN test, int k) { // k is the number of nearest neighbours we need
        String preClass = new String();
        HashMap< Integer, String> cs_dis = new HashMap<>();
        // create our map
        Map<Double, List<String>> cs_dis1 = new HashMap<Double, List<String>>();    
        

        for (DocumentKNN training : trainingDocs) {
            //hm_dis.put(findHammingDis(training, test), training.topic);
            double d = findCosSim(training, test);
            List<String> topics = new ArrayList<String>();
            List<String> temp = new ArrayList<String>();
            if(cs_dis1.containsKey(d)){
            topics =  cs_dis1.get(d);
             topics.add(training.topic) ;
             cs_dis1.put(d, topics);
            }
            else{ 
                topics.add(training.topic);
                 cs_dis1.put(d, topics);
                  
            }
                              

        }

        List sortedKeys = new ArrayList(cs_dis1.keySet());
        Collections.sort(sortedKeys);//in ascending order
        Collections.reverse(sortedKeys);// in descending order
        if (k == 1) {
            //preClass = (String) hm_dis.get(sortedKeys.get(0));
            preClass = (String) cs_dis1.get(sortedKeys.get(0)).get(0);
            return preClass;

        } else {
            ArrayList<String> list = new ArrayList<>(k);
            int m=0;
            
            for(int i = 0 ;i< k ;i++ ) {   
                  for (int j =0 ;j<cs_dis1.get((Double) sortedKeys.get(i)).size();j++) { 
                        list.add(cs_dis1.get((Double) sortedKeys.get(i)).get(j));
                        m++;
                        if(m==k) { 
                            preClass = mostOccured(list);
                            return preClass;
                        }
                  
                  }
                  
            
            
            }
                 
            

            preClass = mostOccured(list);
            return preClass;
        }

        
    }


    

    static String mostOccured(ArrayList<String> s) {

        String s1 = new String();

        Map<String, Integer> stringsCount = new HashMap<>();
        for (int i = 0; i < s.size(); i++) {
            if (!stringsCount.containsKey(s.get(i))) {
                stringsCount.put(s.get(i), 1);
                // print("got");

            } else {
                int a = stringsCount.get(s.get(i)) + 1;
                stringsCount.put(s.get(i), a);
                //print("got2");

            }

        }
        List sortedValues = new ArrayList(stringsCount.values());

        Collections.sort(sortedValues);
        Collections.reverse(sortedValues);

        int g = (Integer) sortedValues.get(0);
        //print("highest occurance:"+g);

        Set m1 = stringsCount.entrySet();
        ArrayList<String> breakTie =  new ArrayList<String>();
        

        Iterator it1 = m1.iterator();

        while (it1.hasNext()) {
            Map.Entry pair = (Map.Entry) it1.next();
           // s1 = new String((String) pair.getKey());
            // print("key is:" + s1);
            if ((Integer) pair.getValue() == (Integer) sortedValues.get(0)) {
                // print("inside main");              
                //return s1;
                breakTie.add((String) pair.getKey()); // most occured Topic names are added
            }

        }
        int t = breakTie.size();
        Random d = new Random();
        int c = d.nextInt(t);// randomly choose one topic that are most occured
          

        return breakTie.get(c);

    }

    static void print(String k) {
        System.out.println(k);
    }

}
