package edu.kit.trufflehog.model.network.graph;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Hoehler on 29.02.2016.
 */
public class TestNodeTest {
    @Test
    public void getAttributeTest() {
        TestNode testNode = new TestNode();

        System.out.println("" + testNode.getAttribute(Integer.class, "ip"));

        testNode.getProperty(IntegerProperty.class, "ip").addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                System.out.println("new value: "+newValue);
            }
        });
        int i =0;
        while(i < 100) {
            i++;
            testNode.setAttribute(Integer.class, "ip", (int)(Math.random()*100));
        }
    }
}