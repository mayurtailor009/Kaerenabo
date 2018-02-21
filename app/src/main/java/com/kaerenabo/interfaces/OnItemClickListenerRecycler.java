package com.kaerenabo.interfaces;

import android.view.View;

/**
 * Item click listerner for recycle view
 */
public interface OnItemClickListenerRecycler {
    /**
     * On item click.
     *
     * @param view     the view
     * @param position the position
     */
    public void onItemClick(View view, int position);
}
