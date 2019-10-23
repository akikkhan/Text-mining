/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package naive.bayes;

/**
 *
 * @author Jayati
 */
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
   static List<String> exclude = Arrays.asList("i","it","lt", "p", "gt","href", "rel", "quot","http", "www", "the","a","an", "i","you","she","he","it","its", "they","am","is" , "are", "was", "were","been","being", "have", "has", "had", "from", "to", "in", "on", "of", "or", "and", "but", "if", "into", "there", "this", "that", "myself","our","ours","ourselves","you","your","yours","yourself","youselves"+"him","his","himself","her","herself","its","itself","them","their","theirselves","what","which","how","who","whom");
  public List<ArrayList<String>> readXml(String path) {

    try {

	File fXmlFile = new File(path);
        List<ArrayList<String>> wordlist = new ArrayList<>();
	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	Document doc = dBuilder.parse(fXmlFile);

	
	doc.getDocumentElement().normalize();

	System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

	NodeList nList = doc.getElementsByTagName("row");

	System.out.println("----------------------------");
        
        System.out.println(nList.getLength());

	for (int temp = 0; temp<nList.getLength(); temp++) {

		Node nNode = nList.item(temp);
                String s= null;

		//System.out.println(temp);

		if (nNode.getNodeType() == Node.ELEMENT_NODE) {

			Element eElement = (Element) nNode;
                        s= eElement.getAttribute("Body");
                        s= s.replaceAll("<p>", "").replaceAll("</p>", "");
                        //s= s.replaceAll("</p>", "");
                        
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
                        
                        
                        
                        if(as.size()>0)
                        
                            wordlist.add(as);
                        
                        

			
			

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
        if(!exclude.contains(words[i]) || words[i].length()>0){
          if(words[i].matches("[A-Za-z0-9]+")){
             // wordlist.add(words[i]);
          }
          
          else if(words[i].matches("[A-Za-z0-9]+[^A-Za-z0-9]")){
              words[i]=words[i].substring(0, words[i].length()-1);
              wordlist.add(words[i]);
             
          }
          else if(words[i].matches("[A-Za-z0-9]+[^A-Za-z0-9][^A-Za-z0-9]+")){
              Pattern pattern = Pattern.compile("[^A-Za-z0-9][^A-Za-z0-9]+");
              Matcher matcher = pattern.matcher(words[i]);
              words[i]=matcher.replaceAll("");
              if(words[i].length()>1)
              
              //words[i]=words[i].substring(0, words[i].length()-2);
                wordlist.add(words[i]);
               //System.out.println(words[i]);
          }
          
          else if(words[i].matches("[A-Za-z0-9]+[^A-Za-z0-9]+([A-Za-z0-9]+[^A-Za-z0-9]*)+")){
              Pattern pattern = Pattern.compile("[^A-Za-z0-9]+");
              Matcher matcher = pattern.matcher(words[i]);
              words[i] = matcher.replaceAll(" ");
              String[] temp = words[i].split(" ");
              for(int j=0;j<temp.length;j++){
                  if(temp[j].length()>1){
                      wordlist.add(temp[j]);
                  }
              }
              
          }
          else if(words[i].matches("[^A-Za-z0-9]+(.*?)")){
          
          } 
          else if(words[i].length()>2){
              wordlist.add(words[i]);
          }
      }
      }
      //for(int i=0;i<wordlist.size();i++){
       //   System.out.println(wordlist.get(i));
      //}
     // System.out.println(wordlist.size());
      return wordlist;
  }

}