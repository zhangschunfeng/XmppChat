package zcf.demo.utils;

import java.util.Comparator;

import zcf.demo.bean.User;

public class PinyinComparator implements Comparator<User>{

	@Override
	public int compare(User o1, User o2) {
		if (o1.getSortLetter().equals("@")|| o2.getSortLetter().equals("#")) {
			return -1;
		} 
		else if (o1.getSortLetter().equals("#")|| o2.getSortLetter().equals("@")) {
			return 1;
		} 
		else {
			
			return o1.getSortLetter().compareTo(o2.getSortLetter());
		}

	}

}
