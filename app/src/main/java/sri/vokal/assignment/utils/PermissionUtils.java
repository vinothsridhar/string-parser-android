package sri.vokal.assignment.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

/**
 * Created by sridhar on 9/7/16.
 */
public class PermissionUtils {
    private static final String TAG = PermissionUtils.class.getSimpleName();

    public static boolean hasPermission(Context context, String permission) {
        Log.d(TAG, "inside hasPermission() and checking permission | " + permission);
        if (shouldAskPermission()) {
            return context.checkCallingOrSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
        }

        return true;
    }

    public static boolean hasPermission(Context context, String[] permissions) {
        for (int i = 0; i < permissions.length; i++) {
            if (!hasPermission(context, permissions[i])) {
                return false;
            }
        }
        return true;
    }

    public static boolean shouldAskPermission() {
        Log.d(TAG, "inside shouldAskPermission()");
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1;
    }

}
