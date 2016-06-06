package zcf.demo.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

public class EmoViewPagerAdapter extends PagerAdapter {
	private List<View> viewdata;
	public EmoViewPagerAdapter(List<View> viewdata)
	{
		this.viewdata=viewdata;
	}
	
	@Override
	public int getCount() {
		
		return viewdata.size();
	}
	
   

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
	
		return arg0==arg1;
	}
	
	@Override
	public void destroyItem(View container, int position, Object object) {
		// TODO Auto-generated method stub
		super.destroyItem(container, position, object);
		((ViewPager) container).removeView(viewdata.get(position));
	}


	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		container.addView(viewdata.get(position));
		return viewdata.get(position);
	}
	
	

}
