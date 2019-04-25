package z.com.dragdroplist;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class SwatchAdapter extends BaseAdapter {


    private Context context;
    private List<SwatchBean> swatchBeans;
    private LayoutInflater layoutInflater;

    public SwatchAdapter(Context context, List<SwatchBean> swatchBeans) {
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
            convertView = layoutInflater.inflate(R.layout.swatch_list_row, null);
            viewHolder = new ViewHolder();
            viewHolder.mainLayout = convertView.findViewById(R.id.main_layout);
            viewHolder.tvZcc = convertView.findViewById(R.id.tv_zcc_text);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        SwatchBean swatchBean = getItem(position);

        if (TextUtils.isEmpty(swatchBean.getName())) {
            viewHolder.tvZcc.setText(swatchBean.getName());
        }

        if (swatchBean.getColor() != 0) {
            viewHolder.mainLayout.setBackgroundColor(swatchBean.getColor());
        }


        return convertView;
    }

    static class ViewHolder {
        View mainLayout;
        TextView tvZcc;
    }

}
