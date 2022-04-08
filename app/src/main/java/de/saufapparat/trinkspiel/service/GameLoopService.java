package de.saufapparat.trinkspiel.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Collections;

import de.saufapparat.trinkspiel.activities.GameConfigurationActivity;
import de.saufapparat.trinkspiel.activities.GroupSelectionPage;
import de.saufapparat.trinkspiel.activities.MainActivity;
import de.saufapparat.trinkspiel.activities.PackageSelectionPage;
import de.saufapparat.trinkspiel.R;
import de.saufapparat.trinkspiel.enmus.ActivitySpezialEnum;
import de.saufapparat.trinkspiel.model.Card;
import de.saufapparat.trinkspiel.util.GamePackageManager;
import de.saufapparat.trinkspiel.util.HelperUtil;
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

    private void toastThatCardDeckFinished(){
        Toast.makeText(this, getString(R.string.karten_gemischt), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        cardIndex=0;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        final String CHANNELID = "Foreground Service ID";
        NotificationChannel channel = new NotificationChannel(
                CHANNELID,
                CHANNELID,
                NotificationManager.IMPORTANCE_LOW
        );

        getSystemService(NotificationManager.class).createNotificationChannel(channel);
        Notification.Builder notification = new Notification.Builder(this, CHANNELID)
                .setContentText("Service is running")
                .setContentTitle("Service enabled")
                .setSmallIcon(R.drawable.ic_launcher_background);

        startForeground(1001, notification.build());


        language = intent.getStringExtra("language");
        reloadPlayersAndCards();
        return super.onStartCommand(intent, flags, startId);
    }

    private void reloadPlayersAndCards(){
        fetchAllPlayers();
        fetchAllCards();
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
                &&  GameConfigurationActivity.getSelectedSpezialPlayer().equals("aus")){
            this.cards = new ArrayList<>(cards.subList(0,repeats-7));
        }
        if (GameConfigurationActivity.getSelectedSpezialActivity() != null
                && GameConfigurationActivity.getSelectedSpezialActivity().equals(ActivitySpezialEnum.aus)){
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
                    .replace("$Sp2", randomPlayer2);

            if(GameConfigurationActivity.getSelectedSpezialPlayer()!=null){
                taskWithPlayerReplaced = taskWithPlayerReplaced.replace("$Spez",  GameConfigurationActivity.getSelectedSpezialPlayer());
            }
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
