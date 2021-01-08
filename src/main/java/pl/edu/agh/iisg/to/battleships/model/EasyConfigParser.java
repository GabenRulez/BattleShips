package pl.edu.agh.iisg.to.battleships.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class EasyConfigParser {
    /*
    * A simple and easy config parser.
    * Created by Wojciech Kosztyła.
    * */


    // Section - Name - Value
    HashMap<String, HashMap<String,String>> configMap = new HashMap<>();


    public EasyConfigParser(String filepath){
        parseFile(filepath);
    }

    private void parseFile(String filepath){
        try{
            File configFile = new File(filepath);
            Scanner scanner = new Scanner(configFile);

            String active_section = "default";

            while(scanner.hasNextLine()){
                String dataLine = scanner.nextLine();

                if(dataLine.length() == 0 || dataLine.charAt(0) == ' ') {
                    active_section = "default";
                    continue;
                }

                if(dataLine.charAt(0) == '#') continue; // Skip comments



                if(dataLine.charAt(0) == '['){
                    active_section = parseSectionName(dataLine);
                    continue;
                }

                String[] keyValuePair = parseNormalLine(dataLine);
                addNewEntry(active_section, keyValuePair[0], keyValuePair[1]);
            }
        }
        catch (FileNotFoundException e){
            System.out.println("Nie odnaleziono pliku o sciezce " + filepath);
            e.printStackTrace();
        }
    }

    private String parseSectionName(String sectionDataLine){
        sectionDataLine = sectionDataLine.split(" ")[0].strip();    // Get rid of extra comments after section name
        String sectionName = sectionDataLine.replace('[',' ').replace(']', ' ').strip();    // Get rid of square brackets
        sectionName = sectionName.replace(" ", "_");    // Get rid of all spaces inside the name and replace them with '_'
        sectionName = sectionName.toLowerCase();    // Make it all lowercase
        return sectionName;
    }

    private String[] parseNormalLine(String dataLine){
        String[] strings = dataLine.strip().split("#")[0].split("=");
        if(strings.length > 2) System.out.println("W linijce '" + dataLine + "' pojawilo sie wiecej znakow '=' niz powinno. Pomijam wszystko po drugim takim znaku.");
        if(strings.length < 2){
            throw new IllegalArgumentException("Can't read config line '" + dataLine + "'. Reason: can't find '=' sign.");
        }
        String[] resultStrings = new String[2];
        resultStrings[0] = strings[0].strip();
        resultStrings[1] = strings[1].strip();
        return resultStrings;
    }

    private void addNewEntry(String section, String key, String value){

        if(!configMap.containsKey(section)){
            configMap.put(section, new HashMap<>());      // To make sure that section will be accessible
        }

        HashMap<String, String> sectionHashMap = configMap.get(section);
        sectionHashMap.put(key, value);
    }

    public String getFromKey(String key){
        for(HashMap<String, String> sectionHashMap : configMap.values()){
            if(sectionHashMap.containsKey(key)) return sectionHashMap.get(key);
        }
        return null;
    }

    public String getFromSectionAndKey(String section, String key){
        HashMap<String, String> sectionHashMap = configMap.get(section);
        if(sectionHashMap != null){
            return sectionHashMap.get(key);
        }
        return null;
    }

    public void listConfig(){
        System.out.println("Listing config parser current values...");
        for(String sectionName : configMap.keySet()){
            System.out.println("Section: " + sectionName);

            HashMap<String, String> sectionHashMap = configMap.get(sectionName);
            for(Map.Entry<String, String> entry : sectionHashMap.entrySet()){
                System.out.println("\t" + entry.getKey() + " = " + entry.getValue());
            }
        }
    }

}
