package Uno;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Game {
    private int currentPlayer ;
    private String[] playerIds;
    private UnoDeck deck;
    private ArrayList<ArrayList<UnoCard>> playerHand;
    private ArrayList<UnoCard> stockPile;

    private UnoCard.Color validColor;
    private UnoCard.Value validValue;

    boolean gameDirection;

    public Game(String[] pids) {
        deck = new UnoDeck();
        deck.shuffle();
        stockPile = new ArrayList<UnoCard>();

        playerIds = pids;
        currentPlayer = 0;
        gameDirection = false;

        playerHand = new ArrayList<ArrayList<UnoCard>>();

        for (int i = 0; i < pids.length; i++) {
            ArrayList<UnoCard> hand = new ArrayList<UnoCard>(Arrays.asList(deck.drawCard(7)));
            playerHand.add(hand);
        }
    }

    public void start(Game game) {
        UnoCard card = deck.drawCard();
        validColor = card.getColor();
        validValue  = card.getValue();

        if (card.getValue() == UnoCard.Value.Wild) {
            start(game);
        }

        if( card.getValue() == UnoCard.Value.Wild_Four || card.getValue() == UnoCard.Value.DrawTwo ) {
            start(game);
        }

        if (card.getValue() == UnoCard.Value.Skip) {
            JLabel message = new JLabel(playerIds[currentPlayer] + " đã mất lượt" );
            message.setFont(new Font("Arial", Font.BOLD, 48));
            JOptionPane.showMessageDialog(null, message);

            if(gameDirection == false) {
                currentPlayer = (currentPlayer + 1 ) % playerIds.length;
                
            } else if (gameDirection == true) {
                currentPlayer = (currentPlayer - 1) % playerIds.length;
                if (currentPlayer==-1) {
                    currentPlayer = playerIds.length -1;
                }
            }
        }
        if (card.getValue() == UnoCard.Value.Reverse) {
            JLabel message = new JLabel(playerIds[currentPlayer] + " Đảo chiều" );
            message.setFont(new Font("Arial", Font.BOLD, 48));
            JOptionPane.showMessageDialog(null, message);
            gameDirection ^= true;
            currentPlayer = playerIds.length -1;
        }

        stockPile.add(card);
    }

    public UnoCard getTopCard() {
        return new UnoCard(validColor, validValue);
    }

    public ImageIcon getTopCardImage() {
        return new ImageIcon(validColor + "_" + validValue + ".png");
    }
}