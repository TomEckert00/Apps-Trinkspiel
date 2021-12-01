package de.saufapparat.trinkspiel.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Messenger;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;

import de.saufapparat.trinkspiel.GameConfigurationActivity;
import de.saufapparat.trinkspiel.GroupSelectionPage;
import de.saufapparat.trinkspiel.PackageSelectionPage;
import de.saufapparat.trinkspiel.R;
import de.saufapparat.trinkspiel.util.Card;
import de.saufapparat.trinkspiel.util.GamePackageManager;
import de.saufapparat.trinkspiel.util.HelperUtil;
import de.saufapparat.trinkspiel.util.Kategorie;
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
    private static String spezialPlayer = null;

    private void toastThatCardDeckFinished(){
        Toast.makeText(this, getString(R.string.karten_gemischt), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        reloadPlayersAndCards();
        cardIndex=0;
    }

    private void reloadPlayersAndCards(){
        spezialPlayer = GameConfigurationActivity.getSelectedSpezialPlayer();
        fetchAllPlayers();
        fetchAllCards();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    public void nextCard(){
        cardIndex++;
        if(cardIndex>=cards.size()){
            addAdditionalCards();
        }
    }

    private void addAdditionalCards() {
        additionalCards = fetchCardsFromProperties();
        fillCardsWithPlayers(additionalCards);
        shuffleInRandomOrder(additionalCards);
        toastThatCardDeckFinished();
        cards.addAll(additionalCards);
    }

    public void previousCard(){
        cardIndex--;
        if (cardIndex<0){
            cardIndex=0;
            Toast.makeText(this, "Keine vorherige Karte!", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchAllPlayers(){
        players = GroupSelectionPage.getPlayerList();
    }

    private void fetchAllCards(){
        cards = fetchCardsFromProperties();
        fillCardsWithPlayers(cards);
        shuffleInRandomOrder(cards);
        cardIndex = 0;
    }

    private ArrayList<Card> fetchCardsFromProperties() {
        return GamePackageManager.getCardsFromProperties(
                this,
                PackageSelectionPage.getSelectedPackage().toString(),
                getResources().getConfiguration().locale.getLanguage());
    }

    private void shuffleInRandomOrder(ArrayList<Card> list){
        Collections.shuffle(list);
    }

    private void fillCardsWithPlayers(ArrayList<Card> cards){
        ArrayList<Card> temporaryCardDeck = cards;
        ArrayList<String> temporaryPlayers = players;
        String removedPlayer = null;

        int repeats = cards.size();
        if (spezialPlayer!=null && spezialPlayer.equals("niemand1234321")){
            this.cards = new ArrayList<>(cards.subList(0,repeats-7));
        }
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
                    .replace("$Sp2", randomPlayer2)
                    .replace("$Spez", spezialPlayer);
            temporaryCardDeck.get(i).setAufgabe(taskWithPlayerReplaced);

        }
        temporaryPlayers.add(removedPlayer);
        cards = temporaryCardDeck;
    }

    public Card fetchCurrentCard(){
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
