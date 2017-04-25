package sri.vokal.assignment;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by sridhar on 25/4/17.
 */

public class StringGroup implements Comparable<StringGroup> {

    public int min;
    public int max;
    public List<StringItem> items = new ArrayList<StringItem>();

    @Override
    public int compareTo(@NonNull StringGroup stringGroup) {
        if (stringGroup.max > max) {
            return -1;
        } else if (stringGroup.max < max) {
            return 1;
        } else {
            return 0;
        }
    }

    public static class StringItem implements Comparable<StringItem> {
        public static final int TYPE_HEADER = 0;
        public static final int TYPE_CHILD = 1;

        public String item;
        public int count;
        public int type = TYPE_CHILD;

        public String toString() {
            if (type == TYPE_HEADER) {
                return item;
            } else {
                return item + " (" + count + ")";
            }
        }

        @Override
        public int compareTo(@NonNull StringItem stringItem) {
            if (stringItem.count < count) {
                return 1;
            } else if (stringItem.count > count) {
                return -1;
            } else {
                return 0;
            }
        }
    }

}
