package gabenrulez.helper;

import java.util.HashMap;

public class texter {
    public static final int consoleWidth = 80;
    private static final int tabWidth = 4;
    public static HashMap<Character, String> previousLines = new HashMap<>();

    public static String createCharLine(char character, int amount){
        String temp = String.valueOf(character);
        int yetToWrite = amount - 1;
        int alreadyWritten = 1;
        while(yetToWrite > 0){
            if(yetToWrite > alreadyWritten){
                temp += temp;
                yetToWrite -= alreadyWritten;
                alreadyWritten *= 2;
            }
            else{
                String temp2 = texter.createCharLine(character, yetToWrite);
                temp += temp2;
                alreadyWritten += yetToWrite;
                yetToWrite = 0;
            }
        }
        return temp;
    }

    public static String createCharLine(char character){
        String result = previousLines.get(character);
        if(result == null){
            result = createCharLine(character, texter.consoleWidth);
            previousLines.put(character, result);
        }
        return result;
    }

    public static String createCentered(String text){
        if(text.length() > texter.consoleWidth) return createTabbed(text);
        return createCharLine(' ', (texter.consoleWidth - text.length())/2) + text;
    }

    public static String createTabbed(String text, int amountOfTabs){
        return createCharLine(' ', texter.tabWidth * amountOfTabs) + text;
    }

    public static String createTabbed(String text){
        return createTabbed(text, 1);
    }

    public static String createNewLines(int amount){
        String temp = "\n";
        int yetToWrite = amount - 1;
        int alreadyWritten = 1;
        while(yetToWrite > 0){
            if(yetToWrite > alreadyWritten){
                temp += temp;
                yetToWrite -= alreadyWritten;
                alreadyWritten *= 2;
            }
            else{
                String temp2 = texter.createNewLines(yetToWrite);
                temp += temp2;
                alreadyWritten += yetToWrite;
                yetToWrite = 0;
            }
        }
        return temp;
    }

    public static String createNewLine(){
        return createNewLines(1);
    }

    public static void printCharLine(char character){
        System.out.println(createCharLine(character));
    }

    public static void printCentered(String text){
        System.out.println(createCentered(text));
    }

    public static void printTabbed(String text, int amount){
        System.out.println(createTabbed(text, amount));
    }

    public static void printTabbed(String text){
        printTabbed(text, 1);
    }

    public static void printNewLines(int amount){
        System.out.print(createNewLines(amount));
    }

    public static void printNewLine(){
        System.out.print(createNewLine());
    }

    public static void printMessage(String messageText){
        System.out.println(createNewLine() + createCharLine('#') + createNewLines(2) + createCentered(messageText) + createNewLines(2) + createCharLine('#') + createNewLine());
    }

    public static void printErrorMessage(String errorSource, String errorMessage){
        System.out.println(createNewLine() + createCharLine('*') + createNewLines(2) + createTabbed("Error from '" + errorSource + "': ") + createNewLine() + createTabbed(errorMessage) + createNewLines(2) + createCharLine('*') + createNewLine());
    }
}
