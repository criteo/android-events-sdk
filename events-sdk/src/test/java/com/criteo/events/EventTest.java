package com.criteo.events;

import junit.framework.Assert;

import org.junit.Test;

public class EventTest {

    @Test
    public void testUserSegmentDefaultValue() throws Exception {
        HomeViewEvent event = new HomeViewEvent();
        Assert.assertEquals(Event.NO_USER_SEGMENT, event.getUserSegment());
    }
}
