package com.jsonservice.parser;

import com.jsonservice.model.JMessage;
import org.springframework.stereotype.Service;
import java.util.Stack;

@Service
public class Parser {


    static private final String [][] mas={{"<p>","</p>"},{"<li><p>","</p></li>"}};



    private String html;
    private String parseString;
    private Stack<Integer> stackString=new Stack<>();
    private Stack<Integer> massiveString=new Stack<>();
    private Stack<Integer> all=new Stack<>();
    private int i;


    final static private String[] ban={"\\\\n","<p>,</p>","\"","},","\\[","]","\\{","},", "<li><p></p></li>","<li></li>","<ul></ul>", "\\\\"};

  


    public String parse(JMessage jMessage) {
        parseString = jMessage.getText();
        stackString.push(0);
        all.push(0);


        html = find(1,0);
        html=html.replaceAll("\n", "</p><p>");

    

    

        for(int j=0;j<ban.length;j++){
        html=html.replaceAll(ban[j], "");
        }

        return html;
    }


    public String addString(int first, int second){
        String local=parseString.substring(first+1, second-1);
        if (!isStringNullOrWhiteSpace(local)) {
            return local;
        }

        return "";
    }

    public int more(){
        return all.peek();
    }

    public String find(int start, int branch){

        char c=' ';
        String local="";


        int buf;
        boolean base=true;
        try{
        for(i=start;i<parseString.length();i++){
            c=parseString.charAt(i);
            switch (c){
                case '{':
                    buf=more();
                    stackString.push(i);
                    all.push(i);
                    local+=mas[0][0]+addString(buf, i)+mas[0][1];
                    local+=find(i+1,branch);
                    base=false;
                    break;

                case '[':
                    buf=more();
                    local+=mas[0][0]+addString(buf, i)+mas[0][1];
                    massiveString.push(i);
                    all.push(i);
                    local+="<ul>"+find(i+1, 1);
                    base=false;
                    break;

                case '}':
                    all.push(i);
                    if(base)
                    local+=mas[branch][0]+addString(stackString.pop(), i)+mas[branch][1];
                    return local;

                case ']':
                    all.push(i);
                    return local+"</ul>";


            }
        }}catch(Exception e){

        }
        return local;
    }





    public static boolean isStringNullOrWhiteSpace(String value) {
        if (value == null) {
            return true;
        }

        for (int i = 0; i < value.length(); i++) {
            if (!Character.isWhitespace(value.charAt(i))) {
                return false;
            }
        }

        return true;
    }
}
