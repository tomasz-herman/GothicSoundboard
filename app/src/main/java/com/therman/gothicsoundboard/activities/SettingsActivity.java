package com.therman.gothicsoundboard.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.util.Log;

import androidx.annotation.Nullable;

import com.therman.gothicsoundboard.R;
import com.therman.gothicsoundboard.utils.FileUtils;

public class SettingsActivity extends PreferenceActivity {

    SettingsFragment fragment;
    Preference directory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragment = new SettingsFragment();
        getFragmentManager().beginTransaction().replace(android.R.id.content, fragment).commit();

    }

    @Override
    protected void onStart() {
        super.onStart();
        directory = fragment.findPreference("directory");
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        directory.setSummary(preferences.getString("directory", getString(R.string.choose_where_is_directory)));
        directory.setOnPreferenceClickListener(preference -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            startActivityForResult(Intent.createChooser(intent, "Choose directory"), 9999);
            return true;
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case 9999:
                Uri uri = data.getData();
                Uri docUri = DocumentsContract.buildDocumentUriUsingTree(uri, DocumentsContract.getTreeDocumentId(uri));
                String path = FileUtils.getFullPathFromTreeUri(docUri, this);
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                preferences.edit().putString("directory", path).apply();
                directory.setSummary(path);
                Log.d("Test", "Result URI " + path);
                break;
        }
    }

    public static class SettingsFragment extends PreferenceFragment {
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
        }
    }
}
