package z.com.dragdroplist;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ZccGridAdapter extends BaseAdapter {


    private Context context;
    private List<SwatchBean> swatchBeans;
    private LayoutInflater layoutInflater;
    private int mItemHeight = 0, mItemWidth = 0;
    private GridAdpter.OnItemInflateListener onItemInflateListener;

    public ZccGridAdapter(Context context, List<SwatchBean> swatchBeans) {
        this.context = context;
        this.swatchBeans = swatchBeans;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }


    @Override
    public int getCount() {
        return swatchBeans.size();
    }

    @Override
    public SwatchBean getItem(int position) {
        return swatchBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.zcc_grid_row_layout, null);
            viewHolder = new ViewHolder();
            viewHolder.mainLayout = convertView.findViewById(R.id.main_layout);
            viewHolder.colorLayout = convertView.findViewById(R.id.color_layout);
            viewHolder.tvZcc = convertView.findViewById(R.id.tv_zcc_text);
            viewHolder.tvPosition = convertView.findViewById(R.id.tv_position);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        SwatchBean swatchBean = getItem(position);viewHolder.tvPosition.setText(""+position);
        if (!TextUtils.isEmpty(swatchBean.getName())) {
            viewHolder.tvZcc.setText(swatchBean.getName());
            GradientDrawable drawable = (GradientDrawable) ContextCompat.getDrawable(context, R.drawable.swatch_drawable);
            try {
                drawable.setColor(swatchBean.getColor());
                viewHolder.colorLayout.setBackground(drawable);
            } catch (IllegalArgumentException e) {

            }

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



    public void setOnItemInflateListener(GridAdpter.OnItemInflateListener onItemInflateListener) {
        this.onItemInflateListener = onItemInflateListener;
    }



    static class ViewHolder {
        View mainLayout;
        View colorLayout;
        TextView tvZcc;
        TextView tvPosition;
    }

}
