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

    public static int getColourMarker(Double magnitude){

        int markerColour;

        if(magnitude >=3){
            markerColour = R.drawable.red_marker;
        }
        else if(magnitude >=2){
            markerColour = R.drawable.orange_marker;
        }
        else if (magnitude >=1){
            markerColour = R.drawable.yellow_marker;
        }
        else{
            markerColour = R.drawable.green_marker;
        }

        return markerColour;
    }

}
