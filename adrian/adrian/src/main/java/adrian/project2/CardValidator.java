package adrian.project2;

import java.util.Set;

public class CardValidator {

    public static String validate(CreditCard card) {
        if (!isIntegerNumbers(card.cardNumber)) {
            return "Error: Card number must contain only digits.";
        }
        if (!isValidLength(card)) {
            return "Error: Invalid card length for " + card.getClass().getSimpleName() + " card.";
        }
        if (!isValidPrefix(card)) {
            return "Error: Invalid card prefix for " + card.getClass().getSimpleName() + " card.";
        }
        if (!isValidLuhn(card.cardNumber)) {
            return "Error: Invalid card number (Luhn check failed).";
        }
        return "Valid Card";
    }

    // Checks if the card number contains only digits
    private static boolean isIntegerNumbers(String cardNumber) {
        for (char number : cardNumber.toCharArray()) {
            if (('0' > number) || (number > '9')) return false;
        }
        return true;
    }

    // Checks if the length is valid for this card type
    private static boolean isValidLength(CreditCard card) {
        return card.allowedLengths.contains(card.cardNumber.length());
    }

    // Checks if the prefix is valid for this card type
    private static boolean isValidPrefix(CreditCard card) {
        for (String prefix : card.allowedPrefixes) {
            if (card.cardNumber.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }

    // Performs the Luhn check
    private static boolean isValidLuhn(String cardNumber) {
        int sum = 0;
        boolean doubleDigit = false;

        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int digit = cardNumber.charAt(i) - '0';
            if (doubleDigit) {
                digit *= 2;
                if (digit > 9) digit -= 9;
            }
            sum += digit;
            doubleDigit = !doubleDigit;
        }

        return (sum % 10 == 0);
    }
}


abstract class CreditCard {
    protected String cardNumber;
    protected Set<Integer> allowedLengths;
    protected Set<String> allowedPrefixes;

    public CreditCard(String cardNumber, Set<String> allowedPrefixes, Set<Integer> allowedLengths) {
        this.cardNumber = cardNumber;
        this.allowedPrefixes = allowedPrefixes;
        this.allowedLengths = allowedLengths;
    }

    public String validate() {
        return CardValidator.validate(this);
    }

    public static CreditCard getCard(String cardType, String cardNumber) {
        switch (cardType) {
            case "Visa":
                return new Visa(cardNumber);
            case "MasterCard":
                return new MasterCard(cardNumber);
            case "American Express":
                return new AmericanExpress(cardNumber);
            case "Discover":
                return new Discover(cardNumber);
            default:
                return null;
        }
    }
}

class Visa extends CreditCard {
    public Visa(String cardNumber) {
        super(cardNumber, Set.of("4"), Set.of(13, 16));
    }
}

class MasterCard extends CreditCard {
    public MasterCard(String cardNumber) {
        super(cardNumber, Set.of("5"), Set.of(16));
    }
}

class AmericanExpress extends CreditCard {
    public AmericanExpress(String cardNumber) {
        super(cardNumber, Set.of("34", "37"), Set.of(15));
    }
}

class Discover extends CreditCard {
    public Discover(String cardNumber) {
        super(cardNumber, Set.of("6"), Set.of(16));
    }
}

