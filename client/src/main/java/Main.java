import ui.ChessRepl;

public class Main {
    public static void main(String[] args) {
        ChessRepl repl = new ChessRepl("http://localhost:8080");

        repl.run();
    }
}