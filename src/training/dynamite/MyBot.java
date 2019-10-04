package training.dynamite;

import com.softwire.dynamite.bot.Bot;
import com.softwire.dynamite.game.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MyBot implements Bot {

    int dynamiteThrownByTwoSixteen = 0;
    int dynamiteThrownByOpponent = 0;
    int rock_vibeCheck = 0;
    int scissors_vibeCheck = 0;
    int paper_vibeCheck = 0;
    int dynamite_vibeCheck = 0;
    Move opponentAlwaysThrows = null;
    Move counterWith = null;
    boolean countering = false;
    int strategyCheckPhase = 0;
    int deceptionPhase = 0;
    int deceptionChoice = 0;
    Random aynRand = new Random();
    int round = 0;

    public MyBot() {


        System.out.println("Unit 0216 online.");
    }

    @Override
    public Move makeMove(Gamestate gamestate) {

        round ++;

        List<Round> previousRounds = gamestate.getRounds();




        //Checks if dynamite was thrown by the opponent in the last round.
        try{
            //System.out.println("Last round, 0216 threw: " + previousRounds.get(previousRounds.size() - 1).getP1() + " and the opponent threw: " + previousRounds.get(previousRounds.size() - 1).getP2());

            if (previousRounds.get(previousRounds.size() - 1).getP2().equals(Move.D)){
                dynamiteThrownByOpponent++;
            }
        } catch (Exception e) {
            //Array too smol.
        }

        //Check for brute-force strategies at round 10 and 110.
        if(previousRounds.size() == 10 || previousRounds.size() == 111){

            strategyCheckPhase++;
            rock_vibeCheck = 0;
            paper_vibeCheck = 0;
            scissors_vibeCheck = 0;
            dynamite_vibeCheck = 0;

            //Check the last 10 rounds for signs of repetition.
            for (int i = previousRounds.size()-10; i != previousRounds.size(); i++){
                if(previousRounds.get(i).getP2().equals(Move.D)){
                    dynamite_vibeCheck++;
                }
                if(previousRounds.get(i).getP2().equals(Move.R)){
                    rock_vibeCheck++;
                }
                if(previousRounds.get(i).getP2().equals(Move.P)){
                    paper_vibeCheck++;
                }
                if(previousRounds.get(i).getP2().equals(Move.S)){
                    scissors_vibeCheck++;
                }
            }

            System.out.println("Phase " + strategyCheckPhase);

            if (rock_vibeCheck == 10){
                opponentAlwaysThrows = Move.R;
                counterWith = Move.P;
                countering = true;
                System.out.println("Opponent always throws rock.");
            }
            else if(scissors_vibeCheck == 10){
                opponentAlwaysThrows = Move.S;
                counterWith = Move.R;
                countering = true;
                System.out.println("Opponent always throws scissors.");
            }
            else if (paper_vibeCheck == 10){
                opponentAlwaysThrows = Move.P;
                counterWith = Move.S;
                countering = true;
                System.out.println("Opponent always throws paper.");
            }
            else if (dynamite_vibeCheck == 10){
                opponentAlwaysThrows = Move.D;
                counterWith = Move.W;
                countering = true;
                System.out.println("Opponent always throws dynamite.");
            } else {
                System.out.println("Opponent is random.");
                opponentAlwaysThrows = null;
                counterWith = null;
                countering = false;
            }
        }



        List<Move> moveList = new ArrayList<>();

        if(countering){ //Counter a specific move.
            //System.out.println("Countering " + opponentAlwaysThrows + " with " + counterWith);
            return counterWith;
        } else { //Or else act randomly, playing 1 move 10 times in a row.

            moveList.add(Move.R);
            moveList.add(Move.P);
            moveList.add(Move.S);
            if (dynamiteThrownByTwoSixteen < 99){
                moveList.add(Move.D);
            }

            if(deceptionPhase == 10){
                aynRand = new Random();
                deceptionChoice = aynRand.nextInt(moveList.size() - 1);
                deceptionPhase = 0;
            }
        }

        Move moveToPlay = moveList.get(deceptionChoice);

        deceptionPhase ++;

        //System.out.println("Two Sixteen uses: " + moveToPlay);

        if (moveToPlay.equals(Move.D)) { dynamiteThrownByTwoSixteen++; }

        return moveToPlay;
    }

    public boolean detectRepeatedMoves(List<Round> previousMoves){
        if(previousMoves.size() < 10){
            return false; //Not enough moves to be considered yet.
        }
        for(int i = previousMoves.size()-10; i != previousMoves.size(); i++){
            if(!previousMoves.get(i).getP2().equals(previousMoves.get(i-1).getP2())){
                return false; //No bombing run detected.
            }
        }
        return true; //Repeated moves detected.
    }

}
