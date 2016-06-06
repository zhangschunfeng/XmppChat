package zcf.demo.adapter;

import java.util.List;

import zcf.demo.fragment.SettingFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class MyPagerAdapter extends FragmentPagerAdapter {
	private List<Fragment> list;

	public MyPagerAdapter(FragmentManager fm,List<Fragment> list) {
		super(fm);
		this.list=list;
	}

	@Override
	public Fragment getItem(int position) {
		
		return list.get(position);
	}

	@Override
	public int getCount() {
	
		return list.size();
	}

	
	
}
