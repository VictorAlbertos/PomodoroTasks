package adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hacerapp.pomodorotasks.R;

import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EBean;

import java.util.List;

import models.Card;
import utilities.PomodoroApp;

@EBean
public class CardsRecyclerViewAdapter extends RecyclerView.Adapter<CardsRecyclerViewAdapter.ViewHolder> {
    private static final int DUMMY_TOOLBAR = 123;
    @App protected PomodoroApp app;
    private List<Card> mCards;
    private ItemCardView mItemCardView;

    public CardsRecyclerViewAdapter init(List<Card> cards, ItemCardView itemCardView) {
        mCards = cards;
        mItemCardView = itemCardView;
        return this;
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int idResource = viewType == DUMMY_TOOLBAR ? R.layout.dummy_toolbar : mItemCardView.getIdResource();
        View view = LayoutInflater.from(app).inflate(idResource, parent, false);
        return new ViewHolder(view);
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {
        if (position == mCards.size()) return;

        Card card = mCards.get(position);
        holder.tv_title.setText(card.getName());
        mItemCardView.onInflate(holder, card);
    }

    @Override public int getItemViewType(int position) {
        if (position == mCards.size()) return DUMMY_TOOLBAR;
        else return 0;
    }

    //+1 to inflate as last position the dummy toolbar view
    @Override public int getItemCount() {
        return mCards.size() + 1;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View root;
        private TextView tv_title;
        public ViewHolder(View view) {
            super(view);
            root = view;
            tv_title = (TextView) view.findViewById(R.id.tv_title);
        }
    }

    public interface ItemCardView {
        int getIdResource();
        void onInflate(ViewHolder viewHolder, Card card);
    }
}
