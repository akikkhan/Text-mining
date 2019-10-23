
package knn;


import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.w3c.dom.Attr;

public class ReadXMLFile {
    
  //static List<String> exclude = Arrays.asList( "i","it","can", "p", "gt","href", "rel", "quot","http", "www", "the",  "you", "she", "they",  "are", "was", "were","been","being", "have", "has", "had", "from", "to", "in", "on", "of", "or", "and", "but", "if", "into", "there", "this", "that", "myself","our","ours","ourselves","you","your","yours","yourself","youselves","him","his","himself","her","herself","its","itself","them","their","theirselves","what","which","who","whom");
   static List<String> exclude = Arrays.asList("i","it","lt", "p", "gt","href", "rel", "quot","http", "www", "the","a","an", "you","she","he","it","its", "they","am","is" , "are", "was", "were","been","being", "have", "has", "had", "from", "to", "in", "on", "of", "or", "and", "but", "if", "into", "there", "this", "that");
  public List<ArrayList<String>> readXml(String path,int turn) {

    try {

	File fXmlFile = new File(path);
        List<ArrayList<String>> wordlist = new ArrayList<>();
	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	Document doc = dBuilder.parse(fXmlFile);

	
	doc.getDocumentElement().normalize();

	//System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

	NodeList nList = doc.getElementsByTagName("row");
        //if(path.contains("Test"))
	//System.out.println("----------------------------");
        int tempStart=0,tempEnd=50;
        //System.out.println(nList.getLength());
        if(path.contains("Training")){
            tempStart = turn*100;
            tempEnd = tempStart + 100;
            
        }
        //System.out.println("Start : "+tempStart+" & End : "+tempEnd);

	for (int temp = tempStart; temp<tempEnd; temp++) {

		Node nNode = nList.item(temp);
               // String s= null;
                
		//System.out.println(temp);

		if (nNode.getNodeType() == Node.ELEMENT_NODE) {

			Element eElement = (Element) nNode;
                        String s= eElement.getAttribute("Body");
                        //if(path.contains("3d_Printer") && path.contains("Training")){
                            //System.out.println(s);
                        //}
                        s= s.replaceAll("<p>", "");
                        s= s.replaceAll("</p>", "");
                        s= s.replaceAll("\n", " ");
                        
                        //System.out.println(s);
                        
                        
                        Pattern pattern = Pattern.compile("<(.*?)>");
                        Matcher matcher = pattern.matcher(s);
                        if (matcher.find())
                        {
                            s=matcher.replaceAll("");
                           
                        }
                        
                       // System.out.println(s);
                        /*
                        pattern = Pattern.compile(".*[ ][ ]+.*");
                        matcher = pattern.matcher(s);
                        if(matcher.find()){
                            s= matcher.replaceAll(" ");
                        }*/
                        //System.out.println(s);
                        
                        ArrayList<String> as = new ArrayList<String>();
                        
                        as=parseWords(s);
                        
                        
                        
                        if(as.size()>0 && path.contains("Training") ){
                        
                            wordlist.add(as);
                            
                            
                        }
                        
                        else if(path.contains("Test")){
                            wordlist.add(as);
                            
                            
                        }
                        
                                
			
			

		}
               

	}
        return wordlist;
    } catch (Exception e) {
	e.printStackTrace();
    }
    return null;
  }
  
  public static ArrayList<String> parseWords(String s){
      ArrayList<String> wordlist = new ArrayList<String>();
      String[] words= s.split(" ");
      for(int i=0;i<words.length;i++){
        words[i] = words[i].toLowerCase().trim();
        //System.out.println(words[i]);
        if(!exclude.contains(words[i]) && words[i].length()>1){
          if(words[i].matches("[A-Za-z0-9]+")){
              words[i] = words[i].trim();
              if(words[i].length()>1 && !exclude.contains(words[i]))
              wordlist.add(words[i]);
          }
          /*
          else if(words[i].matches("[A-Za-z0-9]+[^A-Za-z0-9]")){
              words[i]=words[i].substring(0, words[i].length()-1);
               if(words[i].length()>1 && !exclude.contains(words[i]))
                wordlist.add(words[i]);
             
          }*/
          else{
          boolean check=false;
          if(words[i].matches("[A-Za-z0-9]+[^A-Za-z0-9]+")){
              Pattern pattern = Pattern.compile("[^A-Za-z0-9]+");
              Matcher matcher = pattern.matcher(words[i]);
              words[i]=matcher.replaceAll("").trim();
              if(words[i].length()>1 && !exclude.contains(words[i]))
              wordlist.add(words[i]);
               
          }
          
          else if(words[i].matches("[^A-Za-z0-9]*[A-Za-z0-9]+[^A-Za-z0-9]*")){
              check = true;
              //System.out.println(words[i]);
              Pattern pattern = Pattern.compile("[^A-Za-z0-9]+");
              Matcher matcher = pattern.matcher(words[i]);
              words[i] = matcher.replaceAll(" ");
              String[] temp = words[i].split(" ");
              for(int j=0;j<temp.length;j++){
                  temp[j]= temp[j].trim();
                   if(words[i].length()>1 && !exclude.contains(words[i])){
                      wordlist.add(temp[j]);
                      //System.out.println(temp[j]);
                  }
              }
              
              
          }
          
          /*
          if(check==false && words[i].length()>2 && !exclude.contains(words[i])){
              wordlist.add(words[i].trim());
          }*/
      }
        }
      }
      
      /*
      for(int i=0;i<wordlist.size();i++){
          System.out.println(wordlist.get(i));
      }*/
     // System.out.println(wordlist.size());
      return wordlist;
  }

}