package z.com.dragdroplist;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

public class ZccGridView extends GridView {


    private static final String TAG = ListDragView.class.getSimpleName();
    private Context context;
    private Bitmap mBitmap;
    private SwatchBean mSwatchBean;
    private View mView;
    private Paint paint;
    private int mItemWidth, mItemHeight, rowNum = 2048, columnNum = 2;
    private float x1,y1;
    private SwatchCatchListener swatchCatchListener;


    public ZccGridView(Context context) {
        super(context);
        init(context);
    }

    public ZccGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }


    private void init(Context context) {
        this.context = context;

        paint = new Paint();
        paint.setAlpha(150);

//        mSwatchView = inflate(context, R.layout.zcc_grid_row_layout, null);
//        mSwatchTextView = mSwatchView.findViewById(R.id.tv_zcc_text);
//        mSwatchLayout = mSwatchView.findViewById(R.id.color_layout);


    }


    @Override
    public void onDrawForeground(Canvas canvas) {
        super.onDrawForeground(canvas);



        if (mView != null && mBitmap!=null) {
            if(mBitmap!=null) {
                canvas.drawBitmap(mBitmap, x1, y1, paint);
            }else{
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            }
        }
    }


    private int getIndexFromLocation(float x, float y) {


        if (x < 0 || y < 0) return -1;

        int itemX = (int) Math.ceil(x / mItemWidth);
        int itemY = (int) Math.ceil(y / mItemHeight);

        if (itemY > rowNum) return -1;
        if (itemX > columnNum) return -1;
        int pos = ((itemY - 1) * columnNum) + (itemX - 1);
        return pos;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float actionX = event.getX();
        float actionY = event.getY();

        x1=actionX;y1=actionY;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                int pos = getIndexFromLocation(actionX, actionY);
                mView = getChildAt(pos);
                TextView textView = mView.findViewById(R.id.tv_position);
                mSwatchBean = (SwatchBean) getAdapter().getItem(Integer.parseInt(textView.getText().toString()));
                mBitmap = loadBitmapFromView(mView);
                swatchCatchListener.onCatchSwatch(mBitmap, mSwatchBean, getWidth(), getHeight());
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                if (actionX > getWidth()) {
                    Log.d(TAG, "onTouchEvent: X--" + event.getX() + "--Y--" + event.getY());
                    swatchCatchListener.onDragSwatch(event);
                }
                invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
                event.setAction(MotionEvent.ACTION_UP);
                swatchCatchListener.onDropSwatch(event);
                mBitmap=null;
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                event.setAction(MotionEvent.ACTION_UP);
                swatchCatchListener.onDropSwatch(event);
                mBitmap=null;
                invalidate();
                break;
        }


        return true;

    }


    public Bitmap loadBitmapFromView(View v) {
        Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
        v.draw(c);
        return b;
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

    public void setSwatchCatchListener(SwatchCatchListener swatchCatchListener) {
        this.swatchCatchListener = swatchCatchListener;
    }
}

