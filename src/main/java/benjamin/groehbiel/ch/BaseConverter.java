package benjamin.groehbiel.ch;

import java.util.ArrayList;
import java.util.List;

class BaseConverter {

    public static List<Integer> convert(Long number, Integer fromBase, Integer toBase) {
        int numberLengthUpperBound = (int) (Math.log10(number) / Math.log10(toBase));
        long numberCopy = number;

        List<Integer> convertedNumber = new ArrayList<>();

        for (int i = numberLengthUpperBound; i >= 0; --i) {
            double currentMax = Math.pow(toBase, i);
            Integer multiplier = (int) (numberCopy / currentMax);
            convertedNumber.add(multiplier);
            numberCopy = numberCopy % (long) currentMax;
        }

        return convertedNumber;
    }

}
