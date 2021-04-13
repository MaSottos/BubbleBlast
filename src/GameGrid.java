import java.util.*;
import java.util.stream.*;

public class GameGrid {
    //Singleton
    private static GameGrid instance = null;
    public static GameGrid getGameGrid() {
        if (instance == null) {
            instance = new GameGrid();
        }
        return instance;
    }

    //Attributes
    private Bubble [][] grid;
    private final String [] ACCEPTED_GRID_COORDINATES = {
            "A1","A2","A3","A4","A5","A6",
            "B1","B2","B3","B4","B5","B6",
            "C1","C2","C3","C4","C5","C6",
            "D1","D2","D3","D4","D5","D6",
            "E1","E2","E3","E4","E5","E6"};
    private int movesLeft;

    //Constructor
    private GameGrid() {
        GridGeneration();
    }

    //Getters and setters
    public int getMovesLeft() {
        return movesLeft;
    }

    //Grid methods
    private void GridGeneration() {

/*
        //***Test**
        Bubble[] test = {new Bubble(1)};
        Bubble[][] gridTest = {test};
        grid = gridTest;
*/

        //Stream che crea 5 array[5]
        grid = IntStream.range(1, 6).mapToObj(a ->
                //Stream che crea 6 bolle casuali e le inserisce negli array del primo stream
                IntStream.range(1, 7)
                        .mapToObj(b -> new Bubble((new Random().nextInt(3))))
                        .toArray(Bubble[]::new))
                .toArray(Bubble[][]::new);

        MovesLeft(grid);
    }

    public void GridStamp(){
        //Stream per ogni array
        Stream.of(grid)
                .map(a -> Arrays.stream(a)
                        //Per ogni array prende solo la view delle bolle
                        .map(Bubble::getAppearance).collect(Collectors.toList()))
                .forEach(System.out::println);
    }

    public boolean Move(String coordinates) {
/*
        Controllo per movimenti rimanenti
*/
        if (MovesLeftController()) {
            int y;
            int x;
/*
        Controllo per la validità delle coordinate
*/
            if (Arrays.asList(ACCEPTED_GRID_COORDINATES).contains(coordinates.toUpperCase())) {
/*
            Trasformazione coordinate in numeri validi per matrice
*/
                switch (coordinates.toUpperCase().charAt(0)) {
                    case 'A' -> x = 0;
                    case 'B' -> x = 1;
                    case 'C' -> x = 2;
                    case 'D' -> x = 3;
                    case 'E' -> x = 4;
                    default -> throw new IllegalStateException("Unexpected value: " + coordinates.toLowerCase().charAt(0));
                }
                y = Integer.parseInt(coordinates.substring(1)) - 1;
/*
            Chiamata per il cambio stato della bolla selezionata
*/
/*
                Verifica esistenza bolla
*/
                if (grid[x][y].Touched()) {
/*
                Controllo per individuare una vittoria
*/
                    if (!CheckNonExplodedBubbles()) {
                        System.out.println("Hai vinto!!!");
                        return false;
                    } else {
                        GridStamp();
                        movesLeft--;
/*
                Controllo per l'ortografia
*/
                        if (movesLeft == 1) {
                            System.out.println("Hai " + movesLeft + " tentativo rimasto");
                        } else {
                            System.out.println("Hai " + movesLeft + " tentativi rimasti");
                        }
                        return true;
                    }
                } else {
                    System.out.println("Nessuna bolla presente, riprova");
                    return true;
                }
            } else {
                System.out.println("Inserire coordinate valide");
                return true;
            }

        } else return false;
    }

    public boolean CheckNonExplodedBubbles() {
/*
        Controllo per individuare bolle non esplose
*/
        return Arrays.stream(grid)
                .map(a-> Arrays.stream(a)
                        .filter(bubble -> bubble.getStatement() != BubbleStatement.EXPLODED)
                        .collect(Collectors.toList()))
                .collect(Collectors.toList())
                .stream().flatMap(Collection::stream)
                .anyMatch(a->a.getStatement() != BubbleStatement.EXPLODED);



    }

    public boolean MovesLeftController() {
/*
            Controllo per individuare le mosse rimanenti
*/
        if (movesLeft == 0 && CheckNonExplodedBubbles()) {
            System.out.println("Hai perso, ritenta!");
            return false;
        }
        else return true;
    }

    //TODO MovesLeft
    public void MovesLeft(Bubble[][] grid){
        movesLeft = 5;
    }
}