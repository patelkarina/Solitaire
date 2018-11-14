import java.util.*;

/**
 * This class contains the basic features of the Solitaire game.  
 * 
 */
public class Solitaire 
{

    public static void main(String[] args)
    {
        new Solitaire();
    }

    private Stack<Card> stock;
    private Stack<Card> waste;
    private Stack<Card>[] foundations;
    private Stack<Card>[] piles;
    private SolitaireDisplay display;

    /**
     * Solitaire class constructor 
     * 
     */
    public Solitaire()
    {
        foundations = new Stack[4];
        for (int i = 0; i < foundations.length; i++)
        {
            foundations[i] = new Stack<Card>();
        }
        piles = new Stack[7];
        for (int i = 0; i < piles.length; i++)
        {
            piles[i] = new Stack<Card>();
        }
        stock = new Stack<Card>();
        waste = new Stack<Card>();
        createStock();
        deal();

        //INSERT CODE HERE

        display = new SolitaireDisplay(this);
    }

    /**
     * Gets the card on top of the stock 
     * @return the card on the top of the stock; null if 
     *         stock is empty 
     */
    public Card getStockCard()
    {
        if (stock.isEmpty())
        {
            return null;
        }
        return stock.peek();
    }

    /**
     * Gets the card on top of the waste 
     * @return the card on the top of the waste; null if 
     *         waste is empty 
     */
    public Card getWasteCard()
    {
        if (waste.isEmpty())
        {
            return null;
        }
        return waste.peek();
    }

    /**
     * Gets the card on top of the given foundation
     * @precondition 0 <= index < 4
     * @return the card on top of the given foundation; null if 
     *         foundation is empty 
     */
    public Card getFoundationCard(int index)
    {
        if (foundations[index].isEmpty())
        {
            return null;
        }
        return foundations[index].peek();
    }

    //precondition:  0 <= index < 7
    //postcondition: returns a reference to the given pile
    /**
     * Gets the pile at the given index 
     * @postcondition 0 <= index < 7
     * @return a reference to the given pile 
     */
    public Stack<Card> getPile(int index)
    {
        return piles[index];
    }

    /**
     * Creates an arraylist of 52 cards and randomly removes
     * cards from it and adds it to the stock until no cards are left 
     * to remove 
     */
    private void createStock()
    {
        ArrayList<Card> deck = new ArrayList<Card>();
        for (int i = 1; i <= 13; i++)
        {
            Card k = new Card(i, "c");
            deck.add(k);
        }
        for (int i = 1; i <= 13; i++)
        {
            Card k = new Card(i, "d");
            deck.add(k);
        }
        for (int i = 1; i <= 13; i++)
        {
            Card k = new Card(i, "h");
            deck.add(k);
        }
        for (int i = 1; i <= 13; i++)
        {
            Card k = new Card(i, "s");
            deck.add(k);
        }
        while (deck.size() > 0)
        {
            stock.push(deck.remove((int) (Math.random() * deck.size())));
        }

    }

    /**
     * Deals cards from the stock to the 7 piles 
     */
    private void deal()
    {
        for (int i = 0; i < piles.length; i++)
        {
            for (int k = 0; k < i + 1; k++)
            {
                piles[i].push(stock.pop());
            }
            piles[i].peek().turnUp();
        }
    }

    /**
     * Moves the top three cards from the stock onto 
     * the top of the waste 
     */
    private void dealThreeCards()
    {
        for (int i = 0; i < 3; i++)
        {
            Card k = stock.pop();
            k.turnUp();
            waste.push(k);
        }
    }

    /**
     * Repeatedly moves the top card from the waste 
     * to the top of the stock 
     */
    public void resetStock()
    {
        while (!waste.isEmpty())
        {
            Card k = waste.pop();
            k.turnDown();
            stock.push(k);
        }
    }

    /**
     * Called when stock is clicked 
     */
    public void stockClicked()
    {
        if (!display.isPileSelected() || !display.isWasteSelected())
        {
            if (!stock.isEmpty())
            {
                dealThreeCards();
            }

            else
            {
                resetStock();
                display.unselect();
            }
        }
    }

    /**
     * Called when waste is clicked 
     */
    public void wasteClicked()
    {
        if (!display.isWasteSelected() && !waste.isEmpty())
        {
            display.selectWaste();
        }
        else if (display.isWasteSelected())
        {
            display.unselect();
        }
    }

    /**
     * Called when given foundation is clicked
     * @precondition 0 <= index < 4
     */
    public void foundationClicked(int index)
    {
        System.out.println("foundation #" + index + " clicked");
        if (display.isWasteSelected())
        {
            if (canAddToFoundation(waste.peek(), index))
            {
                foundations[index].push(waste.pop());
                display.unselect();
            }
        }
        else if (display.isPileSelected())
        {
            if (canAddToFoundation(getPile(display.selectedPile()).peek(), index))
            {
                foundations[index].push(getPile(display.selectedPile()).pop());
                display.unselect();
            }
        }

    }

    //precondition:  0 <= index < 7
    //called when given pile is clicked
    /**
     * Called when given pile is clicked
     * @precondition 0 <= index < 7
     */
    public void pileClicked(int index)
    {
        System.out.println("pile #" + index + " clicked");
        if (display.isWasteSelected())
        {
            Card t = waste.peek();
            if (canAddToPile(t, index))
            {
                piles[index].push(waste.pop());
                piles[index].peek().turnUp();
            }
            display.unselect();
            display.selectPile(index);
        }
        else if (display.isPileSelected())
        {
            int p = display.selectedPile();
            if (index != p)
            {
                Stack<Card> t = removeFaceUpCards(p);
                if (canAddToPile(t.peek(), index))
                {
                    addToPile(t, index);
                    if (!piles[p].isEmpty())
                    {
                        piles[p].peek().turnUp();
                    }
                    display.unselect();
                }
                else
                {
                    addToPile(t, p);
                    display.unselect();
                    display.selectPile(index);
                }
            }
            else
            {
                display.unselect();
            }
        }
            else
            {
                display.selectPile(index);
                piles[index].peek().turnUp();
            }
        }

    

    /**
     * Tests if a card can be legally moved to the top of the given pile
     * @precondition 0 <= index < 7
     * @return true if legal, false otherwise 
     */
    private boolean canAddToPile(Card card, int index)
    {
        Stack<Card> p = getPile(index);
        if (p.isEmpty() && card.getRank() == 13)
        {
            return true;
        }
        else if (!p.isEmpty() && p.peek().isFaceUp())
        {
            return (p.peek().getRank() == card.getRank() + 1 && 
                ((p.peek().isRed() && !card.isRed()) || (!p.peek().isRed() && card.isRed())));
        }
        return false;
    }

    /**
     * Removes cards that are face up from the given pile 
     * and adds it to a new stack
     * @precondition 0 <= index < 7
     * @postcondition removes all face up cards on the top
     * of the given pile
     * @return a stack containing these cards
     */
    private Stack<Card> removeFaceUpCards(int index)
    {
        Stack<Card> s = new Stack<Card>();
        while (! piles[index].isEmpty() && piles[index].peek().isFaceUp())
        {
            s.push(piles[index].pop());
        }
        return s;
    }

    /**
     * @precondition 0 <= index < 7
     * @postcondition removes elements from cards and 
     * adds them to the given pile 
     */
    private void addToPile(Stack<Card> cards, int index)
    {
        Stack<Card> p = getPile(index);
        while (!cards.empty())
        {
            p.push(cards.pop());
        }
    }

    /**
     * Tests if a card can be legally moved to the top of the foundation
     * @precondition 0 <= index < 4
     * @return true if legal, false otherwise 
     */
    private boolean canAddToFoundation(Card card, int index)
    {
        Card c = getFoundationCard(index);
        if (c == null)
        {
            return card.getRank() == 1;
        }

        return (c.getRank() == card.getRank() - 1) && 
        (c.getSuit().equals(card.getSuit()));
    }    
}

