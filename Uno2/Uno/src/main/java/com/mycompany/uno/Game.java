/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.uno;

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

    public boolean isGameOver() {
        for (String player : this.playerIds) {
            if(hasEmtyHand(player)) {
                return true;
            }
        }
        return false;
    }

    public String getCurrentPlayer() {
        return this.playerIds[this.currentPlayer];
    }

    public String getPreviousPlayer(int i) {
        int index = this.currentPlayer - i;
        if (index == -1){
            index = playerIds.length -1;
        }
        return this.playerIds[index];
    }

    public String[] getPlayers() {
        return playerIds;
    }

    public ArrayList<UnoCard> getPlayerHand(String pid) {
        int index = Arrays.asList(playerIds).indexOf(pid);
        return playerHand.get(index);
    }

    public int getPlayerHandSize (String pid) {
        return getPlayerHand(pid).size();
    }

    public UnoCard getPlayerCard(String pid, int choice) {
        ArrayList<UnoCard> hand = getPlayerHand(pid);
        return hand.get(choice);
    }

    public boolean hasEmtyHand(String pid) {
        return getPlayerHand(pid).isEmpty();
    }

    public boolean validCardPlay(UnoCard card) {
        return card.getColor() == validColor || card.getValue() == validValue;
    }

    public void checkPlayerTurn(String pid ) throws  InvalidPlayerTurnException {
        if (this.playerIds[this.currentPlayer] != pid) {
        throw new InvalidPlayerTurnException("không phải lượt của " + pid, pid);
        }
    }

    public void submitDraws(String pid) throws InvalidPlayerTurnException {
        checkPlayerTurn(pid);
        if (deck.isEmty()) {
            deck.replaceDeckWith(stockPile);
            deck.shuffle();
        }
        getPlayerHand(pid).add(deck.drawCard());
        if (gameDirection == false) {
            currentPlayer = (currentPlayer+1) % playerIds.length;
        }
        if (gameDirection == true) {
            currentPlayer = (currentPlayer-1) % playerIds.length;
            if (currentPlayer == -1) {
                currentPlayer = playerIds.length-1;
            }
        }
    }
    public void setCardColor (UnoCard.Color color) {
        validColor = color;
    }

    public void submitPlayerCard(String pid, UnoCard card, UnoCard.Color declareColor)
        throws InvalidColorSubmissionException, InvalidValueSubmissionException, InvalidPlayerTurnException {
            checkPlayerTurn(pid);
            ArrayList<UnoCard> pHand = getPlayerHand(pid);

            if (!validCardPlay(card)) {
                if (card.getColor() == UnoCard.Color.Wild) {
                    validColor = card.getColor();
                    validValue = card.getValue();
                }



                if (card.getColor() != validColor) {
                    JLabel message = new JLabel("Màu hiện tại là màu " + validColor+ ", Không phải màu " +card.getColor());
                    message.setFont(new Font("Arial",Font.BOLD,48));
                    JOptionPane.showMessageDialog(null,message);

                    throw new InvalidColorSubmissionException("Màu hiện tại là màu " + validColor+ ", Không phải màu " +card.getColor(),card.getColor(),validColor) ;

                } else if (card.getValue() != validValue) {
                    JLabel message2 = new JLabel("Số hiện tại là " + validValue+ ", Không phải " +card.getValue());
                    message2.setFont(new Font("Arial",Font.BOLD,48));
                    JOptionPane.showMessageDialog(null,message2);
                    throw new InvalidValueSubmissionException("Số hiện tại là " + validValue+ ", Không phải " +card.getValue(), card.getValue(), validValue);
                }
            }

            pHand.remove(card);
            if (hasEmtyHand(this.playerIds[currentPlayer])) {
                JLabel message2 = new JLabel(this.playerIds[currentPlayer] + " thắng !");
                message2.setFont(new Font("Arial",Font.BOLD,48));
                JOptionPane.showMessageDialog(null,message2);
                System.exit(0);
            }

            validColor = card.getColor();
            validValue = card.getValue();
            stockPile.add(card);

            if(gameDirection == false) {
                currentPlayer = (currentPlayer +1) % playerIds.length;
                
            } else if (gameDirection == true) {
                currentPlayer = (currentPlayer - 1) % playerIds.length;
                if (currentPlayer == -1 ) {
                    currentPlayer = playerIds.length -1;
                }
            }

            if (card.getColor() == UnoCard.Color.Wild) {
                validColor =declareColor;
            }
            if(card.getValue() == UnoCard.Value.DrawTwo) {
                pid = playerIds[currentPlayer];
                getPlayerHand(pid).add(deck.drawCard());
                getPlayerHand(pid).add(deck.drawCard());
                JLabel message = new JLabel(pid + " rút 2 lá!");
            }
        if(card.getValue() == UnoCard.Value.Wild_Four) {
            pid = playerIds[currentPlayer];
            getPlayerHand(pid).add(deck.drawCard());
            getPlayerHand(pid).add(deck.drawCard());
            getPlayerHand(pid).add(deck.drawCard());
            getPlayerHand(pid).add(deck.drawCard());
            JLabel message = new JLabel(pid + " rút 4 lá!");
        }

        if(card.getValue() == UnoCard.Value.Skip) {
            JLabel message = new JLabel(playerIds[currentPlayer] + " mất lượt!");
            message.setFont(new Font("Arial",Font.BOLD,48));
            JOptionPane.showMessageDialog(null,message);
            if (gameDirection == false) {
                currentPlayer = (currentPlayer +1) % playerIds.length;
            } else if (gameDirection == true) {
                currentPlayer = (currentPlayer -1) % playerIds.length;
                if (currentPlayer == -1 ) {
                    currentPlayer = playerIds.length -1;
                }
            }
        }

        if(card.getValue() == UnoCard.Value.Reverse) {
            JLabel message = new JLabel(pid + " đảo chiều !");
            message.setFont(new Font("Arial",Font.BOLD,48));
            JOptionPane.showMessageDialog(null,message);
            gameDirection ^= true;
            if(gameDirection == true) {
                currentPlayer = (currentPlayer -2) % playerIds.length;
                if (currentPlayer == -1 ) {
                    currentPlayer = playerIds.length -1;
                }
                if (currentPlayer == -2 ) {
                    currentPlayer = playerIds.length -1;
                }
            } else if (gameDirection == false ) {
                currentPlayer = (currentPlayer +2) % playerIds.length;

            }
        }

    }


}

class InvalidPlayerTurnException extends Exception {
    String playerId;
    public InvalidPlayerTurnException(String message, String pid) {
        super(message);
        playerId = pid;
    }
    public String getPid() {
        return playerId;
    }
}

class InvalidColorSubmissionException extends Exception {
    private UnoCard.Color expected;
    private UnoCard.Color actual;

    public InvalidColorSubmissionException(String message, UnoCard.Color actual, UnoCard.Color expected) {
        this.actual = actual;
        this.expected = expected;
    }

}

class InvalidValueSubmissionException extends Exception {
    private UnoCard.Value expected;
    private UnoCard.Value actual;

    public InvalidValueSubmissionException(String message, UnoCard.Value actual, UnoCard.Value expected) {
        this.actual = actual;
        this.expected = expected;
    }

}
