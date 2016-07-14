package com.averi.worldscribe.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.averi.worldscribe.Category;
import com.averi.worldscribe.R;
import com.averi.worldscribe.activities.PlaceActivity;
import com.averi.worldscribe.utilities.AppPreferences;
import com.averi.worldscribe.utilities.ExternalReader;

import java.util.ArrayList;

/**
 * Created by mark on 02/07/16.
 * An Adapter for RecyclerViews displaying a Person's Residences.
 */
public class ResidencesAdapter extends RecyclerView.Adapter<ResidencesAdapter.ResidenceHolder> {

    /**
     * A ViewHolder containing a Residence Card. Clicking on it will navigate the user to a
     * PlaceActivity containing the specified Place's data. The card also contains an Erase
     * Button for deleting the selected Residence link (but not the Place itself).
     */
    public class ResidenceHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final Context context;
        private final CardView residenceCard;
        private final TextView residenceNameText;
        private final ImageButton deleteButton;
        private final String worldName;
        private final String articleName;
        private String residenceName;

        /**
         * Instantiates a new ResidenceHolder.
         * @param context The Context calling this method.
         * @param worldName The name of the current World.
         * @param personName The name of the current Article.
         * @param itemView The Residences Card that this ViewHolder will handle.
         */
        public ResidenceHolder(Context context, String worldName, String personName, View itemView) {
            super(itemView);

            this.context = context;
            residenceCard = (CardView) itemView;
            residenceNameText = (TextView) residenceCard.findViewById(R.id.itemName);
            deleteButton = (ImageButton) residenceCard.findViewById(R.id.delete);
            this.worldName = worldName;
            this.articleName = personName;

            residenceCard.setOnClickListener(this);
        }

        /**
         * Stores the name of the Residence and displays it.
         * @param residenceName The name of the Residence represented by the card.
         */
        public void bindResidence(String residenceName) {
            this.residenceName = residenceName;
            setSnippetName();
        }

        /**
         * Displays the name of the referenced Residence.
         */
        private void setSnippetName() {
            residenceNameText.setText(residenceName);
        }

        @Override
        public void onClick(View view) {
            goToResidence();
        }

        /**
         * Goes to PlaceActivity to allow the user to view and edit the specified Place of
         * Residence.
         */
        private void goToResidence() {
            Intent goToResidenceIntent = new Intent(context, PlaceActivity.class);

            goToResidenceIntent.putExtra(AppPreferences.WORLD_NAME, worldName);
            goToResidenceIntent.putExtra(AppPreferences.CATEGORY, Category.Place);
            goToResidenceIntent.putExtra(AppPreferences.ARTICLE_NAME, residenceName);

            context.startActivity(goToResidenceIntent);
        }
    }

    private final ArrayList<String> residences;
    private final Context context;
    private final String worldName;
    private final String personName;

    /**
     * Instantiates a new ResidencesAdapter.
     * @param context The Context calling this method.
     * @param worldName The name of the current World.
     * @param personName The name of the Person this Adapter is attached to.
     */
    public ResidencesAdapter(Context context, String worldName, String personName) {
        this.context = context;
        this.worldName = worldName;
        this.personName = personName;

        residences = ExternalReader.getResidences(context, worldName, personName);
    }

    @Override
    public ResidenceHolder onCreateViewHolder(ViewGroup parent, int pos) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.erasable_item_card, parent, false);
        return new ResidenceHolder(context, worldName, personName, view);
    }

    @Override
    public void onBindViewHolder(ResidenceHolder holder, int pos) {
        holder.bindResidence(residences.get(pos));
    }

    @Override
    public int getItemCount() {
        return residences.size();
    }
}