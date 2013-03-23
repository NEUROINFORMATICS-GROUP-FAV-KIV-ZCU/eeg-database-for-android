package cz.zcu.kiv.eeg.mobile.base.ui.filechooser;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.data.Values;
import cz.zcu.kiv.eeg.mobile.base.data.container.FileAdapter;
import cz.zcu.kiv.eeg.mobile.base.data.container.FileInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Activity for browsing devices file system. On file select activity ends and returns path to selected file.
 *
 * @author Petr Miko
 */
public class FileChooserActivity extends ListActivity {

    private final static String TAG = FileChooserActivity.class.getSimpleName();
    private final String ROOT = "/";
    private List<FileInfo> files = new ArrayList<FileInfo>();
    private TextView currentUrlView;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fchooser_list);
        currentUrlView = (TextView) findViewById(R.id.url);
        loadDir(ROOT);
    }

    /**
     * Loads directory content on specified path.
     *
     * @param dirPath path to directory
     */
    private void loadDir(String dirPath) {

        Log.d(TAG, "Loading path " + dirPath);
        File f = new File(dirPath);
        File[] files = f.listFiles();
        currentUrlView.setText(f.getAbsolutePath());
        this.files.clear();

        for (File file : files) {
            //application does not have permission to list
            if (file.isDirectory() && file.listFiles() == null)
                continue;
            //application has permission to access file
            if (file.length() != 0)
                this.files.add(new FileInfo(file, file.getName()));
        }

        FileAdapter fileList = new FileAdapter(this, R.layout.fchooser_list_row, this.files);
        fileList.sort(FileInfo.getFileComparator());

        //adding references to previous directory and to root
        if (!dirPath.equals(ROOT)) {
            this.files.add(0, new FileInfo(f.getParentFile(), ".."));
            this.files.add(0, new FileInfo(new File(ROOT), ROOT));

        }

        setListAdapter(fileList);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        File file = this.files.get(position);
        Log.d(TAG, "Clicked on file: " + file.getName());
        if (file.isDirectory())
            loadDir(file.getAbsolutePath());
        else {
            Intent resultIntent = new Intent();
            resultIntent.putExtra(Values.FILE_PATH, file.getAbsolutePath());
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        }
    }
}