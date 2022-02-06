public class BankInterface {
    static boolean verify(int pin, long card) {
        return card > pin;
    }
}
