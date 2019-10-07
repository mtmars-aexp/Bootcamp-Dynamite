package training.dynamite;

import com.softwire.dynamite.bot.Bot;
import com.softwire.dynamite.game.*;

import java.security.SecureRandom;
import java.util.*;

public class MyBot implements Bot {

    int dynamiteThrownByTwoSixteen = 0;
    int dynamiteThrownByOpponent = 0;
    int round = 0;
    boolean drawSuspicion = false;
    int suspicionCount = 0;
    HashMap<Move, Move> counters = new HashMap<>();


    public MyBot() {

        //Define counters.
        counters.put(Move.R, Move.P); //Rock is countered by paper.
        counters.put(Move.P, Move.S); //Paper is counter by scissors.
        counters.put(Move.S, Move.R); //Scissors is beat by rock.
        counters.put(Move.D, Move.W); //Dynamite is beat by water balloon.
        counters.put(Move.W, Move.S); //Water balloon is beat by everything except dynamite.

        System.out.println("Unit 0216 online. Beep boop.");
    }

    @Override
    public Move makeMove(Gamestate gamestate) {

        System.out.println("If you're reading this, please be kind to me, for I am just a widdle bot. <:3c");

        round++;
        List<Round> previousRounds = gamestate.getRounds();

        try{
            if(previousRounds.get(previousRounds.size()-1).getP2().equals(Move.D)){
                dynamiteThrownByOpponent ++;
            }
            if(previousRounds.get(previousRounds.size()-1).getP1().equals(Move.D)){
                dynamiteThrownByTwoSixteen ++;
            }
        } catch (Exception e) {
            //List too smol.
        }

        if(round < 10){ //Gather information for the first 10 rounds.
            return moveRandomly(dynamiteThrownByTwoSixteen); //Move erratically as there is insufficient data for meaningful choices.

        }


        if(detectBruteForce(previousRounds, 5)){
            //System.out.println("Countering brute force.");
            return counters.get(previousRounds.get(previousRounds.size() - 1).getP2()); //Get the most recent move and counter it.

        }

        if(detectIfBeatingPreviousMove(previousRounds, 5, counters)){
            return counters.get(counters.get(previousRounds.get(previousRounds.size() - 1).getP1())); //Plays the counter to the counter of 0216's previous move.
        }

        if(!drawSuspicion) {
            if(suspicionCount != 3 && detectIfThrowingDynamiteOnDraw(previousRounds)){
                suspicionCount ++; //Sees if the opponent throws dynamite after a draw.
                if(suspicionCount == 3){
                    drawSuspicion = true;
                }
            }

        }

        if(drawSuspicion && previousRounds.get(previousRounds.size() - 1).getP1().equals(previousRounds.get(previousRounds.size() - 1).getP2())){
            //If the previous round was a draw, and 0216 is suspicious that the opponent throws dynamite after a draw, throw a water balloon.
            if(dynamiteThrownByOpponent < 100){
                return Move.W; //Throw a water balloon to counter the opponent's dynamite.
            } else {
                return moveRandomly(dynamiteThrownByTwoSixteen); //0216 can move randomly, since the opponent has no dynamite left to throw.
            }

        }

        return moveRandomly(dynamiteThrownByTwoSixteen); //Move randomly if round number is divisible by 2.
    }

    public boolean detectBruteForce(List<Round> previousRounds, int range){

        //Detects if the last 5 moves by the opponent are the same.

        for(int i = previousRounds.size() - range; i != previousRounds.size(); i++){
            if(!previousRounds.get(i).getP2().equals(previousRounds.get(i-1).getP2())){
                return false; //If there isn't a patch between i and i-1's move, return false. No repetition.
            }
        }
        return true; //Repetition detected.
    }

    public Move moveRandomly(int dynamiteThrownByTwoSixteen){

        //Picks a random move from rock-paper-scissors.

        List<Move> moveList = new ArrayList<>();

        moveList.add(Move.R);
        moveList.add(Move.P);
        moveList.add(Move.S);
        if(dynamiteThrownByTwoSixteen < 98){
            moveList.add(Move.D);
        }

        Random aynRand = new Random();

        return moveList.get(aynRand.nextInt(moveList.size()));
    }

    public boolean detectIfBeatingPreviousMove(List<Round> previousRounds, int range, HashMap<Move, Move> counters){

        for(int i = previousRounds.size() - range; i != previousRounds.size(); i++){

            //If the opponent's move is a counter to the move 0216 used last round.

            if(!previousRounds.get(i).getP2().equals(counters.get(previousRounds.get(i-1).getP1()))){
                return false; //Sequence not found during detection.
            }
        }
        return true;
    }

    public boolean detectIfThrowingDynamiteOnDraw(List<Round> previousRounds){

            //Detect if opponent is throwing dynamite on a draw.
            //System.out.println("Move used 2 turns ago by P1: " + previousRounds.get(previousRounds.size() - 2).getP1());
            //System.out.println("Move used 2 turns ago by P2: " + previousRounds.get(previousRounds.size() - 2).getP2());
            //System.out.println("Was it a draw? " + previousRounds.get(previousRounds.size() - 2).getP1().equals(previousRounds.get(previousRounds.size() - 2).getP2()));
            //System.out.println("Move used 1 turn ago by P2: " + previousRounds.get(previousRounds.size() - 1).getP2());
            //System.out.println("Was it dynamite? " + previousRounds.get(previousRounds.size() - 1 ).getP2().equals(Move.D));
            return previousRounds.get(previousRounds.size()-2).getP1().equals(previousRounds.get(previousRounds.size()-2).getP2()) && previousRounds.get(previousRounds.size()-1).getP2().equals(Move.D);

    }

    public Move counterOpponent(List<Round> previousRounds){
        return counters.get(previousRounds.get(previousRounds.size() - 1).getP2());
    }

}
