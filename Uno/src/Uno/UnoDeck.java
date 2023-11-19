package Uno;

public class UnoDeck {

    private UnoCard[] cards;
    private int cardsInDeck;

    public UnoDeck() {
        cards = new UnoCard[108];

    }

    public void reset() {
        UnoCard.Color[] colors = UnoCard.Color.values();
        cardsInDeck = 0;

        for (int i = 0; i <  colors.length - 1; i++) {
            UnoCard.Color color = colors[i];

            cards[cardsInDeck++] = new UnoCard(color, UnoCard.Value.getValue(0));
        }
    }
}
