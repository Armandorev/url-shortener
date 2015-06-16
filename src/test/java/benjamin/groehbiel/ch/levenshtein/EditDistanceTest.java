package levenshtein;

import benjamin.groehbiel.ch.levenshtein.EditDistance;
import org.junit.Assert;
import org.junit.Test;

public class EditDistanceTest {

    @Test
    public void simpleExampleTest() {
        EditDistance distance = new EditDistance("goggle", "google");
        Assert.assertEquals(1, distance.getEditDistance());
    }

    @Test
    public void sameStringTest() {
        EditDistance distance = new EditDistance("goggle", "goggle");
        Assert.assertEquals(0, distance.getEditDistance());
    }

    @Test
    public void differentStringTest() {
        EditDistance distance = new EditDistance("aaaaa", "bbbbb");
        Assert.assertEquals(5, distance.getEditDistance());
    }

    @Test
    public void emptyString() {
        EditDistance distance = new EditDistance("aaaaa", "");
        Assert.assertEquals(5, distance.getEditDistance());
    }

    @Test
    public void backtrackTest() {
        EditDistance distance = new EditDistance("abc", "adc");
        Assert.assertEquals(1, distance.getEditDistance());
        distance.backtrack();
    }

    @Test
    public void backtrackTestEmptyString() {
        EditDistance distance = new EditDistance("aaa", "");
        Assert.assertEquals(3, distance.getEditDistance());
        distance.backtrack();
    }

    @Test
    public void backtrackDeletionTest() {
        EditDistance distance = new EditDistance("aaa", "aaab");
        Assert.assertEquals(1, distance.getEditDistance());
        distance.backtrack();
    }

    @Test
    public void backtrackInsertionTest() {
        EditDistance distance = new EditDistance("aaab", "aaa");
        Assert.assertEquals(1, distance.getEditDistance());
        distance.backtrack();
    }

    @Test
    public void backtrackExample() {
        EditDistance distance = new EditDistance("google", "goggle");
        Assert.assertEquals(1, distance.getEditDistance());
        distance.backtrack();
    }


}
