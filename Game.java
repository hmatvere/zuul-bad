/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2011.07.31
 */
import java.util.Random;

public class Game 
{
    private Parser parser;
    private Room currentRoom;
    private CommandWords commandWords;
    Random r;
        
    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        r = new Random();
        commandWords = new CommandWords();
        createRooms();
        parser = new Parser();
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        Room closet, mainCorridor, mainCorridor2, toilet, cleanRoom, bossOffice, corridor, storageRoom,
                outside;
      
        // create the rooms
        closet = new Room("in the closet");
        mainCorridor = new Room("in the main corridor");
        mainCorridor2 = new Room("still in the main corridor");
        toilet = new Room("in the toilets");
        cleanRoom = new Room("in the clean room");
        bossOffice = new Room("in the boss' office");
        corridor = new Room("in a corridor");
        storageRoom = new Room("in the storage room");
        outside = new Room("outside, the grass is blue");


        
        // initialise room exits
        closet.setExit("west",mainCorridor);
        mainCorridor.setExit("north", bossOffice);
        mainCorridor.setExit("east", closet);
        mainCorridor.setExit("south", mainCorridor2);
        mainCorridor2.setExit("north", mainCorridor);
        mainCorridor2.setExit("east", cleanRoom);
        mainCorridor2.setExit("west", toilet);
        toilet.setExit("east", mainCorridor2);
        cleanRoom.setExit("west",mainCorridor2);
        bossOffice.setExit("south", mainCorridor);
        bossOffice.setExit("west", corridor);
        corridor.setExit("north", storageRoom);
        corridor.setExit("east", bossOffice);
        storageRoom.setExit("north",outside);
        storageRoom.setExit("south",corridor);
        outside.setExit("south", storageRoom);



        currentRoom = bossOffice;  // start game in boss' office
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
                
        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to the World of Zuul!");
        System.out.println("World of Zuul is a new, incredibly boring adventure game.");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        System.out.println(currentRoom.getLongDescription());
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        if(command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        }

        String commandWord = command.getCommandWord();
        if (commandWord.equals("help")) {
            printHelp();
        }
        else if (commandWord.equals("go")) {
            goRoom(command);
        }
        else if (commandWord.equals("quit")) {
            wantToQuit = quit(command);
        }
        else if(commandWord.equals("look")){
            System.out.println(currentRoom.getLongDescription());
        }
        else if(commandWord.equals("rest")){
            rest();
        }
        else if(commandWord.equals("eat")){
            eat();
        }

        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("You are lost. You are alone. You wander.");
        System.out.println();
        System.out.println("Your command words are:");
        System.out.println(parser.showCommands()); //method in CommandWords
    }

    private void rest(){
        int x = r.nextInt(4)+1;
        System.out.println("You rested for " + x + " hours.");
    }
    private void eat(){
        System.out.println("You just ate an apple pie");
    }

    /** 
     * Try to go in one direction. If there is an exit, enter
     * the new room, otherwise print an error message.
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        // Try to leave current room.
        String direction = command.getSecondWord();
        Room nextRoom = currentRoom.getExit(direction);
        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else {
            currentRoom = nextRoom;
            System.out.println(currentRoom.getLongDescription());
        }
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }
}
