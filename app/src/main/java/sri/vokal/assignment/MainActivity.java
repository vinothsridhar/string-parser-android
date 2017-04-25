package sri.vokal.assignment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import sri.vokal.assignment.utils.FileUtils;
import sri.vokal.assignment.utils.PermissionUtils;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int FILE_BROWSE_REQUEST_CODE = 100;

    private ProgressDialog progressDialog;
    private ListView stringItemListView;
    private TextView helpText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stringItemListView = (ListView) findViewById(R.id.stringItemListView);
        helpText = (TextView) findViewById(R.id.helpText);
    }

    public void browseClick(View v) {
        if (PermissionUtils.hasPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            fileBrowse();
            return;
        }

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 120);
        }
    }

    private void fileBrowse() {
        Intent intent=new Intent();
        intent.setType("text/plain");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Open Text File"), FILE_BROWSE_REQUEST_CODE);
    }

    private void parse(final Uri uri) {
        Log.d(TAG, "selected file uri: " + uri.toString());
        showProgressDialog();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String string = FileUtils.getFileContent(uri);
                Log.d(TAG, "string file content: " + string);
                //split by space
                String[] words = string.split("\\s+");
                ArrayMap<String, Integer> wordsMap = new ArrayMap<String, Integer>();
                for (int i = 0; i < words.length; i++) {
                    String word = words[i];
                    Integer count = wordsMap.get(words[i]);
                    if (count == null) {
                        count = 0;
                    }

                    count++;

                    Log.d(TAG, "count for string: " + word + " | " + count);

                    wordsMap.put(word, count);
                }

                List<StringGroup> stringGroupList = new ArrayList<StringGroup>();

                for (String word : wordsMap.keySet()) {
                    int count = wordsMap.get(word);

                    int minCount = ((count / 10) * 10) + 1;
                    int maxCount = minCount + 9;

                    Log.d(TAG, "min and max count: " + minCount + " | " + maxCount);

                    StringGroup stringGroup = null;
                    for (int j = 0; j < stringGroupList.size(); j++) {
                        if (stringGroupList.get(j).min <= count && stringGroupList.get(j).max >= count) {
                            stringGroup = stringGroupList.get(j);
                        }
                    }
                    if (stringGroup == null) {
                        stringGroup = new StringGroup();
                        stringGroup.min = minCount;
                        stringGroup.max = maxCount;
                        stringGroupList.add(stringGroup);
                    }
                    StringGroup.StringItem stringItem = new StringGroup.StringItem();
                    stringItem.count = count;
                    stringItem.item = word;
                    stringGroup.items.add(stringItem);
                }

                Collections.sort(stringGroupList);

                final List<StringGroup.StringItem> items = new ArrayList<StringGroup.StringItem>();
                for (int i = 0; i < stringGroupList.size(); i++) {
                    StringGroup stringGroup = stringGroupList.get(i);
                    StringGroup.StringItem headerItem = new StringGroup.StringItem();
                    headerItem.type = StringGroup.StringItem.TYPE_HEADER;
                    headerItem.item = stringGroup.min + " - " + stringGroup.max;
                    items.add(headerItem);
                    Collections.sort(stringGroup.items);
                    items.addAll(stringGroup.items);
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showList(items);
                        dismissProgressDialog();
                    }
                });
            }
        }).start();
    }

    private void showList(List<StringGroup.StringItem> items) {
        if (items == null || items.isEmpty()) {
            Toast.makeText(this, "No strings found", Toast.LENGTH_LONG).show();
            return;
        }
        helpText.setVisibility(View.GONE);
        StringItemAdapter adapter = new StringItemAdapter(items);
        stringItemListView.setAdapter(adapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        for (int i = 0; i < permissions.length; i++) {
            String permission = permissions[i];
            int result = grantResults[i];
            if (permission.equals(Manifest.permission.READ_EXTERNAL_STORAGE) && result == PackageManager.PERMISSION_GRANTED) {
                fileBrowse();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FILE_BROWSE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            if (uri != null) {
                parse(uri);
            }
        }
    }

    private void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            try {
                progressDialog.dismiss();
            } catch (Exception e) {
                //Do nothing
            }
        }
    }

    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Processing...");
            progressDialog.setCancelable(false);
        }

        progressDialog.show();
    }
}
