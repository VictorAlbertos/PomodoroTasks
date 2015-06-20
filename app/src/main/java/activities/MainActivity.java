package activities;

import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.hacerapp.pomodorotasks.R;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.FragmentById;
import org.androidannotations.annotations.ViewById;

import fragments.ConfigFragment;

@EActivity(R.layout.main_activity)
public class MainActivity extends FragmentActivity {
    @ViewById protected SlidingUpPanelLayout sliding_up_panel;
    @FragmentById protected ConfigFragment config_fragment;

    @AfterViews protected void initViews() {
        sliding_up_panel.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override public void onPanelExpanded(View view) {
                config_fragment.populateInputs();
            }

            @Override public void onPanelCollapsed(View view) {}
            @Override public void onPanelSlide(View view, float v) {}
            @Override public void onPanelAnchored(View view) {}
            @Override public void onPanelHidden(View view) {}
        });
    }

}
