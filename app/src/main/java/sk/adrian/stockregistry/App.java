package sk.adrian.stockregistry;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

public class App extends Activity {

    private static Context mContext;

    public static Context getContext() {
        return mContext;
    }

    public static void setContext(Context kontext) {
        mContext = kontext;
    }

}
