package com.jsoniter.extra;

import com.jsoniter.JsonIterator;
import com.jsoniter.output.JsonStream;
import junit.framework.TestCase;

public class TestKeywordStrategy extends TestCase {
    public static class TestObject1 {
        public String _package;
    }
    public void test() {
        KeywordSupport.enable();
        TestObject1 obj = new TestObject1();
        obj._package = "!!!";
        assertEquals("{\"package\":\"!!!\"}", JsonStream.serialize(obj));
        assertEquals(obj._package, JsonIterator.deserialize(JsonStream.serialize(obj), TestObject1.class)._package);
    }
}
