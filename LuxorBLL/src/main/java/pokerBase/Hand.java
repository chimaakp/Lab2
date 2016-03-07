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

	/**
	 * Determines if the hand is a five of a kind
	 * @param h
	 * @param hs
	 * @return
	 */
	public static boolean isHandFiveOfAKind(Hand h, HandScore hs) {
		boolean bHandCheck = false;
		if (h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank() ==
				h.getCardsInHand().get(eCardNo.FifthCard.getCardNo()).geteRank()){
			bHandCheck = true;
			hs.setHandStrength(eHandStrength.FiveOfAKind.getHandStrength());
			hs.setHiHand(h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank().getiRankNbr());
			hs.setLoHand(0);
			//No need for kickers as there are no kickers
		}
		return bHandCheck;
	}

	/**
	 * Determines if the hand is a royal flush
	 * @param h
	 * @param hs
	 * @return
	 */
	public static boolean isHandRoyalFlush(Hand h, HandScore hs) {
		boolean bHandScore = false;
		eSuit suit1 = h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteSuit();
		eSuit suit2 = h.getCardsInHand().get(eCardNo.SecondCard.getCardNo()).geteSuit();
		eSuit suit3 = h.getCardsInHand().get(eCardNo.ThirdCard.getCardNo()).geteSuit();
		eSuit suit4 = h.getCardsInHand().get(eCardNo.FourthCard.getCardNo()).geteSuit();
		eSuit suit5 = h.getCardsInHand().get(eCardNo.FifthCard.getCardNo()).geteSuit();
		if ((suit1 == suit2)&&(suit1 == suit3)&&(suit1 == suit4)&&(suit1 == suit5)){
			int firstCard = h.getCardsInHand().get(eCardNo.FifthCard.getCardNo()).getiCardNbr();
			int secondCard = h.getCardsInHand().get(eCardNo.FourthCard.getCardNo()).getiCardNbr();
			int thirdCard = h.getCardsInHand().get(eCardNo.ThirdCard.getCardNo()).getiCardNbr();
			int fourthCard = h.getCardsInHand().get(eCardNo.SecondCard.getCardNo()).getiCardNbr();
			int fifthCard = h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).getiCardNbr();
			if ((firstCard+1 == secondCard)&&(firstCard+2 == thirdCard)&&(firstCard+3 == fourthCard)
					&&(firstCard+4 == fifthCard)){
				if (h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank() == eRank.ACE){
					bHandScore = true;
					hs.setHandStrength(eHandStrength.RoyalFlush.getHandStrength());
					hs.setHiHand(h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank().getiRankNbr());
					hs.setLoHand(0);
				}}}
		return bHandScore;
		}

	/**
	 * Determines if the hand is both a straight and a flush
	 * @param h
	 * @param hs
	 * @return
	 */
	public static boolean isHandStraightFlush(Hand h, HandScore hs) {
		boolean bHandScore = false;
		eSuit suit1 = h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteSuit();
		eSuit suit2 = h.getCardsInHand().get(eCardNo.SecondCard.getCardNo()).geteSuit();
		eSuit suit3 = h.getCardsInHand().get(eCardNo.ThirdCard.getCardNo()).geteSuit();
		eSuit suit4 = h.getCardsInHand().get(eCardNo.FourthCard.getCardNo()).geteSuit();
		eSuit suit5 = h.getCardsInHand().get(eCardNo.FifthCard.getCardNo()).geteSuit();
		if ((suit1 == suit2)&&(suit1 == suit3)&&(suit1 == suit4)&&(suit1 == suit5)){
			int firstCard = h.getCardsInHand().get(eCardNo.FifthCard.getCardNo()).getiCardNbr();
			int secondCard = h.getCardsInHand().get(eCardNo.FourthCard.getCardNo()).getiCardNbr();
			int thirdCard = h.getCardsInHand().get(eCardNo.ThirdCard.getCardNo()).getiCardNbr();
			int fourthCard = h.getCardsInHand().get(eCardNo.SecondCard.getCardNo()).getiCardNbr();
			int fifthCard = h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).getiCardNbr();
			if ((firstCard+1 == secondCard)&&(firstCard+2 == thirdCard)&&(firstCard+3 == fourthCard)
					&&(firstCard+4 == fifthCard)){
				bHandScore = true;
				hs.setHandStrength(eHandStrength.StraightFlush.getHandStrength());
				hs.setHiHand(h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank().getiRankNbr());
				hs.setLoHand(0);
			}
		}
		return bHandScore;
	}

	/**
	 * Determines if the hand is a four of a kind
	 * @param h
	 * @param hs
	 * @return
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

	/**
	 * Determines if the hand is a full house
	 * @param h
	 * @param hs
	 * @return
	 */
	public static boolean isHandFullHouse(Hand h, HandScore hs) {
		boolean bHandScore = false;
		if ((h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank() ==
				h.getCardsInHand().get(eCardNo.SecondCard.getCardNo()).geteRank())&&
				(h.getCardsInHand().get(eCardNo.ThirdCard.getCardNo()).geteRank() ==
				h.getCardsInHand().get(eCardNo.FifthCard.getCardNo()).geteRank())){
			hs.setHandStrength(eHandStrength.FullHouse.getHandStrength());
			hs.setHiHand(h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank().getiRankNbr());
			hs.setLoHand(0);
			bHandScore = true;
		}else {if ((h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank() ==
				h.getCardsInHand().get(eCardNo.ThirdCard.getCardNo()).geteRank())&&
				(h.getCardsInHand().get(eCardNo.FourthCard.getCardNo()).geteRank() ==
				h.getCardsInHand().get(eCardNo.FifthCard.getCardNo()).geteRank())){
			hs.setHandStrength(eHandStrength.FullHouse.getHandStrength());
			hs.setHiHand(h.getBestCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank().getiRankNbr());
			hs.setLoHand(0);
			bHandScore = true;
		}}
		return bHandScore;
	}

	/**
	 * Determines if the hand is a flush
	 * @param h
	 * @param hs
	 * @return
	 */
	public static boolean isHandFlush(Hand h, HandScore hs) {
		boolean bHandScore = false;
		eSuit suit1 = h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteSuit();
		eSuit suit2 = h.getCardsInHand().get(eCardNo.SecondCard.getCardNo()).geteSuit();
		eSuit suit3 = h.getCardsInHand().get(eCardNo.ThirdCard.getCardNo()).geteSuit();
		eSuit suit4 = h.getCardsInHand().get(eCardNo.FourthCard.getCardNo()).geteSuit();
		eSuit suit5 = h.getCardsInHand().get(eCardNo.FifthCard.getCardNo()).geteSuit();
		if ((suit1 == suit2)&&(suit1 == suit3)&&(suit1 == suit4)&&(suit1 == suit5)){
			hs.setHandStrength(eHandStrength.Flush.getHandStrength());
			hs.setHiHand(h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank().getiRankNbr());
			hs.setLoHand(0);
			bHandScore = true;
		}
		return bHandScore;
	}

	/**
	 * Determines if the hand is a straight
	 * @param h
	 * @param hs
	 * @return
	 */
	public static boolean isHandStraight(Hand h, HandScore hs) {
		boolean bHandScore = false;
		int firstCard = h.getCardsInHand().get(eCardNo.FifthCard.getCardNo()).getiCardNbr();
		int secondCard = h.getCardsInHand().get(eCardNo.FourthCard.getCardNo()).getiCardNbr();
		int thirdCard = h.getCardsInHand().get(eCardNo.ThirdCard.getCardNo()).getiCardNbr();
		int fourthCard = h.getCardsInHand().get(eCardNo.SecondCard.getCardNo()).getiCardNbr();
		int fifthCard = h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).getiCardNbr();
		if ((firstCard+1 == secondCard)&&(firstCard+2 == thirdCard)&&(firstCard+3 == fourthCard)
				&&(firstCard+4 == fifthCard)){
			bHandScore = true;
			hs.setHandStrength(eHandStrength.Straight.getHandStrength());
			hs.setHiHand(h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank().getiRankNbr());
			hs.setLoHand(0);
		}
		return bHandScore;
	}

	/**
	 * Determines if the hand is a three of a kind
	 * @param h
	 * @param hs
	 * @return
	 */
	public static boolean isHandThreeOfAKind(Hand h, HandScore hs) {
		boolean bHandScore = false;
		if (h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank() == 
				h.getCardsInHand().get(eCardNo.ThirdCard.getCardNo()).geteRank()){
			bHandScore = true;
			hs.setHandStrength(eHandStrength.ThreeOfAKind.getHandStrength());
			hs.setHiHand(h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank().getiRankNbr());
			hs.setLoHand(0);
			ArrayList<Card> kickers = new ArrayList<Card>();
			kickers.add(h.getCardsInHand().get(eCardNo.FourthCard.getCardNo()));
			kickers.add(h.getCardsInHand().get(eCardNo.FifthCard.getCardNo()));
		}else{if (h.getCardsInHand().get(eCardNo.SecondCard.getCardNo()).geteRank() ==
				h.getCardsInHand().get(eCardNo.FourthCard.getCardNo()).geteRank()){
			bHandScore = true;
			hs.setHandStrength(eHandStrength.ThreeOfAKind.getHandStrength());
			hs.setHiHand(h.getCardsInHand().get(eCardNo.SecondCard.getCardNo()).geteRank().getiRankNbr());
			hs.setLoHand(0);
			ArrayList<Card> kickers = new ArrayList<Card>();
			kickers.add(h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()));
			kickers.add(h.getCardsInHand().get(eCardNo.FifthCard.getCardNo()));
		}else{if (h.getCardsInHand().get(eCardNo.ThirdCard.getCardNo()).geteRank() ==
				h.getCardsInHand().get(eCardNo.FifthCard.getCardNo()).geteRank()){
			bHandScore = true;
			hs.setHandStrength(eHandStrength.ThreeOfAKind.getHandStrength());
			hs.setHiHand(h.getCardsInHand().get(eCardNo.ThirdCard.getCardNo()).geteRank().getiRankNbr());
			hs.setLoHand(0);
			ArrayList<Card> kickers = new ArrayList<Card>();
			kickers.add(h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()));
			kickers.add(h.getCardsInHand().get(eCardNo.SecondCard.getCardNo()));
		}}}
		return bHandScore;
	}

	/**
	 * Determines if the hand is 2 pair
	 * @param h
	 * @param hs
	 * @return
	 */
	public static boolean isHandTwoPair(Hand h, HandScore hs) {
		boolean bHandScore = false;
		Card Card1 = h.getCardsInHand().get(eCardNo.FirstCard.getCardNo());
		Card Card2 = h.getCardsInHand().get(eCardNo.SecondCard.getCardNo());
		Card Card3 = h.getCardsInHand().get(eCardNo.ThirdCard.getCardNo());
		Card Card4 = h.getCardsInHand().get(eCardNo.FourthCard.getCardNo());
		Card Card5 = h.getCardsInHand().get(eCardNo.FifthCard.getCardNo());
		if (Card1.geteRank() == Card2.geteRank()){
			if (Card3.geteRank() == Card4.geteRank()){
				bHandScore = true;
				hs.setHandStrength(eHandStrength.TwoPair.getHandStrength());
				hs.setHiHand(Card1.geteRank().getiRankNbr());
				hs.setLoHand(0);
				ArrayList<Card> kickers = new ArrayList<Card>();
				kickers.add(Card5);
				hs.setKickers(kickers);
			}else{if (Card4.geteRank() == Card5.geteRank()){
				bHandScore = true;
				hs.setHandStrength(eHandStrength.TwoPair.getHandStrength());
				hs.setHiHand(Card1.geteRank().getiRankNbr());
				hs.setLoHand(0);
				ArrayList<Card> kickers = new ArrayList<Card>();
				kickers.add(Card3);
				hs.setKickers(kickers);
			}}}else{if ((Card2.geteRank() == Card3.geteRank())&&
					(Card4.geteRank() == Card5.geteRank())){
				bHandScore = true;
				hs.setHandStrength(eHandStrength.TwoPair.getHandStrength());
				hs.setHiHand(Card2.geteRank().getiRankNbr());
				hs.setLoHand(0);
				ArrayList<Card> kickers = new ArrayList<Card>();
				kickers.add(Card1);
			}}
		return bHandScore;
		}
				
	/**
	 * Determines if the hand is a pair
	 * @param h
	 * @param hs
	 * @return
	 */
	public static boolean isHandPair(Hand h, HandScore hs) {
		boolean bHandScore = false;
		Card Card1 = h.getCardsInHand().get(eCardNo.FirstCard.getCardNo());
		Card Card2 = h.getCardsInHand().get(eCardNo.SecondCard.getCardNo());
		Card Card3 = h.getCardsInHand().get(eCardNo.ThirdCard.getCardNo());
		Card Card4 = h.getCardsInHand().get(eCardNo.FourthCard.getCardNo());
		Card Card5 = h.getCardsInHand().get(eCardNo.FifthCard.getCardNo());
		if (Card1.geteRank() == Card2.geteRank()){
			bHandScore = true;
			hs.setHandStrength(eHandStrength.Pair.getHandStrength());
			hs.setHiHand(Card1.geteRank().getiRankNbr());
			hs.setLoHand(0);
			ArrayList<Card> kickers = new ArrayList<Card>();
			kickers.add(Card3);
			kickers.add(Card4);
			kickers.add(Card5);
		}else{if (Card2.geteRank() == Card3.geteRank()){
			bHandScore = true;
			hs.setHandStrength(eHandStrength.Pair.getHandStrength());
			hs.setHiHand(Card2.geteRank().getiRankNbr());
			hs.setLoHand(0);
			ArrayList<Card> kickers = new ArrayList<Card>();
			kickers.add(Card1);
			kickers.add(Card4);
			kickers.add(Card5);
		}else{if (Card3.geteRank() == Card4.geteRank()){
			bHandScore = true;
			hs.setHandStrength(eHandStrength.Pair.getHandStrength());
			hs.setHiHand(Card3.geteRank().getiRankNbr());
			hs.setLoHand(0);
			ArrayList<Card> kickers = new ArrayList<Card>();
			kickers.add(Card1);
			kickers.add(Card2);
			kickers.add(Card5);
		}else{if (Card4.geteRank() == Card5.geteRank()){
			bHandScore = true;
			hs.setHandStrength(eHandStrength.Pair.getHandStrength());
			hs.setHiHand(Card4.geteRank().getiRankNbr());
			hs.setLoHand(0);
			ArrayList<Card> kickers = new ArrayList<Card>();
			kickers.add(Card1);
			kickers.add(Card2);
			kickers.add(Card3);
		}}}}
		return bHandScore;
		}
	
	/**
	 * Determines the high card value of a hand
	 * @param h
	 * @param hs
	 * @return
	 */
	public static boolean isHandHighCard(Hand h, HandScore hs) {
		boolean bHandScore = true;
		hs.setHandStrength(eHandStrength.HighCard.getHandStrength());
		hs.setHiHand(h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank().getiRankNbr());
		hs.setLoHand(0);
		ArrayList<Card> kickers = new ArrayList<Card>();
		kickers.add(h.getBestCardsInHand().get(eCardNo.SecondCard.getCardNo()));
		kickers.add(h.getBestCardsInHand().get(eCardNo.ThirdCard.getCardNo()));
		kickers.add(h.getBestCardsInHand().get(eCardNo.FourthCard.getCardNo()));
		kickers.add(h.getBestCardsInHand().get(eCardNo.FifthCard.getCardNo()));
		return bHandScore;
	}

}
