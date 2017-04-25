package sri.vokal.assignment.utils;

import android.net.Uri;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by sridhar on 24/4/17.
 */

public class FileUtils {

    public static String getFileContent(Uri uri) {
        InputStream is = null;
        try {
            is = new FileInputStream(new File(uri.getPath()));
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = "";
            String stringFile = "";
            while ((line = br.readLine()) != null) {
                stringFile += line;
            }
            return stringFile;
        } catch (Exception e) {
            //Do nothing
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (Exception e) {
                //Do nothing
            }
        }
        return "";
    }

}
