/*
 * Developer: Ibrahim khan
 * Program: BlackJack
 * Rules:
 * First, it's the player's turn. The player can decide to hit or stay.
 *  hit: draw another card.
    stay: do nothing.

 *  If the player decides to hit, and their hand value exceeds 21, they go bust (lose).

    Once the player decides to stay, the dealer reveals the hidden card.

    Then, the dealer must hit until their cards total up to 17. At 17 points or higher, the dealer can choose to hit or stay.

    You win if your hand value is higher than the dealer's hand.

    You win if the dealer goes bust (exceeds 21)

    You lose if the dealer's hand value is higher than yours.
 */

import java.util.Vector;
import java.util.Scanner;

public class Blackjack{
 static final Scanner scan = new Scanner(System.in);
  public static void main(String[] args){
    System.out.print("Start the game? (y/n): ");  //get the input 'y' to start the game and 'n' to leave the game
    char choice = scan.next().charAt(0);

    while(choice != 'y' && choice != 'n'){    //input validation
      System.out.println("Please either choose 'y' for yes or 'n' no: ");
      choice = scan.next().charAt(0);
    }

    while(choice == 'y'){   
      playgame();   //here we are calling the actual game method
      System.out.print("Care for another game of blackjack? (y/n): ");  //ask to restart the game?
      choice = scan.next().charAt(0);   
      while(choice != 'y' && choice != 'n'){   
        System.out.println("Please either choose 'y' for yes or 'n' no: ");
        choice = scan.next().charAt(0);
      }
    }

    if(choice == 'n'){    //if the player chooses not to play the game exit the game.
      System.out.println("Have a great day!");
      System.exit(0);
    }

    scan.close(); //release the scanner
  }

  public static void playgame(){

      int playerTotalValue = 0;       //intializing with total points = 0
      int dealerTotalValue = 0;
      char playerChoice;

      Vector<String> playerCards = new Vector<String>();    //vectors to hold cards
      Vector<String> dealerCards = new Vector<String>();
      String hiddenCard = facedownCard();
      playerCards.addElement(getCard());  //getCard function returns a random card string
      playerCards.addElement(getCard());
      dealerCards.addElement(getCard());
      dealerCards.addElement(getCard());

      //Display player cards and dealer cards on console
      System.out.println("Player Cards:\n" + playerCards.get(0) + playerCards.get(1));
      System.out.println("\nDealer Cards:\n" + dealerCards.get(0) + hiddenCard);

      System.out.println("\nPlayers turn...\nChoose 1 or 2:\n1 => Hit\n2 => Stay");
      playerChoice = scan.next().charAt(0);

      while(playerChoice != '1' && playerChoice != '2'){
        System.out.println("\nPlease Choose either 1 or 2:\n1 => Hit\n2 => Stay");
        playerChoice = scan.next().charAt(0);
      }
      System.out.println("PLAYER CHOICE IS " + playerChoice);
      
      // while(playerChoice != (int)playerChoice && playerChoice != 1 && playerChoice != 2){
      //     playerChoice = scan.nextInt();
      //     System.out.println("IN THE VALIDATION ELSE");
      //     System.out.println("\nPlease Choose either 1 or 2:\n1 => Hit\n2 => Stay");
      //     playerChoice = scan.nextInt();
      // }
        
      // while(playerChoice != 1 && playerChoice != 2){
      //   System.out.println("\nPlease Choose either 1 or 2:\n1 => Hit\n2 => Stay");
      //   playerChoice = scan.nextInt();
      // }
      //if the player intially chooses to stay calculate the total points
      if(playerChoice == '2'){
        playerTotalValue = (getCardNumber(playerCards.get(0)) + getCardNumber(playerCards.get(1)));
      }

      //player choice is 1
      while(playerChoice == '1'){
        String playerDraw = getCard();  //draw a new card from the deck
        playerCards.addElement(playerDraw); //add the new card to players hand

        System.out.println("Drawn card: \n" + playerDraw);
        playerTotalValue = 0;   //reinitializing the total points to calculate all the cards in hand
        for(int i = 0; i < playerCards.size();i++){
          playerTotalValue += getCardNumber(playerCards.get(i));  
        }

        //if the total value exceeds 21 thats a bust and player looses
        if(playerTotalValue > 21){
          // System.out.println("\nPLAYER LOST!... The player drew a bust.\n");
          // System.out.println("Total Value: " + playerTotalValue);
          matchDecision(playerTotalValue, dealerTotalValue, playerCards, dealerCards);
          break;
        }
        //ask the player again for hit or stay
        System.out.println("\nPlayers turn...\nChoose 1 or 2:\n1 => Hit\n2 => Stay.");
        playerChoice = scan.next().charAt(0);
        while(playerChoice != '1' && playerChoice != '2'){
          System.out.println("\nPlease Choose either 1 or 2:\n1 => Hit\n2 => Stay");
          playerChoice = scan.next().charAt(0);
        }

        //if chosen 1 then the loop goes again and if chosen 2 then the following code executes
        if(playerChoice == '2'){
          playerTotalValue = 0;
          for(int i =0;i<playerCards.size();i++){
            playerTotalValue += getCardNumber(playerCards.get(i));
          }
        }
      }
if(playerTotalValue < 21){
      //dealer turn starts as player chose to stay.
        System.out.println("\nDealers turn...\n");
        System.out.println("Dealer reveals Hidden card\n" + dealerCards.get(1));  //reveal the hidden card

       dealerTotalValue = (getCardNumber(dealerCards.get(0)) + getCardNumber(dealerCards.get(1)));  //initialize the total value with the first two cards.

       //dealer will keep drawing untill the total hand value reaches 17
       if(dealerTotalValue < 18){
        while(dealerTotalValue < 18){
          String newdealerCard = getCard();
          dealerCards.addElement(newdealerCard);
          System.out.println("Dealer drew: \n" + newdealerCard + "\n");
          dealerTotalValue += getCardNumber(newdealerCard);
         }
       }
       //once the value exceeds 17
       //the dealer will calculate the probability and decide hit or stay
       //based on the probability function
       if(dealerTotalValue > 17){
        int dealerChoice = probability(dealerTotalValue);

          while(dealerChoice == 1 && dealerTotalValue <= 21){
            String newDealerCard = getCard();
            dealerCards.addElement(newDealerCard);
            System.out.println("Dealer drew: \n" + newDealerCard + "\n");

            dealerTotalValue += getCardNumber(newDealerCard);

            dealerChoice = probability(dealerTotalValue);

          }
        //if the dealer chooses to stay or the value exceeds 21(which will be a bust)
        //then call the matchDecision function to declare the winner.
        if(dealerChoice == 2 || dealerTotalValue > 21){
          matchDecision(playerTotalValue, dealerTotalValue, playerCards, dealerCards);
        }
       }
    
}
  
  }

  //this method will select a random card and return the string
  public static String getCard(){
    String card = new String();
    int max = 13;
    int min = 1;
    int range = max - min + 1;
    int randomCard = (int)(Math.random() * range) + min;
    switch(randomCard){
      case 1: card =
             
      "   _____\n"+
      "  |A _  |\n"+ 
      "  | ( ) |\n"+
      "  |(_'_)|\n"+
      "  |  |  |\n"+
      "  |____V|\n";
      break;
      case 2: card = 
      "   _____\n"+              
      "  |2    |\n"+ 
      "  |  o  |\n"+
      "  |     |\n"+
      "  |  o  |\n"+
      "  |____Z|\n";
      break;
      case 3: card =     
      "   _____\n" +
      "  |3    |\n"+
      "  | o o |\n"+
      "  |     |\n"+
      "  |  o  |\n"+
      "  |____E|\n";
      break;
      case 4: card =
      "   _____\n" +
      "  |4    |\n"+
      "  | o o |\n"+
      "  |     |\n"+
      "  | o o |\n"+
      "  |____h|\n";
      break;
      case 5: card =
      "   _____ \n" +
      "  |5    |\n" +
      "  | o o |\n" +
      "  |  o  |\n" +
      "  | o o |\n" +
      "  |____S|\n";
      break;
      case 6: card =
      "   _____ \n" +
      "  |6    |\n" +
      "  | o o |\n" +
      "  | o o |\n" +
      "  | o o |\n" +
      "  |____6|\n";
      break;
      case 7: card =
      "   _____ \n" +
      "  |7    |\n" +
      "  | o o |\n" +
      "  |o o o|\n" +
      "  | o o |\n" +
      "  |____7|\n";
      break;
      case 8: card =
      "   _____ \n" +
      "  |8    |\n" +
      "  |o o o|\n" +
      "  | o o |\n" +
      "  |o o o|\n" +
      "  |____8|\n";
      break;
      case 9: card =
      "   _____ \n" +
      "  |9    |\n" +
      "  |o o o|\n" +
      "  |o o o|\n" +
      "  |o o o|\n" +
      "  |____9|\n";
      break;
      case 10: card =
      "   _____ \n" +
      "  |10  o|\n" +
      "  |o o o|\n" +
      "  |o o o|\n" +
      "  |o o o|\n" +
      "  |___10|\n";
      break;
      case 11: card =
      "   _____\n" +
      "  |J  ww|\n"+ 
      "  | o {)|\n"+ 
      "  |o o% |\n"+ 
      "  | | % |\n"+ 
      "  |__%%[|\n";
      break;
      case 12: card =
      "   _____\n" +
      "  |Q  ww|\n"+ 
      "  | o {(|\n"+ 
      "  |o o%%|\n"+ 
      "  | |%%%|\n"+ 
      "  |_%%%O|\n";
      break;
      case 13: card =
      "   _____\n" +
      "  |K  WW|\n"+ 
      "  | o {)|\n"+ 
      "  |o o%%|\n"+ 
      "  | |%%%|\n"+ 
      "  |_%%%>|\n";
      break;
      default: System.out.println("Could not draw out a card!");
    }
    return card;
  }
//function to create the face down card
  public static String facedownCard(){
      return 
      "   _____\n"+
      "  |  F  |\n"+ 
      "  | F F |\n"+
      "  |F F F|\n"+
      "  |  F  |\n"+
      "  |_____|\n";
  }
//this function will return the value of the card
//it takes a String and returns a value accordingly
  public static int getCardNumber(String card){
    int value = 0;
    switch(card){
      case       
      "   _____\n"+
      "  |A _  |\n"+ 
      "  | ( ) |\n"+
      "  |(_'_)|\n"+
      "  |  |  |\n"+
      "  |____V|\n" :
      value = 1;
      break;
      case       
      "   _____\n"+              
      "  |2    |\n"+ 
      "  |  o  |\n"+
      "  |     |\n"+
      "  |  o  |\n"+
      "  |____Z|\n" :
      value = 2;
      break;
      case
      "   _____\n" +
      "  |3    |\n"+
      "  | o o |\n"+
      "  |     |\n"+
      "  |  o  |\n"+
      "  |____E|\n": 
      value = 3;
      break;
      case
      "   _____\n" +
      "  |4    |\n"+
      "  | o o |\n"+
      "  |     |\n"+
      "  | o o |\n"+
      "  |____h|\n":
      value = 4;
      break;
      case
      "   _____ \n" +
      "  |5    |\n" +
      "  | o o |\n" +
      "  |  o  |\n" +
      "  | o o |\n" +
      "  |____S|\n":
      value = 5;
      break;
      case
      "   _____ \n" +
      "  |6    |\n" +
      "  | o o |\n" +
      "  | o o |\n" +
      "  | o o |\n" +
      "  |____6|\n":
      value = 6;
      break;
      case
      "   _____ \n" +
      "  |7    |\n" +
      "  | o o |\n" +
      "  |o o o|\n" +
      "  | o o |\n" +
      "  |____7|\n":
      value = 7;
      break;
      case
      "   _____ \n" +
      "  |8    |\n" +
      "  |o o o|\n" +
      "  | o o |\n" +
      "  |o o o|\n" +
      "  |____8|\n":
      value = 8;
      break;
      case
      "   _____ \n" +
      "  |9    |\n" +
      "  |o o o|\n" +
      "  |o o o|\n" +
      "  |o o o|\n" +
      "  |____9|\n":
      value = 9;
      break;
      case
      "   _____ \n" +
      "  |10  o|\n" +
      "  |o o o|\n" +
      "  |o o o|\n" +
      "  |o o o|\n" +
      "  |___10|\n":
      value = 10;
      break;
      case 
      "   _____\n" +
      "  |J  ww|\n"+ 
      "  | o {)|\n"+ 
      "  |o o% |\n"+ 
      "  | | % |\n"+ 
      "  |__%%[|\n":
      value = 11;
      break;
      case
      "   _____\n" +
      "  |Q  ww|\n"+ 
      "  | o {(|\n"+ 
      "  |o o%%|\n"+ 
      "  | |%%%|\n"+ 
      "  |_%%%O|\n":
      value = 12;
      break;
      case
      "   _____\n" +
      "  |K  WW|\n"+ 
      "  | o {)|\n"+ 
      "  |o o%%|\n"+ 
      "  | |%%%|\n"+ 
      "  |_%%%>|\n":
      value = 13;
      break;
      default : System.out.println("Could not retrieve value!");
      break;
    }
    return value;
  }
//this probability function will determine the chance of exceeding 21 and make decisions accordingly
  public static int probability(int value){
    int decision = 0;
    if(value == 17 || value == 18){
      int max = 99;
      int min = 0;
      int range = max - min + 1;
      int tempRandom = (int)(Math.random() * range) + min;  //random temp number

      if(tempRandom < 90){  //90% probability that the dealer will stay
        System.out.println("Dealer chose Stay!");
        decision = 2;
      }
      else{   //10% probability that the dealer will go for a hit
        System.out.println("Dealer chose Hit!");
        decision = 1;
      }
    }else if(value == 19 || value == 20){
      int max = 99;
      int min = 0;
      int range = max - min + 1;
      int tempRandom = (int)(Math.random() * range) + min;
      if(tempRandom < 98){  //98% probability the dealer will choose to stay
        System.out.println("Dealer chose Stay!");
        decision = 2;
      }
      else{   //2% probability the dealer will choose to hit(2% chance dealer is feeling a little daring today.)
        System.out.println("Dealer chose Hit!");
        decision = 1;
      }
    }else if(value == 21){  //100% probability the dealer will choose stay(dealer will bust otherwise)
      System.out.println("Dealer chose Stay!");
      decision = 2;
    }
return decision;
  }

  //making the match decision
  public static void matchDecision(int playerTotal, int dealerTotal, Vector<String> playerCards, Vector<String> dealerCards){
    System.out.println("\n\t***GAME OVER***\t\n");
    System.out.println("\n\t***MATCH RESULT***\t\n");

    if(playerTotal > 21){ //is player busted then dealer won
      System.out.println("Dealer Won! Player drew a bust with a total value of: " + playerTotal + "\n");
    }
    else if(dealerTotal > 21){  //if dealer busted then player won
      System.out.println("Player Won! Dealer drew a bust with a total value of: " + dealerTotal + "\n");
    }
    else if(playerTotal > dealerTotal){ //if no one busted and player total hand value is greater than that of dealers then player won.
      System.out.println("\nPlayer Won!\nPlayer Total: " + playerTotal + "\nDealerTotal: " + dealerTotal);
    }
    else if(dealerTotal > playerTotal){//if no one busted and dealers total hand value is greater than that of players then dealer won.
      System.out.println("\nDealer Won!\nPlayer Total: " + playerTotal + "\nDealerTotal: " + dealerTotal);
    }
    else{ //if both have same hands then its a draw
      System.out.println("\nWhat a match! It ended up as a draw!\n");
    }

    System.out.println("Player Cards: \n" );  //show player cards
    showPlayerCards(playerCards);
    
    System.out.println("\nDealer Cards: \n"); //show dealer cards
    showDealerCards(dealerCards);

  }

  //public function to print out dealer cards from vector
  public static void showDealerCards(Vector<String> dealerCards){
    for(int i = 0; i < dealerCards.size(); i++){
      System.out.print(dealerCards.get(i));
    }
  }

  //public function to print out player cards from vector
  public static void showPlayerCards(Vector<String> playerCards){
    for(int i = 0; i < playerCards.size(); i++){
      System.out.print(playerCards.get(i));
    }
  }
}