package managers;

public class ResponseOutput {
    private static final StringBuilder stringBuilder = new StringBuilder();

    public static void append (String out) {stringBuilder.append(out);}

    public static void nextLine () {stringBuilder.append("\n");}

    public static void appendln (String out) {
        stringBuilder.append(out).append("\n");}

    public static String getString () {return stringBuilder.toString(); }

    public static String getAndClear () {
        String strReturn = stringBuilder.toString();
        stringBuilder.delete(0, stringBuilder.length());
        return strReturn;
    }

    public static void clear () {stringBuilder.delete(0, stringBuilder.length());}

}
