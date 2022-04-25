package de.saufapparat.trinkspiel.service;

import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Binder;
import android.os.IBinder;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import de.saufapparat.trinkspiel.activities.GameConfigurationActivity;
import de.saufapparat.trinkspiel.activities.GroupSelectionPage;
import de.saufapparat.trinkspiel.activities.MainActivity;
import de.saufapparat.trinkspiel.activities.PackageSelectionPage;
import de.saufapparat.trinkspiel.R;
import de.saufapparat.trinkspiel.enmus.ActivitySpezialEnum;
import de.saufapparat.trinkspiel.enmus.HotSpezialEnum;
import de.saufapparat.trinkspiel.model.Card;
import de.saufapparat.trinkspiel.util.GamePackageManager;
import de.saufapparat.trinkspiel.util.HelperUtil;
import de.saufapparat.trinkspiel.util.TinyDB;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameLoopService extends Service {

    private int cardIndex;
    private ArrayList<Card> cards = new ArrayList<>();
    private ArrayList<Card> additionalCards;
    private ArrayList<String> players;
    private final IBinder mBinder = new MyBinder();
    private String language;
    private TinyDB tinyDB;
    private boolean isQuickplay = false;
    private Intent intent;
    private boolean serviceStarted;

    private void toastThatCardDeckFinished() {
        Toast.makeText(this, getString(R.string.karten_gemischt), Toast.LENGTH_SHORT).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.intent = intent;
        initialize();
        if(!serviceStarted){
            reloadPlayersAndCards();
            //Toast.makeText(this, "Service started", Toast.LENGTH_SHORT).show();
            serviceStarted = true;
        }
        UpdateConfiguration(language);
        return super.onStartCommand(intent, flags, startId);
    }

    private void initialize() {
        tinyDB = new TinyDB(getApplicationContext());
        language = intent.getStringExtra("language");
        String quickplayExtra = intent.getStringExtra("quickplay");
        if (quickplayExtra != null && "true".equals(quickplayExtra)) {
            isQuickplay = true;

        }
    }

    private void reloadPlayersAndCards() {
        fetchAllPlayers();
        fetchAllCards();
    }

    private void fetchAllPlayers() {
        players = GroupSelectionPage.getPlayerList();
    }

    private void fetchAllCards() {
        if (cards.size() == 0 && isQuickplay) {
            ArrayList<Object> lis = tinyDB.getListObject("cards", Card.class);
            ArrayList<Card> newcards = new ArrayList<>();
            for (Object o : lis) {
                newcards.add((Card) o);
            }
            cards = newcards;
        }
        else{
            cards = prepareCards();
            ArrayList<Object> cardsToSave = new ArrayList<>();
            for (Card card : cards) {
                cardsToSave.add((Object) card);
            }
            tinyDB.putListObject("cards", cardsToSave);
            tinyDB.putString("savedLanguage", language);
            //Toast.makeText(this, "Cards saved", Toast.LENGTH_SHORT).show();
            cardIndex = 0;
        }
    }

    private ArrayList<Card> prepareCards(){
        ArrayList<Card> cardsToPrepare = fetchCardsFromProperties();
        cardsToPrepare = fillCardsWithPlayers(cardsToPrepare);
        shuffleInRandomOrder(cardsToPrepare);
        return cardsToPrepare;
    }

    public void nextCard() {
        updateCardIndex(1);
        if (cardIndex >= cards.size()) {
            addAdditionalCards();
        }
    }

    private void updateCardIndex(int i) {
        cardIndex += i;
        tinyDB.putInt("cardIndex", cardIndex);
    }

    private void addAdditionalCards() {
        additionalCards = prepareCards();
        toastThatCardDeckFinished();
        cards.addAll(additionalCards);
    }

    public void previousCard() {
        updateCardIndex(-1);
        if (cardIndex < 0) {
            cardIndex = 0;
            Toast.makeText(this, "Keine vorherige Karte!", Toast.LENGTH_SHORT).show();
        }
    }

    private ArrayList<Card> fetchCardsFromProperties() {
        return GamePackageManager.getCardsFromProperties(
                this,
                PackageSelectionPage.getSelectedPackageName().toString(),
                language);
    }

    private void shuffleInRandomOrder(ArrayList<Card> list) {
        Collections.shuffle(list);
    }

    private ArrayList<Card> fillCardsWithPlayers(ArrayList<Card> cards) {
        ArrayList<String> temporaryPlayers = players;
        String removedPlayer = null;

        int repeats = cards.size();
        if (GameConfigurationActivity.getSelectedSpezialPlayer() != null
                && isSpecialPlayerSettingOff()) {
            cards = new ArrayList<>(cards.subList(0, repeats - 7));
        }

        if (GameConfigurationActivity.getSelectedSpezialActivity() != null
                && GameConfigurationActivity.getSelectedSpezialActivity().equals(getString(R.string.spezial_standardpack_aus))) {
            cards = new ArrayList<>(cards.subList(0, repeats - 7));
        }

        if (GameConfigurationActivity.getSelectedSpezialHot() != null
                && GameConfigurationActivity.getSelectedSpezialHot().equals(HotSpezialEnum.sicher)) {
            cards = new ArrayList<>(cards.subList(0, repeats - 7));
        }

        //Toast.makeText(getApplicationContext(), cards.size() + " so lang", Toast.LENGTH_LONG).show();

        for (int i = 0; i < cards.size(); i++) {
            String randomPlayer = temporaryPlayers.get(HelperUtil.getRandomNumber(0, temporaryPlayers.size()));
            temporaryPlayers.remove(randomPlayer);
            if (removedPlayer != null) {
                temporaryPlayers.add(removedPlayer);
            }
            removedPlayer = randomPlayer;
            String randomPlayer2 = temporaryPlayers.get(HelperUtil.getRandomNumber(0, temporaryPlayers.size()));

            String taskWithPlayerReplaced = cards.get(i).getAufgabe()
                    .replace("$Sp1", randomPlayer)
                    .replace("$Sp2", randomPlayer2);

            String selectedSpezialPlayer = GameConfigurationActivity.getSelectedSpezialPlayer();
            if (selectedSpezialPlayer!=null && ! selectedSpezialPlayer.equals(getString(R.string.spezial_standardpack_aus))) {
                taskWithPlayerReplaced = taskWithPlayerReplaced.replace("$Spez", GameConfigurationActivity.getSelectedSpezialPlayer());
            }
            cards.get(i).setAufgabe(taskWithPlayerReplaced);

        }
        temporaryPlayers.add(removedPlayer);
        return cards;
    }

    private boolean isSpecialPlayerSettingOff() {
        return GameConfigurationActivity.getSelectedSpezialPlayer().equals(getApplicationContext().getResources().getString(R.string.spezial_standardpack_aus));
    }

    public Card fetchCurrentCard() {
        if (cards == null || cards.size() == 0) {
            reloadPlayersAndCards();
        }
        return cards.get(cardIndex);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return mBinder;
    }

    public class MyBinder extends Binder {
        public GameLoopService getService() {
            return GameLoopService.this;
        }
    }

    private void UpdateConfiguration(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        Resources resources = getResources();
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }
}
