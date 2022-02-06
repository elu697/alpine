public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Arg:\n Consumer - nln\n Router1 - nln\n Router2 - nln\n Client - http\n Server - http\n");
            return;
        }
        if (args.length != 1) return;

        String mode = args[0];

        switch (mode) {
            case "Consumer": break;
            case "Router1": break;
            case "Router2": break;
            case "Client": break;
            case "Server": break;
        }
    }
}
