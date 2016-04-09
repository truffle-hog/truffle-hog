package edu.kit.trufflehog.command.usercommand;

import edu.kit.trufflehog.model.configdata.ConfigData;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

/**
 * Created by Valentin Kiechle on 09.04.2016.
 */
public class SetLoggingIntervalCommandTest {

    @Test (expected = UnsupportedOperationException.class)
    public void setLoggingIntervalCommandTest() {
        ConfigData cd = mock(ConfigData.class);
        SetLoggingIntervalCommand slic = new SetLoggingIntervalCommand(cd);
        slic.execute();
        slic.setSelection(null);
    }
}