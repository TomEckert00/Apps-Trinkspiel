package de.saufapparat.trinkspiel.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.saufapparat.trinkspiel.activities.GameConfigurationActivity;
import de.saufapparat.trinkspiel.activities.GroupSelectionPage;
import de.saufapparat.trinkspiel.activities.MainActivity;
import de.saufapparat.trinkspiel.activities.PackageSelectionPage;
import de.saufapparat.trinkspiel.R;
import de.saufapparat.trinkspiel.enmus.ActivitySpezialEnum;
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
    TinyDB tinyDB;

    private void toastThatCardDeckFinished(){
        Toast.makeText(this, getString(R.string.karten_gemischt), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        cardIndex=0;
        tinyDB = new TinyDB(getApplicationContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        language = intent.getStringExtra("language");
        reloadPlayersAndCards();
        return super.onStartCommand(intent, flags, startId);
    }

    private void reloadPlayersAndCards(){
        fetchAllPlayers();
        fetchAllCards();
    }

    public void nextCard(){
        updateCardIndex(1);
        if(cardIndex>=cards.size()){
            addAdditionalCards();
        }
    }

    private void updateCardIndex(int i) {
        cardIndex += i;
        tinyDB.putInt("cardIndex", cardIndex);
    }

    private void addAdditionalCards() {
        additionalCards = fetchCardsFromProperties();
        fillCardsWithPlayers(additionalCards);
        shuffleInRandomOrder(additionalCards);
        toastThatCardDeckFinished();
        cards.addAll(additionalCards);
    }

    public void previousCard(){
        updateCardIndex(-1);
        if (cardIndex<0){
            cardIndex=0;
            Toast.makeText(this, "Keine vorherige Karte!", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchAllPlayers(){
        players = GroupSelectionPage.getPlayerList();
    }

    private void fetchAllCards(){
        if(cards.size() == 0){
            cards = fetchCardsFromProperties();
            fillCardsWithPlayers(cards);
            shuffleInRandomOrder(cards);
            ArrayList<Object> cardsToSave = new ArrayList<>();
            for(Card card : cards){
                cardsToSave.add((Object) card);
            }

            tinyDB.putListObject("cards", cardsToSave);
            cardIndex = 0;
        }
        else{
            ArrayList<Object> lis = tinyDB.getListObject("cards", Card.class);
            ArrayList<Card> newcards = new ArrayList<>();
            for(Object o : lis){
                newcards.add((Card) o);
            }
            cards = newcards;
        }
    }

    private ArrayList<Card> fetchCardsFromProperties() {
        return GamePackageManager.getCardsFromProperties(
                this,
                PackageSelectionPage.getSelectedPackage().toString(),
                language);
    }

    private void shuffleInRandomOrder(ArrayList<Card> list){
        Collections.shuffle(list);
    }

    private void fillCardsWithPlayers(ArrayList<Card> cards){
        ArrayList<Card> temporaryCardDeck = cards;
        ArrayList<String> temporaryPlayers = players;
        String removedPlayer = null;

        int repeats = cards.size();
        if (GameConfigurationActivity.getSelectedSpezialPlayer() != null
                &&  GameConfigurationActivity.getSelectedSpezialPlayer().equals(R.string.spezial_standardpack_aus)){
            this.cards = new ArrayList<>(cards.subList(0,repeats-7));
        }

        if (GameConfigurationActivity.getSelectedSpezialActivity() != null
                && GameConfigurationActivity.getSelectedSpezialActivity().equals(ActivitySpezialEnum.aus)){
            this.cards = new ArrayList<>(cards.subList(0,repeats-7));
        }

        if (GameConfigurationActivity.getSelectedSpezialHot() != null
                && GameConfigurationActivity.getSelectedSpezialHot().equals(R.string.spezial_standardpack_aus)){
            this.cards = new ArrayList<>(cards.subList(0,repeats-7));
        }

        Toast.makeText(getApplicationContext(),this.cards.size() + " so lang",Toast.LENGTH_LONG).show();

        for(int i = 0; i < repeats; i++){
            String randomPlayer = temporaryPlayers.get(HelperUtil.getRandomNumber(0,temporaryPlayers.size()));
            temporaryPlayers.remove(randomPlayer);
            if(removedPlayer != null){
                temporaryPlayers.add(removedPlayer);
            }
            removedPlayer = randomPlayer;
            String randomPlayer2 = temporaryPlayers.get(HelperUtil.getRandomNumber(0,temporaryPlayers.size()));

            String taskWithPlayerReplaced = temporaryCardDeck.get(i).getAufgabe()
                    .replace("$Sp1", randomPlayer)
                    .replace("$Sp2", randomPlayer2);

            if(GameConfigurationActivity.getSelectedSpezialPlayer()!=null){
                taskWithPlayerReplaced = taskWithPlayerReplaced.replace("$Spez",  GameConfigurationActivity.getSelectedSpezialPlayer());
            }
            temporaryCardDeck.get(i).setAufgabe(taskWithPlayerReplaced);

        }
        temporaryPlayers.add(removedPlayer);
        this.cards = temporaryCardDeck;
    }

    public Card fetchCurrentCard(){
        if(cards == null || cards.size() == 0){
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
}
