package com.example.haidt.mylivewallpaper.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.example.haidt.mylivewallpaper.Fragment.Catagory;
import com.example.haidt.mylivewallpaper.Fragment.NewPicture;
import com.example.haidt.mylivewallpaper.Fragment.StandOutPicture;

public class TabFragmentAdapter extends FragmentPagerAdapter {
    private Context context;
    private final int numOfTab=3;
    public TabFragmentAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context=context;
    }
    @Override
    public Fragment getItem(int position) {
        if (position==0)
            return Catagory.getINSTANCE();
        else if(position==1)
            return StandOutPicture.getINSTANCE();
        else if(position==2)
            return NewPicture.getINSTANCE();
        else return null;
    }
    @Override
    public int getCount() {
        return numOfTab;
    }
    @Override
    public CharSequence getPageTitle(int position){
        if (position==0)
            return "Thể Loại";
        else if(position==1)
            return "Nổi bật";
        else if(position==2)
            return "Mới";
        else return null;
    }
}
