package z.com.dragdroplist;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

public class ZccScroller extends RelativeLayout {

    private Context context;
    private View tracker;
    private View slider;
    private int trackerHeight;
    private int sliderHeight;
    private float currentActionDown, currentActionMove, previousActionDown, previousActionMove;
    private ScrollListener scrollListener;

    public ZccScroller(Context context) {
        super(context);
        init(context);
    }

    public ZccScroller(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    private void init(Context context) {
        View view = inflate(context, R.layout.zcc_scroller, null);
        tracker = view.findViewById(R.id.track);
        slider = view.findViewById(R.id.slider);

        addView(view);


        this.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                trackerHeight = tracker.getHeight();
                sliderHeight = slider.getHeight();
            }
        });

        setListeners();

    }


    private void setListeners() {
        tracker.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float actionY = event.getY();


                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        currentActionDown = event.getY();
                        if (actionY < 0) {
                            slider.setTranslationY(0);
                            scrollListener.onScroll(0);
                        } else if (actionY == 0) {
                            slider.setTranslationY(0);
                            scrollListener.onScroll(0);
                        } else if (actionY + sliderHeight >= trackerHeight) {
                            slider.setTranslationY(trackerHeight - sliderHeight - 10);
                            scrollListener.onScroll(100);
                        } else {
                            slider.setTranslationY(actionY);
                            float percentage = (actionY / trackerHeight) * 100;
//                            percentage = currentActionDown > previousActionDown ? percentage : -percentage;
//                            previousActionDown = currentActionDown;
//                            scrollListener.onScroll((int) percentage);
                            scrollListener.onScroll((int)currentActionDown);
                        }

                        break;
                    case MotionEvent.ACTION_MOVE:
                        currentActionMove = event.getY();
                        if (actionY == 0) {
                            slider.setTranslationY(0);
                            scrollListener.onScroll(0);
                        } else if (actionY + sliderHeight >= trackerHeight) {
                            slider.setTranslationY(trackerHeight - sliderHeight - 10);
                            scrollListener.onScroll(100);
                        } else if (actionY < 0) {
                            slider.setTranslationY(0);
                            scrollListener.onScroll(0);
                        } else {
                            slider.setTranslationY(actionY);
                            float percentage = (actionY / trackerHeight) * 100;
//                            percentage = currentActionMove > previousActionMove ? percentage : -percentage;
//                            previousActionMove = currentActionMove;
//                            scrollListener.onScroll((int) percentage);
                            scrollListener.onScroll((int)currentActionMove);
                        }
                        break;
                }

                return true;
            }


        });
    }


    public interface ScrollListener {
        void onScroll(int percentage);
    }

    public void setScrollListener(ScrollListener scrollListener) {
        this.scrollListener = scrollListener;
    }

    public int getTrackerHeight(){
        return trackerHeight-sliderHeight;
    }
}
