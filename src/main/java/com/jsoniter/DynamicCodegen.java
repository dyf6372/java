package com.jsoniter;

import com.jsoniter.spi.Decoder;
import javassist.*;

class DynamicCodegen {

    static ClassPool pool = ClassPool.getDefault();

    static {
        pool.insertClassPath(new ClassClassPath(Decoder.class));
    }

    public static Decoder gen(String cacheKey, String source) throws Exception {
        Decoder decoder;
        CtClass ctClass;
        try {
            ctClass = pool.makeClass(cacheKey);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("frozen class (cannot edit)")) {
                try {
                    return (Decoder) Class.forName(cacheKey).newInstance();
                } catch (ClassNotFoundException e1) {
                    ctClass = pool.get(cacheKey);
                    return (Decoder) ctClass.toClass().newInstance();
                }
            } else {
                throw e;
            }
        }
        ctClass.setInterfaces(new CtClass[]{pool.get(Decoder.class.getName())});
        CtMethod staticMethod = CtNewMethod.make(source, ctClass);
        ctClass.addMethod(staticMethod);
        CtMethod interfaceMethod = CtNewMethod.make("" +
                "public Object decode(com.jsoniter.JsonIterator iter) {" +
                "return decode_(iter);" +
                "}", ctClass);
        ctClass.addMethod(interfaceMethod);
        decoder = (Decoder) ctClass.toClass().newInstance();
        return decoder;
    }

    public static void enableStreamingSupport() throws Exception {
        CtClass ctClass = pool.makeClass("com.jsoniter.IterImpl");
        ctClass.setSuperclass(pool.get(IterImplForStreaming.class.getName()));
        ctClass.toClass();
    }
}
