import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {
    public static class WrongMove
            extends RuntimeException {
        public void IncorrectFileExtensionException(String errorMessage, Throwable err) {
        }
    }
    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("/home/oleksandr/IdeaProjects/lab_2/python_version/lab_2_config.txt");
        Scanner scanner = new Scanner(file);
        scanner.nextLine();
        String S0 = scanner.nextLine();
        String[] finalStates = scanner.nextLine().split(" ");
        Map<String, Map<String, String>> moves = new HashMap<>();
        while (scanner.hasNextLine()) {
            String[] m = scanner.nextLine().split(" ");
            if (!moves.containsKey(m[0])) {
                Map<String, String> new_map = new HashMap<>();
                moves.put(m[0], new_map);
            }
            if (!moves.containsKey(m[2])) {
                Map<String, String> new_map = new HashMap<>();
                moves.put(m[2], new_map);
            }
            moves.get(m[0]).put(m[1], m[2]);
        }

        String word_1 = "";
        String newStart = move(word_1, S0, moves);

        String word_2 = "b";
        List<String> newFinals = new ArrayList<>();
        for (String key : moves.keySet()) {
            String currentState = key;
            try{
                String nextState = move(word_2, key, moves);
                if (Arrays.asList(finalStates).contains(nextState)){
                    boolean add = newFinals.add(key);
                }
            }
            catch (Exception WrongMove){
                ;
            }
        }
        String solution = findSolution(newStart, newFinals, moves);

        System.out.println(steps);
    }
    static List<String> used_states = new ArrayList<>();
    static List<String> steps = new ArrayList<>();
    static String success;
    public static String move(String word, String start, Map<String, Map<String, String>> moves){
        String currentState = start;
        for(int i = 0; i < word.length(); i++){
            if (moves.get(currentState).containsKey(Character.toString(word.charAt(i)))){
            currentState = moves.get(currentState).get(Character.toString(word.charAt(i)));
        }
            else {
            throw new WrongMove();
            }
        }
        return currentState;
    }
    public static String findSolution(String start, List<String> finals, Map<String, Map<String, String>> moves) {
        if (finals.size() == 0){
            return "None";
        }
        if (Arrays.asList(finals).contains(start)){
            return "";
        }
        for (String key : moves.get(start).keySet()) {
            boolean stepMade = false;
            String nextState = moves.get(start).get(key).toString();
            if (finals.contains(nextState)){
                 steps.add(key);
                return "";
            }
            if (!used_states.contains(nextState)){
                steps.add(key);
                stepMade = true;
                success = findSolution(nextState, finals, moves);
            }
            if ((!(Objects.equals(success, "None")))  && (!(Objects.equals(success, "end")))){
                return "end";
            } else if (Objects.equals(success, "end")) {
                return success;
            } else if (stepMade){
                int indexOfLastElement = steps.size() - 1;
                steps.remove(indexOfLastElement);
            }
        }
        return "None";
    }
}