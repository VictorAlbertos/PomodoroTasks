package custom_views;


import android.content.Context;
import android.util.AttributeSet;

import com.hacerapp.pomodorotasks.R;

import org.androidannotations.annotations.EViewGroup;

@EViewGroup(R.layout.action_count_down_view_large)
public class ActionCountDownViewLarge extends ActionCountDownView {
    public ActionCountDownViewLarge(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
