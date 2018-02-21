package com.kaerenabo.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Expandable Height ListView.
 * Allow to use list view inside scrollbar
 */
public class ExpandableHeightListView extends ListView {

    /**
     * The Expanded.
     */
    boolean expanded = false;

    /**
     * Instantiates a new Expandable height list view.
     *
     * @param context the context
     */
    public ExpandableHeightListView(Context context) {
        super(context);
    }

    /**
     * Instantiates a new Expandable height list view.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public ExpandableHeightListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Instantiates a new Expandable height list view.
     *
     * @param context  the context
     * @param attrs    the attrs
     * @param defStyle the def style
     */
    public ExpandableHeightListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * Is expanded boolean.
     *
     * @return the boolean
     */
    public boolean isExpanded() {
        return expanded;
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (isExpanded()) {
            int expandSpec = MeasureSpec.makeMeasureSpec(MEASURED_SIZE_MASK, MeasureSpec.AT_MOST);
            super.onMeasure(widthMeasureSpec, expandSpec);

            ViewGroup.LayoutParams params = getLayoutParams();
            params.height = getMeasuredHeight();
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    /**
     * Sets expanded.
     *
     * @param expanded the expanded
     */
    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }
}
