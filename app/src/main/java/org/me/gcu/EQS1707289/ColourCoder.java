package org.me.gcu.EQS1707289;

public class ColourCoder {

    public static String getColourCode(Double magnitude){

        String textColour;

        if(magnitude >=3){
            textColour = "#f53716";
        }
        else if(magnitude >=2){
            textColour = "#f59116";
        }
        else if (magnitude >=1){
            textColour = "#f5de16";
        }
        else{
            textColour = "#a1eb00";
        }

        return textColour;
    }
}
