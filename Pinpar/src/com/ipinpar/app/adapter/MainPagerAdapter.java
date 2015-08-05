package com.ipinpar.app.adapter;

import java.util.List;

import com.ipinpar.app.PPBaseFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;

public class MainPagerAdapter extends FragmentStatePagerAdapter {
	
	private List<PPBaseFragment> mFragments;
	
	public MainPagerAdapter(FragmentManager fm,List<PPBaseFragment> list){
		super(fm);
		mFragments = list;
	}

	@Override
	public int getCount() {
		// TODO 自动生成的方法存�?
		return mFragments.size();
	}
	
	@Override
	public Fragment getItem(int arg0) {
		// TODO 自动生成的方法存�?
		PPBaseFragment page = null;
        if (mFragments.size() > arg0) {
            page = mFragments.get(arg0);
            return page;
        }
			return null;
	}
	
	@Override
	public Object instantiateItem(View container, int position) {
		// TODO Auto-generated method stub
		return super.instantiateItem(container, position);
	}

}
