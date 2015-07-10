package custom_views;


import android.content.Context;
import android.util.AttributeSet;

import com.hacerapp.pomodorotasks.R;

import org.androidannotations.annotations.EViewGroup;

@EViewGroup(R.layout.action_count_down_view_small)
public class ActionCountDownViewSmall extends ActionCountDownView {
    public ActionCountDownViewSmall(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
