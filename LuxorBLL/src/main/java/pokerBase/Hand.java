package pokerBase;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Locale;

import exceptions.DeckException;
import exceptions.HandException;
import pokerEnums.*;

import static java.lang.System.out;
import static java.lang.System.err;

public class Hand {

	private ArrayList<Card> CardsInHand;
	private ArrayList<Card> BestCardsInHand;
	private HandScore HandScore;
	private boolean bScored = false;

	public Hand() {
		CardsInHand = new ArrayList<Card>();
		BestCardsInHand = new ArrayList<Card>();
	}

	public ArrayList<Card> getCardsInHand() {
		return CardsInHand;
	}

	private void setCardsInHand(ArrayList<Card> cardsInHand) {
		CardsInHand = cardsInHand;
	}

	public ArrayList<Card> getBestCardsInHand() {
		return BestCardsInHand;
	}

	public void setBestCardsInHand(ArrayList<Card> bestCardsInHand) {
		BestCardsInHand = bestCardsInHand;
	}

	public HandScore getHandScore() {
		return HandScore;
	}

	public void setHandScore(HandScore handScore) {
		HandScore = handScore;
	}

	public boolean isbScored() {
		return bScored;
	}

	public void setbScored(boolean bScored) {
		this.bScored = bScored;
	}

	public Hand AddCardToHand(Card c) {
		CardsInHand.add(c);
		return this;
	}

	public Hand Draw(Deck d) throws DeckException {
		CardsInHand.add(d.Draw());
		return this;
	}

	/**
	 * EvaluateHand is a static method that will score a given Hand of cards
	 * 
	 * @param h
	 * @return
	 * @throws HandException 
	 */
	public static Hand EvaluateHand(Hand h) throws HandException {

		Collections.sort(h.getCardsInHand());

		//Collections.sort(h.getCardsInHand(), Card.CardRank);

		if (h.getCardsInHand().size() != 5) {
			throw new HandException(h);
		}

		HandScore hs = new HandScore();
		try {
			Class<?> c = Class.forName("pokerBase.Hand");

			for (eHandStrength hstr : eHandStrength.values()) {
				Class[] cArg = new Class[2];
				cArg[0] = pokerBase.Hand.class;
				cArg[1] = pokerBase.HandScore.class;

				Method meth = c.getMethod(hstr.getEvalMethod(), cArg);
				Object o = meth.invoke(null, new Object[] { h, hs });

				// If o = true, that means the hand evaluated- skip the rest of
				// the evaluations
				if ((Boolean) o) {
					break;
				}
			}

			h.bScored = true;
			h.HandScore = hs;

		} catch (ClassNotFoundException x) {
			x.printStackTrace();
		} catch (IllegalAccessException x) {
			x.printStackTrace();
		} catch (NoSuchMethodException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return h;
	}

	/*
	 * Do these like the 4 of a kind example
	 */
	public static boolean isHandFiveOfAKind(Hand h, HandScore hs) {
		hs.setHandStrength(eHandStrength.FiveOfAKind.getHandStrength());

		return false;
	}

	public static boolean isHandRoyalFlush(Hand h, HandScore hs) {
		hs.setHandStrength(eHandStrength.RoyalFlush.getHandStrength());
		return false;
	}

	public static boolean isHandStraightFlush(Hand h, HandScore hs) {
		hs.setHandStrength(eHandStrength.StraightFlush.getHandStrength());
		return false;
	}

	/*
	 * Use this method as a template for the other hand evaluating methods
	 */
	public static boolean isHandFourOfAKind(Hand h, HandScore hs) {
		
		boolean bHandCheck = false;
		
		if (h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank() == 
				h.getCardsInHand().get(eCardNo.FourthCard.getCardNo()).geteRank()) 
		{
			bHandCheck =true;
			hs.setHandStrength(eHandStrength.FourOfAKind.getHandStrength());
			hs.setHiHand(h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank().getiRankNbr());
			hs.setLoHand(0);
			ArrayList<Card> kickers = new ArrayList<Card>();
			kickers.add(h.getCardsInHand().get(eCardNo.FifthCard.getCardNo()));
			hs.setKickers(kickers);
			
		} else if (h.getCardsInHand().get(eCardNo.SecondCard.getCardNo()).geteRank() == 
				h.getCardsInHand().get(eCardNo.FifthCard.getCardNo()).geteRank()) 
		{
			bHandCheck =true;
			hs.setHandStrength(eHandStrength.FourOfAKind.getHandStrength());
			hs.setHiHand(h.getCardsInHand().get(eCardNo.SecondCard.getCardNo()).geteRank().getiRankNbr());
			hs.setLoHand(0);
			ArrayList<Card> kickers = new ArrayList<Card>();
			kickers.add(h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()));
			hs.setKickers(kickers);
		}
		
		return bHandCheck;
	}

	/*
	 * And these like the 4 of a kind example
	 */
	public static boolean isHandFullHouse(Hand h, HandScore hs) {
		hs.setHandStrength(eHandStrength.FullHouse.getHandStrength());
		return false;
	}

	public static boolean isHandFlush(Hand h, HandScore hs) {
		hs.setHandStrength(eHandStrength.Flush.getHandStrength());
		return false;
	}

	public static boolean isHandStraight(Hand h, HandScore hs) {
		hs.setHandStrength(eHandStrength.Straight.getHandStrength());
		return false;
	}

	public static boolean isHandThreeOfAKind(Hand h, HandScore hs) {
		hs.setHandStrength(eHandStrength.ThreeOfAKind.getHandStrength());
		return false;
	}

	public static boolean isHandTwoPair(Hand h, HandScore hs) {
		hs.setHandStrength(eHandStrength.TwoPair.getHandStrength());
		return true;
	}

	public static boolean isHandPair(Hand h, HandScore hs) {
		hs.setHandStrength(eHandStrength.Pair.getHandStrength());
		return false;
	}

	public static boolean isHandHighCard(Hand h, HandScore hs) {
		hs.setHandStrength(eHandStrength.HighCard.getHandStrength());
		return false;
	}

}
