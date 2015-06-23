package activities;

import android.view.View;

import com.hacerapp.pomodorotasks.R;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.FragmentById;
import org.androidannotations.annotations.ViewById;

import fragments.ConfigFragment;
import utilities.ui.Animations;

@EActivity(R.layout.main_activity)
public class MainActivity extends BaseAppCompatActivity {
    @Bean protected Animations mAnimations;
    @ViewById protected SlidingUpPanelLayout sliding_up_panel;
    @FragmentById protected ConfigFragment config_fragment;

    @AfterViews protected void initViews() {
        config_fragment.toolbar.setAlpha(.8f);

        sliding_up_panel.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override public void onPanelExpanded(View view) {
                config_fragment.populateInputs();
                mAnimations.alpha(config_fragment.toolbar, 1);
            }

            @Override public void onPanelCollapsed(View view) {
                mAnimations.alpha(config_fragment.toolbar, .8f);
            }

            @Override public void onPanelSlide(View view, float v) {}
            @Override public void onPanelAnchored(View view) {}
            @Override public void onPanelHidden(View view) {}
        });
    }
}
