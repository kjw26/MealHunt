package com.mealhunt.mealhunt;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class HorizontalSpaceItemDecoration extends RecyclerView.ItemDecoration {

        private final int horizontalSpaceHeight;

        public HorizontalSpaceItemDecoration(int horizontalSpaceHeight) {
            this.horizontalSpaceHeight = horizontalSpaceHeight;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1) {
                outRect.bottom = horizontalSpaceHeight;
            }
        }
    }
