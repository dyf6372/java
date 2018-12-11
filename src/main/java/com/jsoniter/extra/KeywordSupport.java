package com.jsoniter.extra;

import com.jsoniter.spi.Binding;
import com.jsoniter.spi.ClassDescriptor;
import com.jsoniter.spi.EmptyExtension;
import com.jsoniter.spi.JsonException;
import com.jsoniter.spi.JsoniterSpi;

public class KeywordSupport
{
    private static boolean enabled;

    public static synchronized void enable() {
        if (enabled) {
            throw new JsonException("KeywordSupport.enable can only be called once");
        }
        enabled = true;
        JsoniterSpi.registerExtension(new EmptyExtension() {
            @Override
            public void updateClassDescriptor(ClassDescriptor desc) {
                for (Binding binding : desc.allBindings()) {
                    String translated = binding.name.startsWith("_") ? binding.name.substring(1) : binding.name;
                    binding.toNames = new String[]{translated};
                    binding.fromNames = new String[]{translated};
                }
            }
        });
    }

}
