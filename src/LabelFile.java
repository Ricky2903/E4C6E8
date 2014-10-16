public class LabelFile {
    private static int condLabel = 0;
    private static int loopLabel = 0;
    public static String getCondLabel() {
        return "COND_" + condLabel++;
    }
    public static String getLoopLabel() {
        return "WHL_" + loopLabel++;
    }
}