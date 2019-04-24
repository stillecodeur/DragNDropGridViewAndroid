package z.com.dragdroplist;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import org.askerov.dynamicgrid.BaseDynamicGridAdapter;

import java.util.List;

public class GridAdpter extends BaseDynamicGridAdapter implements OnItemDropListener {

    private Context context;
    private List<SwatchBean> nums;
    private LayoutInflater layoutInflater;
    private int mItemHeight = 0, mItemWidth = 0;
    private OnItemInflateListener onItemInflateListener;

    @Override
    public void onDropAtIndex(int pickedIndex, int droppedIndex) {

        SwatchBean oldItem = getItem(pickedIndex);
        SwatchBean newItem = getItem(droppedIndex);

        nums.set(droppedIndex, oldItem);
        nums.set(pickedIndex, newItem);

        notifyDataSetChanged();
    }

    @Override
    public void onDropOutside(int pickedIndex) {

        SwatchBean droppedItem = getItem(pickedIndex);
        droppedItem.setColor(0);
        droppedItem.setName(null);
        notifyDataSetChanged();
    }

    public interface OnItemInflateListener {
        void onInflate(int h, int w);
    }

    public void setOnItemInflateListener(OnItemInflateListener onItemInflateListener) {
        this.onItemInflateListener = onItemInflateListener;
    }


    public GridAdpter(Context context, List<SwatchBean> items, int columnCount) {
        super(context, items, columnCount);
        this.context = context;
        this.nums = items;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }


    @Override
    public int getCount() {
        return nums.size();
    }

    @Override
    public SwatchBean getItem(int position) {
        return nums.get(position);
    }


    public void setItem(int pos, SwatchBean swatchBean) {
        nums.set(pos, swatchBean);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.grid_row, null);
            viewHolder = new ViewHolder();
            viewHolder.mainLayout = convertView.findViewById(R.id.main_layout);
            viewHolder.colorLayout = convertView.findViewById(R.id.color_layout);
            viewHolder.tvZcc = convertView.findViewById(R.id.tv_zcc_text);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        SwatchBean swatchBean = getItem(position);
        if (!TextUtils.isEmpty(swatchBean.getName())) {
            viewHolder.tvZcc.setText(swatchBean.getName());
            GradientDrawable drawable = (GradientDrawable) ContextCompat.getDrawable(context, R.drawable.swatch_drawable);
            try {
                drawable.setColor(swatchBean.getColor());
                viewHolder.colorLayout.setBackground(drawable);
            } catch (IllegalArgumentException e) {

            }

        } else {
            viewHolder.colorLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.swatch_icon));
            viewHolder.tvZcc.setText("");
        }


        if (mItemHeight == 0 && mItemWidth == 0) {


            final View copyConvertView = convertView;
            copyConvertView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    copyConvertView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    mItemHeight = copyConvertView.getHeight();
                    mItemWidth = copyConvertView.getWidth();

                    if (onItemInflateListener != null) {
                        onItemInflateListener.onInflate(mItemHeight, mItemWidth);
                    }
                }
            });

        }

        return convertView;
    }


    static class ViewHolder {
        View mainLayout;
        View colorLayout;
        TextView tvZcc;
    }

    public int getItemHeight() {
        return mItemHeight;
    }


    public int getItemWidth() {
        return mItemWidth;
    }


}
