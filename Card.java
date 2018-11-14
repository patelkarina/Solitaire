
/**
 * This class contains the basic features associated with a card. 
 * 
 * @author Karina Patel 
 * @version 3 November 2016
 */
public class Card
{
    private int rank;
    private String suit;
    private boolean isFaceUp;
    
    /**
     * Card class constructor
     */
    public Card(int number, String type)
    {
        rank = number;
        suit = type;
        isFaceUp = false;
    }

    /**
     * 
     * @return     the rank of the card (1-13)
     */
    public int getRank()
    {
        return rank;
    }
    
    /**
     * 
     * @return    the suit of the card 
     */
    public String getSuit()
    {
        return suit;
    }
    
    /**
     * Determines if the card is red or not 
     * 
     * @return     true if red, false otherwise 
     */
    public boolean isRed()
    {
        if (suit.equals("d") || suit.equals("h"))
        {       
            return true;
        }
        return false;
    }
    
    /**
     * Determines if the card is face up or not 
     * 
     * @return    true if face up, false otherwise 
     */
    public boolean isFaceUp()
    {
        return isFaceUp;
    }
    
    /**
     * Turns the card to face up 
     * 
     */
    public void turnUp()
    {
        isFaceUp = true;
    }
    
    /**
     * Turns the card to face down 
     * 
     */
    public void turnDown()
    {
        isFaceUp = false;
    }
    
    /**
     * Implements the card images 
     * 
     */
    public String getFileName()
    {
        if (!isFaceUp())
        {
            return "cards//back-honors-16.jpg";
        }
        if (rank==1)
            {
                return "cards//a" + suit + ".gif";
            }
        if (rank == 10)
        {
            return "cards//t" + suit + ".gif";
        }
        if (rank == 11)
        {
            return "cards//j" + suit + ".gif";
        } 
        if (rank == 12)
        {
            return "cards//q" + suit + ".gif";
        }
         if (rank == 13)
        {
            return "cards//k" + suit + ".gif";
        }
        return "cards//" + rank + suit + ".gif";
    }
}
