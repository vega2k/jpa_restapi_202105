package jpastudy.jpashop;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GroupingTest {
    @Test
    public void groupby() {
        List<Transaction> transactions =
                Arrays.asList( new Transaction(Currency.EUR, 1500.0),
                        new Transaction(Currency.USD, 2300.0),
                        new Transaction(Currency.USD, 9900.0),
                        new Transaction(Currency.EUR, 1100.0),
                        new Transaction(Currency.JPY, 7800.0),
                        new Transaction(Currency.CHF, 6700.0));

        //Map<Currency, List<Transaction>>
        Map<Currency, List<Transaction>> map1 = transactions.stream() //Stream<Transaction>
                .collect(Collectors.groupingBy(Transaction::getCurrency));
                //.collect(Collectors.groupingBy(tx -> tx.getCurrency()));
        System.out.println(map1);

        //Map<Currency, Map<Boolean, List<Transaction>>>

    }

    static class Transaction {
        private final Currency currency;
        private final double value;

        public Transaction(Currency currency, double value) {
            this.currency = currency;
            this.value = value;
        }

        public Currency getCurrency() {
            return currency;
        }

        public double getValue() {
            return value;
        }

        @Override
        public String toString() {
            return currency + " : " + value;
        }
    }

    enum Currency {
        EUR, USD, JPY, GBP, CHF
    }
}
