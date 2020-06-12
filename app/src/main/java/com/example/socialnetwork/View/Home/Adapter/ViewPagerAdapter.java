package com.example.socialnetwork.View.Home.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.socialnetwork.View.Home.Tab_chat.ChatFragment;
import com.example.socialnetwork.View.Home.Tab_home.HomeFragment;
import com.example.socialnetwork.View.Home.Tab_menu.MenuFragment;
import com.example.socialnetwork.View.Home.Tab_youtube.YoutubeFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {


    public ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
       switch (position){
           case 0:
               return new HomeFragment();
           case 1:
               return new YoutubeFragment();
           case 2:
               return new ChatFragment();
           default:
               return new MenuFragment();
       }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
