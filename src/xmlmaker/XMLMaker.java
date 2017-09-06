package xmlmaker;
import com.sun.javafx.scene.control.skin.VirtualFlow;
import java.util.*;  

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;

public class XMLMaker {
      private static Document document;
      private static ArrayList<TermModel> termModels = new ArrayList<TermModel>() {};
      private static HashMap<String, String> myText  = new HashMap<String, String>();
      private static HashMap<String, String> myPolarity  = new HashMap<String, String>();     
      private static HashMap<String, String> myCategory  = new HashMap<String, String>();
      private static List<String> myAspectKeys = new ArrayList<String>();
      private static List<String> myCategoryKeys = new ArrayList<String>();
      
      private static int counter = 0;
     
      private static String getText(String str){
          
          String arr [] = getSplitString(str);
          StringBuilder btr = new StringBuilder();
          for (int i=4; i<arr.length; i++){
              btr.append(arr[i]);
              btr.append(" ");              
          }
         //System.out.println("text: "+btr.toString());            
          return btr.toString();
      }
  
    public static void main(String[] args) {
        // TODO code application logic here
         
         DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    try {
      DocumentBuilder builder = factory.newDocumentBuilder();
      document = builder.newDocument();
    }catch (ParserConfigurationException parserException) {
      parserException.printStackTrace();
    }
    
     Element root = document.createElement("root");
    document.appendChild(root);
      
    String filePath = "C:\\Users\\Owner\\Documents\\NetBeansProjects\\XMLMaker\\src\\xmlmaker\\ana.txt";
        
        try{
        List<String> lines = Files.readAllLines(Paths.get(filePath), StandardCharsets.UTF_8);
        //System.out.println(lines.get(0));
        //int counter = 0;
            
        for (int i= 0; i< lines.size(); i++){
            
            String str [] = getSplitString(lines.get(i));
            //System.out.println(str[0].length());
                
           // String myTextString = "";
                        
            if(str[1].equalsIgnoreCase("AspectCategory")){                
                //myTextString = getText(lines.get(i));
                
                myText.put(str[0], getText(lines.get(i)));
              //  System.out.println(str[0]);
                
                //System.out.print(myTextString);
                 
            } else if (str[1].equalsIgnoreCase("Category")) {
                myCategory.put(str[2], str[3]);
                
            } else if (str[1].equalsIgnoreCase("CategoryPolarity")) {
                myPolarity.put(str[2], str[3]);
                myCategoryKeys.add(str[2]);
//                myText.put(str[2], getText(lines.get(i))+"ovi");
//                //System.out.print("key: "+str[2]);
            } else {
                if(lines.get(i).startsWith("T")){                       
                whenStartsWithT(lines.get(i));                
            } else if (lines.get(i).startsWith("A")){
                whenStartsWithA(lines.get(i));
            }
                
            }
             
        }
        
        // make single node

        }catch(IOException ex){
        }
        
        List<Node> aspectNodeList = new ArrayList<Node>();
        //Node  aspectNode;
        List<Node> aspectCategoryList = new ArrayList<Node>();
        //Node text;
        int dummy;
                
       for (int i=0; i< myCategory.size(); i++) {
                      
           Element sentence = document.createElement("sentence");
           Attr sentenceID = document.createAttribute("id");
           
           dummy = i+1;
           
           sentenceID.setValue(String.valueOf(dummy));
           sentence.setAttributeNode(sentenceID);   
           
           //aspectNode = createAspectTermNode(document, i);
           aspectCategoryList = geAspectCategoriesList(document, i);
           aspectNodeList = getAspectTermsList(document, i);
           
           // add text
           
           Element text = document.createElement("text");    
           System.out.print( myText.get( myCategoryKeys.get(i) ) );
           //System.out.print(myText.size());
           //System.out.print(myCategoryKeys.get(i));
           
           text.setTextContent(myText.get(myCategoryKeys.get(i)));           
           sentence.appendChild(text);
           
           // add categories
           if(aspectCategoryList.size() >0){
               
               for (int j=0; j<aspectCategoryList.size(); j++){
               
               sentence.appendChild(aspectCategoryList.get(j));
           
           }
           }
           
           // add aspectTerms
           if (aspectNodeList.size() >0){
               
                  for (int j=0; j<aspectNodeList.size(); j++){
               
               sentence.appendChild(aspectNodeList.get(j));
           
           }
           
           }
           
           
           //sentence.appendChild(aspectNode);
            
           root.appendChild(sentence);
              
              
       }
       
       makeXML();
        
    }
    
    // ======================================================================= //
    // ======================================================================= //    
    // ======================================================================= //    
    // ======================================================================= //
    
    private static void whenStartsWithA(String  str){
        String [] localStr = getSplitString (str);
        int index = localStr.length-1;
        if(localStr.length > 0){
            StaticData.getInstance().setValue(localStr[2], localStr[index]);  
            //System.out.println("hashmap key: "+localStr[2]);
            //System.out.println("hashmap value: "+localStr[index]);
        }
        
    }    
    
    private static String [] getSplitString (String str){
        return str.split("\\s+");
    }
        
    private static void whenStartsWithT(String str){
         
        String [] localStr  = getSplitString(str);
       
         
        if(localStr.length >0){          
             //System.out.println("whenT: "+str);
             TermModel tm = new TermModel();            
            tm.setKey(localStr[0]);
            tm.setFrom(localStr[2]);
            tm.setTo(localStr[3]);
            tm.setUrduTerm(getUrduTerm(localStr));
            termModels.add(tm);
            myAspectKeys.add(localStr[0]);
            
        }
    
    }
    
    private static String getUrduTerm(String [] str){
        StringBuilder bg = new StringBuilder();
        
        if(str.length >=4){
            for (int i=4; i< str.length; i++){
                bg.append(str[i]);
                bg.append(" ");
            }
        }
        
        return bg.toString();
    }
    
    private static List<Node> geAspectCategoriesList(Document document, int termCounter) {
        
        List<Node> localaspectCategoriesList = new ArrayList<Node>();
        Element aspectCategory = document.createElement("aspectCategories");  
        
        Element aspectCategories = document.createElement("aspectCategory");        
        Attr polarity = document.createAttribute("polarity");
        Attr category = document.createAttribute("category");
        
        polarity.setValue(myPolarity.get(myCategoryKeys.get(counter)));
        category.setValue(myCategory.get(myCategoryKeys.get(counter)));      
        
         aspectCategories.setAttributeNode(polarity);
         aspectCategories.setAttributeNode(category);         
         aspectCategory.appendChild(aspectCategories);         
         localaspectCategoriesList.add(aspectCategory);
         
        return localaspectCategoriesList;
    }
    
    
    private static List<Node> getAspectTermsList(Document document, int termCounter) {
        
        List<Node> localAspectTermsList = new ArrayList<Node>();
    
    
    Element aspectTerm = document.createElement("aspectTerm");    
        
    for (int m=0; m<StaticData.getInstance().getHashMap().size(); m++){
        Element aspectTerms = document.createElement("aspectTerms");
    //Attr genderAttribute = document.createAttribute("gender");
    
    Attr polarity = document.createAttribute("polarity");
    
    Attr from = document.createAttribute("from");
    
    Attr term = document.createAttribute("term");
    
    Attr to = document.createAttribute("to");
    
    
    TermModel tm = termModels.get(m);
    //tm.getFrom();
    
    term.setValue(tm.getUrduTerm());
    from.setValue(tm.getFrom());
    to.setValue(tm.getTo());
    
    polarity.setValue(getPolarity(tm.getKey()));
   
    // append attribute to contact element
   
    aspectTerms.setAttributeNode(to);
    aspectTerms.setAttributeNode(from);
    aspectTerms.setAttributeNode(term);
    aspectTerms.setAttributeNode(polarity);
    
    aspectTerm.appendChild(aspectTerms);
    
    localAspectTermsList.add(aspectTerm);
    }
     

    return localAspectTermsList;
  }
    
    private static String getPolarity(String key){
        
        HashMap<String, String> hm = StaticData.getInstance().getHashMap();
         ///System.out.println("asad"+key);
        return hm.get(key);
    }

    private static void makeXML(){
    
        //System.out.println("mil"+termModels.get(0).toString());
    
    // write the XML document to disk
    try {

      // create DOMSource for source XML document
      Source xmlSource = new DOMSource(document);

      // create StreamResult for transformation result
      Result result = new StreamResult(new FileOutputStream("C:\\Users\\Owner\\Documents\\NetBeansProjects\\XMLMaker\\src\\xmlmaker\\aspectTerm.xml"));

      // create TransformerFactory
      TransformerFactory transformerFactory = TransformerFactory.newInstance();

      // create Transformer for transformation
      Transformer transformer = transformerFactory.newTransformer();

      transformer.setOutputProperty("indent", "yes");

      // transform and deliver content to client
      transformer.transform(xmlSource, result);
    }

    // handle exception creating TransformerFactory
    catch (TransformerFactoryConfigurationError factoryError) {
      System.err.println("Error creating " + "TransformerFactory");
      factoryError.printStackTrace();
    }catch (TransformerException transformerError) {
      System.err.println("Error transforming document");
      transformerError.printStackTrace();
    }    catch (IOException ioException) {
      ioException.printStackTrace();
    }
    
    
    }
    
}
