package com.example.diksha.thirdeye;

import android.support.v4.app.Fragment;

/**
 * Created by diksha on 17/5/17.
 */

public class ThirdEyeMainActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return PhotosFragment.newInstance();
    }
}