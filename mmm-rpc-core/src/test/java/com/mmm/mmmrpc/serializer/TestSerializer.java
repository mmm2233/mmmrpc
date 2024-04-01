package com.mmm.mmmrpc.serializer;


import org.junit.Test;

import java.util.ServiceLoader;

public class TestSerializer {
    @Test
    public void testSerializer() {
        Serializer serializer = null;
        ServiceLoader<Serializer> loader = ServiceLoader.load(Serializer.class);
        for (Serializer s : loader) {
            serializer = s;
            System.out.println(serializer);
        }
    }
}
