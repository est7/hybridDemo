package com.est7.vrecyclerview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/9.
 */

public class XRecyclerView extends RecyclerView {

    private int mRefreshProgressStyle = ProgressStyle.SysProgress;
    private int mLoadingMoreProgressStyle = ProgressStyle.SysProgress;

    private boolean pullRefreshEnabled = true;
    private boolean loadingMoreEnabled = true;
    //header
    private ArrowRefreshHeader mRefreshHeader;
    //footer
    private LoadingMoreFooter mFootView;


    //以下是3+n个特殊类型，属于自己处理的。
    private static final int TYPE_REFRESH_HEADER = 10000;//设置一个很大的数字,尽可能避免和用户的adapter冲突
    private static final int TYPE_FOOTER = 10001;
    private static final int HEADER_INIT_INDEX = 10002;
    private static List<Integer> sHeaderTypes = new ArrayList<>();//每个header必须有不同的type,不然滚动的时候顺序会变化


    //header可能会有很多，所以用list
    private ArrayList<View> mHeaderViews = new ArrayList<>();
    private WrapAdapter mWrapAdapter;
    private LoadingListener mLoadingListener;

    private final RecyclerView.AdapterDataObserver mDataObserver = new DataObserver();
    //adapter没有数据的时候显示,类似于listView的emptyView
    private View mEmptyView;

    public XRecyclerView(Context context) {
        this(context, null);
    }

    public XRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {

        if (pullRefreshEnabled) {
            mRefreshHeader = new ArrowRefreshHeader(getContext());
            mRefreshHeader.setProgressStyle(mRefreshProgressStyle);
        }
        LoadingMoreFooter footView = new LoadingMoreFooter(getContext());
        footView.setProgressStyle(mLoadingMoreProgressStyle);
        mFootView = footView;
        mFootView.setVisibility(GONE);
    }


    public void addHeaderView(View view) {
        sHeaderTypes.add(HEADER_INIT_INDEX + mHeaderViews.size());
        mHeaderViews.add(view);
        if (mWrapAdapter != null) {
            mWrapAdapter.notifyDataSetChanged();
        }
    }

    public void setEmptyView(View emptyView) {
        this.mEmptyView = emptyView;
        mDataObserver.onChanged();
    }

    public View getEmptyView() {
        return mEmptyView;
    }

    public int getHeadersCount() {
        return mHeaderViews.size();
    }


    //根据header的ViewType判断是哪个header
    private View getHeaderViewByType(int itemType) {
        if (!isHeaderType(itemType)) {
            return null;
        }
        return mHeaderViews.get(itemType - HEADER_INIT_INDEX);
    }

    //判断一个type是否为HeaderType
    private boolean isHeaderType(int itemViewType) {
        return mHeaderViews.size() > 0 && sHeaderTypes.contains(itemViewType);
    }

    //判断是否是XRecyclerView保留的itemViewType
    private boolean isReservedItemViewType(int itemViewType) {
        if (itemViewType == TYPE_REFRESH_HEADER || itemViewType == TYPE_FOOTER || sHeaderTypes.contains(itemViewType)) {
            return true;
        } else {
            return false;
        }
    }

    public void setPullRefreshEnabled(boolean enabled) {
        pullRefreshEnabled = enabled;
    }

    public void setLoadingMoreEnabled(boolean enabled) {
        loadingMoreEnabled = enabled;
        if (!enabled) {
            //如果是false，那么就把footer隐藏掉
            if (mFootView instanceof LoadingMoreFooter) {
                ((LoadingMoreFooter) mFootView).setState(LoadingMoreFooter.STATE_COMPLETE);
            }
        }
    }

    @Override
    public void setAdapter(Adapter adapter) {
        mWrapAdapter = new WrapAdapter(adapter);
        super.setAdapter(mWrapAdapter);
        adapter.registerAdapterDataObserver(mDataObserver);
        mDataObserver.onChanged();

    }

    //避免用户自己调用getAdapter() 引起的ClassCastException
    @Override
    public Adapter getAdapter() {
        if (mWrapAdapter != null)
            return mWrapAdapter.getOriginalAdapter();
        else
            return null;
    }

    private class WrapAdapter extends RecyclerView.Adapter<ViewHolder> {

        private RecyclerView.Adapter adapter;

        public WrapAdapter(RecyclerView.Adapter adapter) {
            this.adapter = adapter;
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_REFRESH_HEADER) {
                return new SimpleViewHolder(mRefreshHeader);
            } else if (isHeaderType(viewType)) {
                return new SimpleViewHolder(getHeaderViewByType(viewType));
            } else if (viewType == TYPE_FOOTER) {
                return new SimpleViewHolder(mFootView);
            }
            return adapter.onCreateViewHolder(parent, viewType);
        }


        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            if (isHeader(position) || isRefreshHeader(position)) {
                return;
            }
        }

        public boolean isHeader(int position) {
            return position >= 1 && position < mHeaderViews.size() + 1;
        }


        public boolean isRefreshHeader(int position) {
            return position == 0;
        }

        public boolean isFooter(int position) {
            if(loadingMoreEnabled) {
                return position == getItemCount() - 1;
            }else {
                return false;
            }
        }

        @Override
        public int getItemCount() {
            if (loadingMoreEnabled) {
                if (adapter != null) {
                    return getHeadersCount() + adapter.getItemCount() + 2;
                } else {
                    return getHeadersCount() + 2;
                }
            } else {
                if (adapter != null) {
                    return getHeadersCount() + adapter.getItemCount() + 1;
                } else {
                    return getHeadersCount() + 1;
                }
            }
        }

        public Adapter getOriginalAdapter() {

            return adapter;
        }


        @Override
        public int getItemViewType(int position) {
            int adjPosition = position - (getHeadersCount() + 1);
            if (isRefreshHeader(position)) {
                return TYPE_REFRESH_HEADER;
            }
            if (isHeader(position)) {
                position = position - 1;
                return sHeaderTypes.get(position);
            }
            if (isFooter(position)) {
                return TYPE_FOOTER;
            }

            int adapterCount;
            if (adapter != null) {
                adapterCount = adapter.getItemCount();
                if (adjPosition < adapterCount) {
                    int type = adapter.getItemViewType(adjPosition);
                    if (isReservedItemViewType(type)) {
                        throw new IllegalStateException("XRecyclerView require itemViewType in adapter should be less than 10000 ");
                    }
                    return type;
                }
            }
            return 0;
        }


        @Override
        public long getItemId(int position) {
            if (adapter != null && position >= getHeadersCount() + 1) {
                int adjPosition = position - (getHeadersCount() + 1);
                if (adjPosition < adapter.getItemCount()) {
                    return adapter.getItemId(adjPosition);
                }
            }
            return -1;
        }


        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
            RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
            if (manager instanceof GridLayoutManager) {
                final GridLayoutManager gridManager = ((GridLayoutManager) manager);
                gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        return (isHeader(position) || isFooter(position) || isRefreshHeader(position))
                                ? gridManager.getSpanCount() : 1;
                    }
                });
            }
            adapter.onAttachedToRecyclerView(recyclerView);
        }


        @Override
        public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
            super.onViewAttachedToWindow(holder);
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams && (isHeader(holder.getLayoutPosition()) ||isRefreshHeader(holder.getLayoutPosition()) || isFooter(holder.getLayoutPosition()))) {
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }
            adapter.onViewAttachedToWindow(holder);
        }

    }





    private class SimpleViewHolder extends RecyclerView.ViewHolder {
        public SimpleViewHolder(View itemView) {
            super(itemView);
        }
    }



    public void setLoadingListener(LoadingListener listener) {
        mLoadingListener = listener;
    }

    public interface LoadingListener {

        void onRefresh();

        void onLoadMore();
    }



    public class DividerItemDecoration extends RecyclerView.ItemDecoration {

        private Drawable mDivider;
        private int mOrientation;

        /**
         * Sole constructor. Takes in a {@link Drawable} to be used as the interior
         * divider.
         *
         * @param divider A divider {@code Drawable} to be drawn on the RecyclerView
         */
        public DividerItemDecoration(Drawable divider) {
            mDivider = divider;
        }

        /**
         * Draws horizontal or vertical dividers onto the parent RecyclerView.
         *
         * @param canvas The {@link Canvas} onto which dividers will be drawn
         * @param parent The RecyclerView onto which dividers are being added
         * @param state The current RecyclerView.State of the RecyclerView
         */
        @Override
        public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
            if (mOrientation == LinearLayoutManager.HORIZONTAL) {
                drawHorizontalDividers(canvas, parent);
            } else if (mOrientation == LinearLayoutManager.VERTICAL) {
                drawVerticalDividers(canvas, parent);
            }
        }

        /**
         * Determines the size and location of offsets between items in the parent
         * RecyclerView.
         *
         * @param outRect The {@link Rect} of offsets to be added around the child
         *                view
         * @param view The child view to be decorated with an offset
         * @param parent The RecyclerView onto which dividers are being added
         * @param state The current RecyclerView.State of the RecyclerView
         */
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);

            if (parent.getChildAdapterPosition(view) <= getHeadersCount() + 1) {
                return;
            }
            mOrientation = ((LinearLayoutManager) parent.getLayoutManager()).getOrientation();
            if (mOrientation == LinearLayoutManager.HORIZONTAL) {
                outRect.left = mDivider.getIntrinsicWidth();
            } else if (mOrientation == LinearLayoutManager.VERTICAL) {
                outRect.top = mDivider.getIntrinsicHeight();
            }
        }

        /**
         * Adds dividers to a RecyclerView with a LinearLayoutManager or its
         * subclass oriented horizontally.
         *
         * @param canvas The {@link Canvas} onto which horizontal dividers will be
         *               drawn
         * @param parent The RecyclerView onto which horizontal dividers are being
         *               added
         */
        private void drawHorizontalDividers(Canvas canvas, RecyclerView parent) {
            int parentTop = parent.getPaddingTop();
            int parentBottom = parent.getHeight() - parent.getPaddingBottom();

            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount - 1; i++) {
                View child = parent.getChildAt(i);

                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                int parentLeft = child.getRight() + params.rightMargin;
                int parentRight = parentLeft + mDivider.getIntrinsicWidth();

                mDivider.setBounds(parentLeft, parentTop, parentRight, parentBottom);
                mDivider.draw(canvas);
            }
        }

        /**
         * Adds dividers to a RecyclerView with a LinearLayoutManager or its
         * subclass oriented vertically.
         *
         * @param canvas The {@link Canvas} onto which vertical dividers will be
         *               drawn
         * @param parent The RecyclerView onto which vertical dividers are being
         *               added
         */
        private void drawVerticalDividers(Canvas canvas, RecyclerView parent) {
            int parentLeft = parent.getPaddingLeft();
            int parentRight = parent.getWidth() - parent.getPaddingRight();

            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount - 1; i++) {
                View child = parent.getChildAt(i);

                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                int parentTop = child.getBottom() + params.bottomMargin;
                int parentBottom = parentTop + mDivider.getIntrinsicHeight();

                mDivider.setBounds(parentLeft, parentTop, parentRight, parentBottom);
                mDivider.draw(canvas);
            }
        }
    }

    private class DataObserver extends RecyclerView.AdapterDataObserver {
        @Override
        public void onChanged() {
            if (mWrapAdapter != null) {
                mWrapAdapter.notifyDataSetChanged();
            }
            if (mWrapAdapter != null && mEmptyView != null) {
                int emptyCount = 1 + getHeadersCount();
                if (loadingMoreEnabled) {
                    emptyCount++;
                }
                if (mWrapAdapter.getItemCount() == emptyCount) {
                    mEmptyView.setVisibility(View.VISIBLE);
                    XRecyclerView.this.setVisibility(View.GONE);
                } else {

                    mEmptyView.setVisibility(View.GONE);
                    XRecyclerView.this.setVisibility(View.VISIBLE);
                }
            }
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            mWrapAdapter.notifyItemRangeChanged(positionStart, itemCount, payload);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeRemoved(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            mWrapAdapter.notifyItemMoved(fromPosition, toPosition);
        }
    };
}
