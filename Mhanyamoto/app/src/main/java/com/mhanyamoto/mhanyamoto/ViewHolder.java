package com.mhanyamoto.mhanyamoto;

import android.util.SparseArray;
import android.view.View;

/*
 * The View Holder pattern allows to avoid the findViewById() method in the adapter. 
 * A ViewHolder class is a static inner class in your adapter which holds references to the relevant views. 
 * in your layout. This reference is assigned to the row view as a tag via the setTag() method. 
 * If we receive a convertView object, we can get the instance of the ViewHolder via the getTag() 
 * method and assign the new attributes to the views via the ViewHolder reference. 
 * While this sounds complex this is approximately 15 % faster then using the findViewById() method.
 */
public class ViewHolder {
	@SuppressWarnings("unchecked")
	public static <T extends View> T get(View view, int id) {
		SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
		if (viewHolder == null) {
			viewHolder = new SparseArray<View>();
			view.setTag(viewHolder);
		}
		View childView = viewHolder.get(id);
		if (childView == null) {
			childView = view.findViewById(id);
			viewHolder.put(id, childView);
		}
		return (T) childView;
	}
}
