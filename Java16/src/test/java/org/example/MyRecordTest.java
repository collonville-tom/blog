package org.example;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class MyRecordTest {

    Object obj=new MyRecord(0,1);

    @Test
    public void testRecordPatternMatching()
    {
        if(obj instanceof MyRecord r)
        {
            assertEquals(0,r.x());
            assertEquals(1,r.y());
            assertEquals("MyRecord[x=0, y=1]",r.toString());
            assertEquals(1,r.hashCode());
            assertTrue(r.equals(r));
        }
    }

    @Test
    public void testWrapRecordonTheFly()
    {
        record MyWrapRecord(MyRecord r){}
        MyWrapRecord myWrapRecord=new MyWrapRecord((MyRecord)obj);
        assertEquals(0,myWrapRecord.r().x());
        assertEquals(1,myWrapRecord.r().y());
    }

    @Test
    public void testBeCarefulRecord()
    {
        record BeCarefulRecord(List<String> s){}
        List<String> l=new ArrayList<>(List.of("toto","tata"));

        BeCarefulRecord myWrapRecord=new BeCarefulRecord(l);
        assertArrayEquals(List.of("toto","tata").toArray(),myWrapRecord.s().toArray());

        myWrapRecord.s().remove("toto");
        assertArrayEquals(List.of("tata").toArray(),myWrapRecord.s().toArray());
    }
}