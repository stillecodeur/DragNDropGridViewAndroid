package z.com.dragdroplist;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.jaredrummler.android.colorpicker.ColorPickerView;

public class ColorPicker extends AppCompatActivity {



    private Context context;
    private ColorPickerView cpView;
    private TextView tvColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_picker);
        context=ColorPicker.this;

        cpView=findViewById(R.id.cp_view);
        tvColor=findViewById(R.id.tv_hex);


        cpView.setOnColorChangedListener(new ColorPickerView.OnColorChangedListener() {
            @Override
            public void onColorChanged(int i) {
                tvColor.setTextColor(i);
                tvColor.setText(""+Color.red(i)+","+Color.green(i)+","+Color.blue(i));
            }
        });
    }
}
