package com.carl.cocos2d.animation;

import com.dd.plist.PropertyListFormatException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.*;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;

@RunWith(JUnit4.class)
public class SpriteSheetParserTest {

    @Test
    public void testSpriteSheetParser() throws PropertyListFormatException,
            ParserConfigurationException, SAXException, ParseException, IOException {
        SimpleFileOpener fo = new SimpleFileOpener();
        SpriteSheetParser parser = new SpriteSheetParser(fo);

        parser.parse("plant_1_4_n_2_1_anim.plist");

        SpriteSheet result = parser.getResult();
        assertNotNull(result);

        List<Sprite> frames = result.getFrames();
        assertNotNull(frames);
        assertEquals(frames.size(), 20);

        for (Sprite frame : frames) {
            String name = frame.getName();
            assertNotNull(name);

            Pattern p = Pattern.compile("plant_1_4_n_2_a\\d{1,2}.png");
            Matcher m = p.matcher(name);
            assertTrue(m.matches());
            assertNotNull(frame.getSpriteColorRect());
            assertNotNull(frame.getSpriteOffset());
            assertNotNull(frame.getSpriteSize());
            assertNotNull(frame.getSpriteSourceSize());
            assertTrue(frame.isSpriteTrimmed());
            assertNotNull(frame.getTextureRect());
            assertNotNull(frame.isTextureRotated());
        }
        assertNotNull(result.getMetaData());
    }
}
