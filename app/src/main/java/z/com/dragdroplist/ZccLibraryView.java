package z.com.dragdroplist;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class ZccLibraryView extends RelativeLayout {


    private Context context;
    private ZccGridView gridView;
    private ListView listView;
    private View track, slider;
    private float numColumn, numRow, columnWidth, hrSpace, vrSpace, visibleListHeight, trackerHeight, sliderHeight;
    private float ratio, height, scrolledHeight, rowHeight;


    public ZccLibraryView(Context context) {
        super(context);
        init(context);
    }

    public ZccLibraryView(Context context, AttributeSet attrs) {
        super(context, attrs);


        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ZccLibraryView);
        try {
            rowHeight = typedArray.getDimension(R.styleable.ZccLibraryView_row_height, 100);
            numColumn = typedArray.getInt(R.styleable.ZccLibraryView_numColumn, 1);
            columnWidth = typedArray.getDimension(R.styleable.ZccLibraryView_columnWidth, 1);
            hrSpace = typedArray.getDimension(R.styleable.ZccLibraryView_hr_space, 1);
            vrSpace = typedArray.getDimension(R.styleable.ZccLibraryView_vr_space, 1);
        } finally {
            typedArray.recycle();
        }
        init(context);
    }




    private void init(Context context) {
        this.context = context;

        View view = inflate(context, R.layout.zcc_library_view_layout, null);
        addView(view);
//        listView = view.findViewById(R.id.list_view);
        gridView = view.findViewById(R.id.grid_view);
        track = view.findViewById(R.id.track);
        slider = view.findViewById(R.id.slider);

        gridView.setColumnWidth((int) columnWidth);
        gridView.setNumColumns((int) numColumn);
        gridView.setHorizontalSpacing((int) hrSpace);
        gridView.setVerticalSpacing((int) vrSpace);


        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                height = getHeight();
                trackerHeight = track.getHeight();
                sliderHeight = slider.getHeight();

                int count = 0;

                for (int i = 0; i <= gridView.getLastVisiblePosition(); i++) {
                    if (getChildAt(i) != null) {
                        count++;
                    }
                }

                visibleListHeight = count * rowHeight;
                ratio = 4096 / (trackerHeight - sliderHeight);
                slider.setTranslationY(0);
                setListeners();
                setListScrollListener();


            }
        },800);


//        this.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                height = getHeight();
//                trackerHeight = track.getHeight();
//                sliderHeight = slider.getHeight();
//
//                int count = 0;
//
//                for (int i = 0; i <= gridView.getLastVisiblePosition(); i++) {
//                    if (getChildAt(i) != null) {
//                        count++;
//                    }
//                }
//
//                visibleListHeight = count * rowHeight;
//                ratio = 4096 / (trackerHeight - sliderHeight);
//                slider.setTranslationY(0);
//                setListeners();
//                setListScrollListener();
//
//            }
//        });


    }


    private void setListeners() {

        track.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int dy = Math.round(ratio * motionEvent.getY());
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (motionEvent.getY() < 0) {
                            slider.setTranslationY(0);
                        } else if (motionEvent.getY() + sliderHeight >= trackerHeight) {
                            slider.setTranslationY(trackerHeight - sliderHeight);
                        } else {
                            slider.setTranslationY(motionEvent.getY());
                        }
                        gridView.setSelection(dy);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        gridView.setSelection(dy);
                        if (motionEvent.getY() == 0) {
                            slider.setTranslationY(0);
                            gridView.setSelection(0);
                        } else if (motionEvent.getY() + sliderHeight >= trackerHeight) {
                            slider.setTranslationY(trackerHeight - sliderHeight);
                            gridView.setSelection(dy);
                        } else if (motionEvent.getY() < 0) {
                            slider.setTranslationY(0);
                            gridView.setSelection(0);

                        } else {
                            slider.setTranslationY(motionEvent.getY());
                            gridView.setSelection(dy);
                        }
                        break;
                }

                return true;
            }
        });




    }


    public void setListSelection(int index) {


        final int temp = index;

        gridView.post(new Runnable() {
            @Override
            public void run() {

                gridView.setSelectionFromTop(temp, (int) visibleListHeight / 2);

            }
        });


        slider.post(new Runnable() {
            @Override
            public void run() {
                slider.setTranslationY(ratio / temp);
            }
        });


//        gridView.getCustomLibraryAdapter().setTouchPosition(index);


    }


    private void setListScrollListener() {
        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                float motionEventY = i / ratio;
                slider.setTranslationY(motionEventY);
            }
        });


    }

    public void setGridAdapter(ZccGridAdapter adapter) {
        gridView.setAdapter(adapter);
    }


    public void setRowWidth(int w){
        gridView.setItemWidth(w);
    }

    public void setRowHeight(int h){
        gridView.setItemHeight(h);
    }


    public void setSwatchCatchListener(SwatchCatchListener swatchCatchListener){
        gridView.setSwatchCatchListener(swatchCatchListener);
    }
}
