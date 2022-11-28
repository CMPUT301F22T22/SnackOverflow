package com.example.snackoverflow;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class IngredientAdapterTest {

    @Test
    public void testGetDateText() {
        Date date = new GregorianCalendar(2022, Calendar.NOVEMBER, 11).getTime();
        Ingredient ingredient = new Ingredient("pasta",date,"Pantry",1,1,"Misc");
        String result = IngredientAdapter.getDateText(ingredient);
        assertEquals("2022-11-11", result);
    }
}
