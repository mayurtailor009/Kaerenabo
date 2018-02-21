package com.kaerenabo.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.GridView;

/**
 * Expendible GridView
 * Allow to use grid view inside scrollbar
 */
public class ExpandableHeightGridView extends GridView {

    /**
     * The Expanded.
     */
    boolean expanded = false;

    /**
     * Instantiates a new Expandable height grid view.
     *
     * @param context the context
     */
    public ExpandableHeightGridView(Context context)
    {
        super(context);
    }

    /**
     * Instantiates a new Expandable height grid view.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public ExpandableHeightGridView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    /**
     * Instantiates a new Expandable height grid view.
     *
     * @param context  the context
     * @param attrs    the attrs
     * @param defStyle the def style
     */
    public ExpandableHeightGridView(Context context, AttributeSet attrs,
                                    int defStyle)
    {
        super(context, attrs, defStyle);
    }

    /**
     * Is expanded boolean.
     *
     * @return the boolean
     */
    public boolean isExpanded()
    {
        return expanded;
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        // HACK! TAKE THAT ANDROID!
        if (isExpanded())
        {
            // Calculate entire height by providing a very large height hint.
            // View.MEASURED_SIZE_MASK represents the largest height possible.
            int expandSpec = MeasureSpec.makeMeasureSpec(MEASURED_SIZE_MASK,
                    MeasureSpec.AT_MOST);
            super.onMeasure(widthMeasureSpec, expandSpec);

            ViewGroup.LayoutParams params = getLayoutParams();
            params.height = getMeasuredHeight();
        }
        else
        {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    /**
     * Sets expanded.
     *
     * @param expanded the expanded
     */
    public void setExpanded(boolean expanded)
    {
        this.expanded = expanded;
    } }
