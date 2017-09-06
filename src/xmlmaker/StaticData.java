/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xmlmaker;

import java.util.HashMap;

/**
 *
 * @author Owner
 */
public class StaticData {
     private static HashMap<String, String> myValues  = new HashMap<String, String>();
     private static StaticData instance = null;
     
    

   


    
    protected  StaticData(){
        
    }
    
    public static StaticData getInstance() {
      if(instance == null) {
         instance = new StaticData();
      }
      return instance;
   }
    
    public static void setValue(String key, String value){
        myValues.put(key, value);        
    }
    
    public HashMap<String, String> getHashMap(){
       
        return myValues;
    }
    
    
    
    
    
}
