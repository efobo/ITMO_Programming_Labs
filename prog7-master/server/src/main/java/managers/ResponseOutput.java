package managers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ResponseOutput {
    private static final StringBuilder stringBuilder = new StringBuilder();
    private static List<String> argList = new ArrayList<>();

    public static void append (String out) {stringBuilder.append(out);}

    public static void nextLine () {stringBuilder.append("\n");}

    public static void appenderror (String out) {stringBuilder.append("error: " + out + "\n");}

    public static void appendln (String out) {
        stringBuilder.append(out).append("\n");}

    public static void appendargs(String ... args) {
        argList.addAll(Arrays.asList(args));
    }

    public static String getString () {return stringBuilder.toString(); }

    public static String getAndClear () {
        String strReturn = stringBuilder.toString();
        stringBuilder.delete(0, stringBuilder.length());
        return strReturn;
    }

    public static String[] getArgsAndClear() {
        String[] argsAsArray = new String[argList.size()];
        argsAsArray = argList.toArray(argsAsArray);
        argList.clear();
        return argsAsArray;
    }

    public static void clear () {stringBuilder.delete(0, stringBuilder.length());}

}
