package de.saufapparat.trinkspiel;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import de.saufapparat.trinkspiel.util.HelperUtil;

public class PackageSelectionPage extends AppCompatActivity {

    private static String selectedPackageName;
    private Button startGameButton;
    private LinearLayout cardViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_selection_page);

        initializeViews();
    }

    private void initializeViews() {
        startGameButton = findViewById(R.id.button_startGameLoop);
        cardViews = findViewById(R.id.package_cards);
        selectedPackageName = "";
    }

    @Override
    protected void onResume() {
        super.onResume();
        HelperUtil.removeNavigationBarBottom(this);

        checkButtonActivation();
    }

    private void checkButtonActivation() {
        startGameButton.setEnabled(false);
        if (selectedPackageName != null && !selectedPackageName.equals("")){
            startGameButton.setEnabled(true);
        }
    }

    public void startGameWithSelectedPackage(View view){
        Intent intent = new Intent(this, GameLoop.class);
        startActivity(intent);
    }

    public void selectStandardPackage(View view){
        selectedPackageName = "StandardPackage";
        startGameButton.setEnabled(true);
        resetAllColorsFromPackages();
        highLightSelectedPackage(0);
    }

    public void selectOnlinePackage(View view){
        selectedPackageName = "OnlinePackage";
        startGameButton.setEnabled(true);
        resetAllColorsFromPackages();
        highLightSelectedPackage(1);
    }

    public void selectActivityPackage(View view){
        selectedPackageName = "ActivityPackage";
        startGameButton.setEnabled(true);
        resetAllColorsFromPackages();
        highLightSelectedPackage(2);
    }

    private void resetAllColorsFromPackages() {
        for(int i = 0; i < cardViews.getChildCount(); i++){
            ((CardView) cardViews.getChildAt(i)).setCardBackgroundColor(getResources().getColor(R.color.flo1));
        }
    }

    private void highLightSelectedPackage(int index) {
        CardView selectedCardView = (CardView) cardViews.getChildAt(index);
        selectedCardView.setCardBackgroundColor(getResources().getColor(R.color.flo3));
    }

    public void openMoreInformationDialog(View view){
        String infoLabel = "Das Paket";
        String informationText = "Ich beschreibe das Paket";
        switch (String.valueOf(view.getTag())){
            case "standard":
                infoLabel = getString(R.string.standardPaket_label);
                informationText=getString(R.string.standardPaket_infoText);
                break;
            case "online":
                infoLabel = getString(R.string.onlinePaket_label);
                informationText=getString(R.string.onlinePaket_infoText);
                break;
            case "aktiv":
                infoLabel = getString(R.string.activityPaket_label);
                informationText=getString(R.string.activityPaket_infoText);
                break;
            default:
                break;
        }
        ImageView vi = (ImageView) view;
        vi.setColorFilter(R.color.flo2, PorterDuff.Mode.OVERLAY);
        vi.animate().setDuration(100).withEndAction(new Runnable() {
            @Override
            public void run() {
                vi.setColorFilter(null);
            }
        }).start();
        showInfoDialog(infoLabel ,informationText);
    }

    public void showInfoDialog(String infoLabel, String infoText){
        Bundle bundle = new Bundle();
        bundle.putString("label", infoLabel);
        bundle.putString("text", infoText);
        PackageInformationDialog dialog = new PackageInformationDialog(bundle);
        dialog.show(getSupportFragmentManager(),"Mehr Informationen Dialog");

    }

    public void redirectBack(View v){
        this.finish();
        return;
    }

    public static String getSelectedPackage(){
        return selectedPackageName;
    }
}