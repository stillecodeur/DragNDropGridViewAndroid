package z.com.dragdroplist;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class ListDragView extends ListView {


    private static final String TAG=ListDragView.class.getSimpleName();
    private DragRecycleView dragRecycleView;
    private Bitmap mBitmap;
    private SwatchBean mSwatchBean;
    private View mView;
    private Paint paint;

    public ListDragView(Context context) {
        super(context);
        init();
    }

    public ListDragView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ListDragView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    public void setDragRecycleView(DragRecycleView dragRecycleView) {
        this.dragRecycleView = dragRecycleView;
    }


    private void init() {
        paint = new Paint();
    }


    @Override
    public void onDrawForeground(Canvas canvas) {

        if(mView!=null) {
            canvas.drawBitmap(mBitmap, mView.getRight() - 150, mView.getTop() - 150, paint);
        }


    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float actionX = event.getX();
        float actionY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                int pos=4;
                mView = getChildAt(pos);
                mSwatchBean = (SwatchBean) getAdapter().getItem(pos);
                mBitmap=loadBitmapFromView(mView);
                break;
            case MotionEvent.ACTION_MOVE:
                if (actionX > getWidth()) {
                    Log.d(TAG, "onTouchEvent: X--"+event.getX()+"--Y--"+event.getY());
                    dragRecycleView.setMovableItem(mBitmap,mSwatchBean, event,getWidth(),getHeight());
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                event.setAction(MotionEvent.ACTION_UP);
                dragRecycleView.setActionDrop(event);
                break;
            case MotionEvent.ACTION_UP:
                event.setAction(MotionEvent.ACTION_UP);
                dragRecycleView.setActionDrop(event);
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
}
