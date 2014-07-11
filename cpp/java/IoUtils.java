import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IoUtils {

    private static final int BUFFER_SIZE = 100;

    public static long copy(InputStream is, OutputStream os) {
        byte[] buf = new byte[BUFFER_SIZE];
        long total = 0;
        int len = 0;
        try {
            while ((total < 100) && (-1 != (len = is.read(buf)))) {
                os.write(buf, 0, len);
                total += len;
            }
        } catch (IOException ioe) {
            throw new RuntimeException("error reading stream", ioe);
        }
        //System.out.println("Total: " + total);
        return total;
    }

    public static String getString(InputStream is) {
        byte[] buf = new byte[BUFFER_SIZE];
        String strOut="";
        try {
            is.read(buf);
            strOut = new String(buf, "UTF-8");
        } catch (IOException ioe) {
            throw new RuntimeException("error reading stream", ioe);
        }
        //System.out.println(strOut);
        //return strOut;
        return strOut.substring(strOut.indexOf("GET /?") + 6, strOut.indexOf(" HTTP"));
    }

}