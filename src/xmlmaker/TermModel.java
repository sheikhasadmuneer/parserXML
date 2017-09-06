/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xmlmaker;

/**
 *
 * @author Owner
 */
public class TermModel {
     private  String key;
     private  String from;
     private  String to;
     private  String urduTerm;
     
     
      public  void setKey(String key) {
        this.key = key;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public   void setUrduTerm(String urduTerm) {
        this.urduTerm = urduTerm;
    }

    public   String getKey() {
        return key;
    }

    public   String getFrom() {
        return from;
    }

    public   String getTo() {
        return to;
    }

    public   String getUrduTerm() {
        return urduTerm;
    }
     
    
}
