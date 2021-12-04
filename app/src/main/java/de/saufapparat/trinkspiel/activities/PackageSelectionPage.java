package de.saufapparat.trinkspiel.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesResponseListener;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.saufapparat.trinkspiel.R;
import de.saufapparat.trinkspiel.enmus.GamePackage;
import de.saufapparat.trinkspiel.util.HelperUtil;
import de.saufapparat.trinkspiel.util.PackageInformationDialog;

public class PackageSelectionPage extends AppCompatActivity {

    private static GamePackage selectedPackageName;
    private Button startGameButton;
    private LinearLayout cardViews;

    SkuDetails itemInfo;
    BillingClient billingClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_selection_page);

        billingClient = BillingClient.newBuilder(this)
                .enablePendingPurchases()
                .setListener(new PurchasesUpdatedListener() {
                    @Override
                    public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> list) {
                        if(billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && list != null){
                            for(Purchase purchase : list){
                                if(purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED &&
                                        !purchase.isAcknowledged()){
                                    verifyPurchase(purchase);
                                }
                            }
                        }
                    }
                }).build();
        connectToGooglePlayBilling();

        initializeViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        billingClient.queryPurchasesAsync(
                BillingClient.SkuType.INAPP,
                new PurchasesResponseListener() {
                    @Override
                    public void onQueryPurchasesResponse(@NonNull BillingResult billingResult, @NonNull List<Purchase> list) {
                        if(billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK){
                            for(Purchase purchase : list){
                                if(purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED && !purchase.isAcknowledged()){
                                    verifyPurchase(purchase);
                                }
                            }
                        }
                    }
                }
        );
        GameConfigurationActivity.setConfigsSet(false);
        HelperUtil.removeNavigationBarBottom(this);

        checkButtonActivation();
    }

    private void connectToGooglePlayBilling(){
        billingClient.startConnection(
                new BillingClientStateListener() {
                    @Override
                    public void onBillingServiceDisconnected() {
                        connectToGooglePlayBilling();
                    }

                    @Override
                    public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                        if(billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK){
                            getProductDetails();
                        }
                    }
                }
        );
    }

    private void verifyPurchase(Purchase purchase){
        String requestUrl  = "https://europe-west1-saufapparat.cloudfunctions.net/verifyPurchases?" +
                "purchaseToken=" + purchase.getPurchaseToken() + "&" +
                "purchaseTime=" + purchase.getPurchaseTime() + "&" +
                "orderId=" + purchase.getOrderId();

        Activity activity = this;

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                requestUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject purchaseInfoFromServer = new JSONObject(response);
                            if(purchaseInfoFromServer.getBoolean("isValid")){
                                AcknowledgePurchaseParams acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder().setPurchaseToken(purchase.getPurchaseToken()).build();
                                billingClient.acknowledgePurchase(
                                        acknowledgePurchaseParams,
                                        new AcknowledgePurchaseResponseListener() {
                                            @Override
                                            public void onAcknowledgePurchaseResponse(@NonNull BillingResult billingResult) {
                                                if(billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK){
                                                    Toast.makeText(activity, "ACKNOWLEDGED!", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        }
                                );
                            }
                        }catch (Exception err){

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        Volley.newRequestQueue(this).add(stringRequest);

    }

    private void getProductDetails(){
        List<String> productIds = new ArrayList<>();
        productIds.add("card_package_hot");
        SkuDetailsParams getProductDetailsQuery = SkuDetailsParams
                .newBuilder()
                .setSkusList(productIds)
                .setType(BillingClient.SkuType.INAPP)
                .build();
        billingClient.querySkuDetailsAsync(
                getProductDetailsQuery,
                new SkuDetailsResponseListener() {
                    @Override
                    public void onSkuDetailsResponse(@NonNull BillingResult billingResult, @Nullable List<SkuDetails> list) {
                        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && list != null) {
                            TextView itemNameHotTextView = findViewById(R.id.hotPaket_label);
                            TextView itemPrice = findViewById(R.id.hotPaket_price);
                            itemInfo = list.get(0);
                            itemNameHotTextView.setText(itemInfo.getTitle());
                            itemPrice.setText(itemInfo.getPrice());
                        }
                    }
                });
    }

    private void initializeViews() {
        startGameButton = findViewById(R.id.button_startGameLoop);
        cardViews = findViewById(R.id.package_cards);
        selectedPackageName = null;
    }

    private void checkButtonActivation() {
        startGameButton.setEnabled(false);
        if (selectedPackageName != null && !selectedPackageName.equals("")){
            startGameButton.setEnabled(true);
        }
    }

    //onclick von startGameButton
    public void startGameWithSelectedPackage(View view){
        Intent intent = new Intent(this, GameConfigurationActivity.class);
        startActivity(intent);
    }

    public void selectStandardPackage(View view){
        Toast.makeText(this, "ACKNOWLEDGED!", Toast.LENGTH_LONG).show();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                "https://europe-west1-saufapparat.cloudfunctions.net/verifyPurchases?purchaseToken=adsasadsdasdsad&orderId=orderrrrr&purchaseTime=jhkads&isValid=true",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        Volley.newRequestQueue(PackageSelectionPage.this).add(stringRequest);


        selectedPackageName = GamePackage.StandardPackage;
        startGameButton.setEnabled(true);
        resetAllColorsFromPackages();
        highLightSelectedPackage(0);
    }

    public void selectOnlinePackage(View view){
        selectedPackageName = GamePackage.OnlinePackage;
        startGameButton.setEnabled(true);
        resetAllColorsFromPackages();
        highLightSelectedPackage(1);
    }

    public void selectActivityPackage(View view){
        selectedPackageName = GamePackage.ActivityPackage;
        startGameButton.setEnabled(true);
        resetAllColorsFromPackages();
        highLightSelectedPackage(2);
    }

    public void selectHotPackage(View view){
        billingClient.launchBillingFlow(
                this,
                BillingFlowParams
                        .newBuilder()
                        .setSkuDetails(itemInfo)
                        .build());
        selectedPackageName = GamePackage.HotPackage;
        startGameButton.setEnabled(true);
        resetAllColorsFromPackages();
        highLightSelectedPackage(3);
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
            case "hot":
                infoLabel = getString(R.string.hotPaket_label);
                informationText=getString(R.string.hotPaket_infoText);
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

    public static GamePackage getSelectedPackage(){
        return selectedPackageName;
    }
}