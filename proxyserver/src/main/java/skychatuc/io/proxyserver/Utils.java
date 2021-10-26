package skychatuc.io.proxyserver;

public class Utils {
    public static String convertByteToHexadecimal(byte[] byteArray) {
        StringBuilder strBuilder = new StringBuilder();
        for(byte val : byteArray) {
            strBuilder.append(String.format("%02x", val&0xff));
        }
        return strBuilder.toString();
    }
}
