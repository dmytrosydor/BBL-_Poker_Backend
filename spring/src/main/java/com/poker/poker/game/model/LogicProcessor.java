package com.poker.poker.game.model;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.reverseOrder;
import static java.util.Collections.sort;

public class LogicProcessor {
    public static PlayerBestHand processBestHand(PlayerInGame p, GameState gs){
        List<Card> cards = gs.getCommunityCards();

        cards.addAll(p.getHand());

        List<List<Card>> combinatorics = getAll5CardCombinations(cards);

        PlayerBestHand ans = new PlayerBestHand(Combination.HIGH_CARD, combinatorics.getFirst());

        for (List<Card> combination : combinatorics) {
            PlayerBestHand temp = new PlayerBestHand(getCombination(combination), combination);

            ans = (compareHands(ans, temp) < 2) ? ans : temp;
        }

        return ans;
    }

    private static Combination getCombination(List<Card> cards) {
        int[] rankCount = new int[13];
        int[] suitCount = new int[4];

        for (Card card : cards) {
            rankCount[card.getRank().ordinal()]++;
            suitCount[card.getSuit().ordinal()]++;
        }

        boolean isFlush = false;

        for (int count : suitCount){
            if (count >= 5){
                isFlush = true;
                break;
            }
        }

        boolean isStraight = checkStraight(rankCount);

        return getHandRank(rankCount, isFlush, isStraight);
    }

    private static boolean checkStraight(int[] rankCount) {
        int consecutive = 0;
        for (int i = 0; i < 13; i++) {
            if (rankCount[i] > 0) {
                consecutive++;
                if (consecutive == 5) return true;
            } else {
                consecutive = 0;
            }
        }

        return (rankCount[0] > 0 && rankCount[1] > 0 && rankCount[2] > 0 && rankCount[3] > 0 && rankCount[12] > 0);
    }

    private static Combination isRoyalFlush(int[] rankCount) {
        return (rankCount[8] >= 1 && rankCount[9] >= 1 && rankCount[10] >= 1 && rankCount[11] >= 1 && rankCount[12] >= 1) ? Combination.ROYAL_FLUSH : Combination.STRAIGHT_FLUSH;
    }

    private static Combination getHandRank(int[] rankCount, boolean isFlush, boolean isStraight) {
        int pairs = 0, three = 0, four = 0;

        for (int count : rankCount) {
            if (count == 4) four++;
            if (count == 3) three++;
            if (count == 2) pairs++;
        }

        if (isFlush && isStraight) return isRoyalFlush(rankCount);
        if (four == 1) return Combination.FOUR_OF_A_KIND;
        if (three == 1 && pairs == 1) return Combination.FULL_HOUSE;
        if (isFlush) return Combination.FLUSH;
        if (isStraight) return Combination.STRAIGHT;
        if (three == 1) return Combination.THREE_OF_A_KIND;
        if (pairs == 2) return Combination.TWO_PAIRS;
        if (pairs == 1) return Combination.PAIR;
        return Combination.HIGH_CARD;
    }

    private static List<List<Card>> getAll5CardCombinations(List<Card> cards) {
        sort(cards);

        List<List<Card>> result = new ArrayList<>();
        List<Card> combination = new ArrayList<>();
        generateCombinations(cards, result, combination, 0);
        return result;
    }

    private static void generateCombinations(List<Card> cards, List<List<Card>> result,
                                             List<Card> combination, int start) {

        if (combination.size() == 5) {
            result.add(new ArrayList<>(combination));
            return;
        }

        for (int i = start; i < cards.size(); i++) {
            combination.add(cards.get(i));
            generateCombinations(cards, result, combination, i + 1);
            combination.removeLast();
        }
    }

    public static int compareHands(PlayerBestHand a, PlayerBestHand b) {
        if (a.getCombination() != b.getCombination()) return (a.getCombination().ordinal() > b.getCombination().ordinal()) ? 1 : 2;

        Combination c = a.getCombination();

        return switch (c) {
            case STRAIGHT_FLUSH -> compareStraightFlush(a, b);
            case FOUR_OF_A_KIND -> compareFourOfAKind(a, b);
            case FULL_HOUSE -> compareFullHouse(a, b);
            case FLUSH -> compareFlush(a, b);
            case STRAIGHT -> compareStraight(a, b);
            case THREE_OF_A_KIND -> compareThreeOfAKind(a, b);
            case TWO_PAIRS -> compareTwoPairs(a, b);
            case PAIR -> comparePair(a, b);
            case HIGH_CARD -> compareHighCard(a, b);
            default -> 0;
        };

    }

    private static int compareHighCard(PlayerBestHand a, PlayerBestHand b) {
        List<Card> al = a.getCards(), bl = b.getCards();

        al.sort(reverseOrder());
        bl.sort(reverseOrder());

        for (int i = 0; i < al.size(); i++) {
            if (al.get(i).getRank().ordinal() > bl.get(i).getRank().ordinal()) return 1;

            else if (al.get(i).getRank().ordinal() < bl.get(i).getRank().ordinal()) return 2;
        }

        return 0;
    }

    private static int comparePair(PlayerBestHand a, PlayerBestHand b) {
        List<Card> al = a.getCards(), bl = b.getCards();

        int[] aCount = new int[13], bCount = new int[13];

        for (int i = 0; i < al.size(); i++) {
            aCount[al.get(i).getRank().ordinal()]++;
            bCount[bl.get(i).getRank().ordinal()]++;
        }

        int aa = 0, bb = 0;
        List<Integer> aKickers = new ArrayList<>();
        List<Integer> bKickers = new ArrayList<>();

        for (int i = 0; i < al.size(); i++) {
            if (aCount[i] == 2) aa = i;
            if (bCount[i] == 2) bb = i;

            if (aCount[i] == 1) aKickers.add(i);
            if (bCount[i] == 1) bKickers.add(i);
        }

        if (aa != bb) return (aa > bb) ? 1 : 2;

        aKickers.sort(reverseOrder());
        bKickers.sort(reverseOrder());

        for (int i = 0; i < aKickers.size(); i++) {
            if (aKickers.get(i) > bKickers.get(i)) return 1;

            else if (aKickers.get(i) < bKickers.get(i)) return 2;
        }

        return 0;
    }

    private static int compareTwoPairs(PlayerBestHand a, PlayerBestHand b) {
        List<Card> al = a.getCards(), bl = b.getCards();

        int[] aCount = new int[13], bCount = new int[13];

        for (int i = 0; i < al.size(); i++) {
            aCount[al.get(i).getRank().ordinal()]++;
            bCount[bl.get(i).getRank().ordinal()]++;
        }

        int aFirst = 0, bFirst = 0, aSecond = 0, bSecond = 0, aKicker = 0, bKicker = 0;

        for (int i = 0; i < al.size(); i++) {
            if (aCount[i] == 2) aFirst = i;
            if (bCount[i] == 2) bFirst = i;

            if (aCount[i] == 2 && i != aFirst) aSecond = i;
            if (bCount[i] == 2 && i != bFirst) bSecond = i;

            if (aCount[i] == 1) aKicker = i;
            if (bCount[i] == 1) bKicker = i;
        }

        if (Math.max(aFirst, aSecond) != Math.max(bFirst, bSecond)) return (Math.max(aFirst, aSecond) > Math.max(bFirst, bSecond)) ? 1 : 2;

        if (Math.min(aFirst, aSecond) != Math.min(bFirst, bSecond)) return (Math.min(aFirst, aSecond) > Math.min(bFirst, bSecond)) ? 1 : 2;

        if (aKicker != bKicker) return (aKicker > bKicker) ? 1 : 2;

        return 0;
    }

    private static int compareThreeOfAKind(PlayerBestHand a, PlayerBestHand b) {
        List<Card> al = a.getCards(), bl = b.getCards();

        int[] aCount = new int[13], bCount = new int[13];

        for (int i = 0; i < al.size(); i++) {
            aCount[al.get(i).getRank().ordinal()]++;
            bCount[bl.get(i).getRank().ordinal()]++;
        }

        int aa = 0, bb = 0;
        List<Integer> aKickers = new ArrayList<>();
        List<Integer> bKickers = new ArrayList<>();

        for (int i = 0; i < al.size(); i++) {
            if (aCount[i] == 3) aa = i;
            if (bCount[i] == 3) bb = i;

            if (aCount[i] == 1) aKickers.add(i);
            if (bCount[i] == 1) bKickers.add(i);
        }

        if (aa != bb) return (aa > bb) ? 1 : 2;

        aKickers.sort(reverseOrder());
        bKickers.sort(reverseOrder());

        for (int i = 0; i < aKickers.size(); i++) {
            if (aKickers.get(i) > bKickers.get(i)) return 1;

            else if (aKickers.get(i) < bKickers.get(i)) return 2;
        }

        return 0;
    }

    private static int compareStraight(PlayerBestHand a, PlayerBestHand b) {
        List<Card> al = a.getCards(), bl = b.getCards();

        int[] aCount = new int[13], bCount = new int[13];

        for (int i = 0; i < al.size(); i++) {
            aCount[al.get(i).getRank().ordinal()]++;
            bCount[bl.get(i).getRank().ordinal()]++;
        }

        if (isWheel(aCount) != isWheel(bCount)) return (isWheel(bCount)) ? 1 : 2;

        else if (isWheel(aCount)) return 0;

        int aMax = 0, bMax = 0;

        for (int i = 0; i < 13; i++) {
            if (aCount[i] > 0) aMax = i;
            if (bCount[i] > 0) bMax = i;
        }

        if (aMax != bMax) return (aMax > bMax) ? 1 : 2;

        return 0;
    }

    private static boolean isWheel(int[] a) {
        return a[0] > 0 && a[12] > 0;
    }

    private static int compareFlush(PlayerBestHand a, PlayerBestHand b) {
        return compareHighCard(a, b);
    }

    private static int compareFullHouse(PlayerBestHand a, PlayerBestHand b) {
        List<Card> al = a.getCards(), bl = b.getCards();

        int[] aCount = new int[13], bCount = new int[13];

        for (int i = 0; i < al.size(); i++) {
            aCount[al.get(i).getRank().ordinal()]++;
            bCount[bl.get(i).getRank().ordinal()]++;
        }

        int aThree = 0, bThree = 0, aPair = 0, bPair = 0;

        for (int i = 0; i < al.size(); i++) {
            if (aCount[i] == 3) aThree = i;
            if (bCount[i] == 3) bThree = i;

            if (aCount[i] == 2) aPair = i;
            if (bCount[i] == 2) bPair = i;
        }

        if (aThree != bThree) return (aThree > bThree) ? 1 : 2;

        if (aPair != bPair) return (aPair > bPair) ? 1 : 2;

        return 0;
    }

    private static int compareFourOfAKind(PlayerBestHand a, PlayerBestHand b) {
        List<Card> al = a.getCards(), bl = b.getCards();

        int[] aCount = new int[13], bCount = new int[13];

        for (int i = 0; i < al.size(); i++) {
            aCount[al.get(i).getRank().ordinal()]++;
            bCount[bl.get(i).getRank().ordinal()]++;
        }

        int aa = 0, bb = 0, aSolo = 0, bSolo = 0;

        for (int i = 0; i < al.size(); i++) {
            if (aCount[i] == 4) aa = i;
            if (bCount[i] == 4) bb = i;

            if (aCount[i] == 1) aSolo = i;
            if (bCount[i] == 1) bSolo = i;
        }

        if (aa != bb) return (aa > bb) ? 1 : 2;

        if (aSolo != bSolo) return (aSolo > bSolo) ? 1 : 2;

        return 0;
    }

    private static int compareStraightFlush(PlayerBestHand a, PlayerBestHand b) {
        return compareStraight(a, b);
    }

}
