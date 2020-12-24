package com.example.cpsc475_project4;

import androidx.lifecycle.MutableLiveData;

public class DownloadTask_TP extends DownloadTask {
    MainActivity myActivity;

    DownloadTask_TP(MainActivity myActivity){
        super();
        attach(myActivity);
    }
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (myActivity != null) {
            myActivity.processJSon(result);
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    void detach() {
        myActivity = null;
    }
    void attach(MainActivity activity) {
        this.myActivity = activity;
    }
}
