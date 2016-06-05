package com.carl.cocos2d.animation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class PairStringTest {
    private static final String POINT = "{15, 120}";
    private static final String SIZE = "{436, 448}";
    private static final String RECT = String.format("{%s, %s}", POINT, SIZE);
    @Test
    public void testParsingPoint() {
        Point p = Point.fromString(POINT);
        assert p != null;
        assertEquals(p.x, 15);
        assertEquals(p.y, 120);
    }

    @Test
    public void testParsingSize() {
        Size s = Size.fromString(SIZE);
        assert s != null;
        assertEquals(s.w, 436);
        assertEquals(s.h, 448);
    }

    @Test
    public void testParsingRect() {
        Rect rect = Rect.fromString(RECT);
        assert rect != null;
        assertEquals(rect.topLeft.x, 15);
        assertEquals(rect.topLeft.y, 120);
        assertEquals(rect.size.w, 436);
        assertEquals(rect.size.h, 448);
    }
}