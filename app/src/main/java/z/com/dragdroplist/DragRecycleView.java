package z.com.dragdroplist;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.GridView;

public class DragRecycleView extends GridView {


    enum ListMode {
        NOTHING, DRAG, DROP, PICK
    }

    private static final String TAG = DragRecycleView.class.getSimpleName();
    private Context context;
    private GestureDetector detector;
    private int selectedPosition = -1, mItemHeight = 0, mItemWidth = 0, mPickedIndex = -1, mDroppedIndex = -1;
    private Paint paint;
    private ListMode mListMode = ListMode.NOTHING;
    private Bitmap mItemBitmap;
    private float x1, y1, x2, y2;
    private int rowNum = 3, columnNum = 8;
    private boolean longPressed = false;
    private OnItemSwapListener onItemSwapListener;

    public void setOnItemSwapListener(OnItemSwapListener onItemSwapListener) {
        this.onItemSwapListener = onItemSwapListener;
    }

    public interface OnItemSwapListener {
        void onSwapped(int pickedIndex, int droppedIndex);
    }

    public DragRecycleView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public DragRecycleView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DragRecycleView);
        int noOfColumns = typedArray.getInt(R.styleable.DragRecycleView_columns, 1);
        int columnw = (int) typedArray.getDimension(R.styleable.DragRecycleView_columnw, 1);
        int hrSpace = (int) typedArray.getDimension(R.styleable.DragRecycleView_hrspace, 1);
        int vrSpace = (int) typedArray.getDimension(R.styleable.DragRecycleView_vrspace, 1);

        this.setNumColumns(noOfColumns);
        this.setColumnWidth(columnw);
        this.setHorizontalSpacing(hrSpace);
        this.setVerticalSpacing(vrSpace);
        init(context);

    }

    private void init(Context context) {
        this.context = context;
        detector = new GestureDetector(context, new MyGestureListener());
        paint = new Paint();
//        paint.setAntiAlias(true);
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.ADD));
//        paint.setAlpha(0xFF);
    }


    final Handler handler = new Handler();
    Runnable mLongPressed = new Runnable() {
        public void run() {
            longPressed = true;
            mPickedIndex = getIndexFromLocation(x1, y1);
            Log.d(TAG, "onLongPress: selecPos" + selectedPosition);
            mListMode = ListMode.PICK;
            invalidate();
        }
    };


    @Override
    public void onDrawForeground(Canvas canvas) {
        super.onDrawForeground(canvas);
        switch (mListMode) {
            case PICK:
                SwatchBean swatchBean = (SwatchBean) getAdapter().getItem(mPickedIndex);
                View view = getChildAt(mPickedIndex);
                mItemBitmap = loadBitmapFromView(view);
                canvas.drawBitmap(mItemBitmap, view.getLeft() + 20, view.getTop() + 20, paint);
                longPressed = false;
                break;
            case DRAG:
                canvas.drawBitmap(mItemBitmap, x2 - 150, y2 - 150, paint);
                break;
            case DROP:
                mDroppedIndex = getIndexFromLocation(x2, y2);
                View droppedView = getChildAt(mPickedIndex);
                mListMode = ListMode.NOTHING;
                canvas.drawBitmap(mItemBitmap, droppedView.getLeft(), droppedView.getTop(), paint);
                mItemBitmap.recycle();
                mItemBitmap = null;
                if (onItemSwapListener != null) {
                    onItemSwapListener.onSwapped(mPickedIndex, mDroppedIndex);
                }
                break;


        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


//        switch (mListMode) {
//            case PICK:
//                int i = (int) getAdapter().getItem(mPickedIndex);
//                View view = getChildAt(mPickedIndex);
//                mItemBitmap = loadBitmapFromView(view);
//                canvas.drawBitmap(mItemBitmap, view.getLeft() + 20, view.getTop() + 20, paint);
//                longPressed = false;
//                break;
//            case DRAG:
//                canvas.drawBitmap(mItemBitmap, x2 - 150, y2 - 150, paint);
//                break;
//            case DROP:
//                mDroppedIndex = getIndexFromLocation(x2, y2);
//                mListMode = ListMode.NOTHING;
//                canvas.drawBitmap(mItemBitmap, -50, -50, paint);
//                mItemBitmap.recycle();
//                mItemBitmap = null;
//                if(onItemSwapListener!=null){
//                    onItemSwapListener.onSwapped(mPickedIndex,mDroppedIndex);
//                }
//                break;
//
//
//        }


    }


    private int getIndexFromLocation(float x, float y) {
        int itemX = (int) Math.ceil(x / mItemWidth);
        int itemY = (int) Math.ceil(y / mItemHeight);

        if (itemY > rowNum) itemY = rowNum;
        if (itemX > columnNum) itemX = columnNum;
        int pos = ((itemY - 1) * columnNum) + (itemX - 1);
//        if (pos < 0) pos = 0;
//        else if (pos == 0) pos = 1;

        return pos;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {


        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = ev.getX();
                y1 = ev.getY();
                mPickedIndex = getIndexFromLocation(x1, y1);
                mListMode = ListMode.PICK;
                SwatchBean swatchBean = (SwatchBean) getAdapter().getItem(mPickedIndex);
                if (TextUtils.isEmpty(swatchBean.getName())) {
                    return false;
                }
                View view = getChildAt(mPickedIndex);
                mItemBitmap = loadBitmapFromView(view);
                break;
            case MotionEvent.ACTION_MOVE:
                if (longPressed) return false;
                mListMode = ListMode.DRAG;
                x2 = ev.getX();
                y2 = ev.getY();
                handler.removeCallbacks(mLongPressed);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                x2 = ev.getX();
                y2 = ev.getY();
                mListMode = ListMode.DROP;
                handler.removeCallbacks(mLongPressed);
                invalidate();
                break;

        }

        return true;

    }


    public class MyGestureListener implements GestureDetector.OnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            Log.d(TAG, "onDown: ");
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {
            Log.d(TAG, "onShowPress: ");

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            Log.d(TAG, "onSingleTapUp: ");
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            Log.d(TAG, "onScroll: ");
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            Log.d(TAG, "onLongPress: X--" + e.getX() + "Y--" + e.getY());


            longPressed = true;
            mPickedIndex = getIndexFromLocation(e.getX(), e.getY());
            Log.d(TAG, "onLongPress: selecPos" + selectedPosition);
            mListMode = ListMode.PICK;
            invalidate();
//            View view=getSelectedView();
//            selectedPosition=getPositionForView(view);
//            invalidate();

        }


        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.d(TAG, "onFling: ");
            return true;
        }
    }


    public int getItemHeight() {
        return mItemHeight;
    }

    public void setItemHeight(int mItemHeight) {
        this.mItemHeight = mItemHeight;
    }

    public int getItemWidth() {
        return mItemWidth;
    }

    public void setItemWidth(int mItemWidth) {
        this.mItemWidth = mItemWidth;
    }


    public Bitmap loadBitmapFromView(View v) {
        Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
        v.draw(c);
        return b;
    }
}
