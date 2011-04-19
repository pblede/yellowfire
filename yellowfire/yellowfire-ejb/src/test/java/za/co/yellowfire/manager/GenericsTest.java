package za.co.yellowfire.manager;

import org.junit.Test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 *
 */
public class GenericsTest {

    private interface Gen<T> {}
    
    private class Generics01<T> implements Gen<T> {

        protected Type[] getGenericType() {
            Type superclasses = getClass().getGenericSuperclass();
            return new Type[] {superclasses};
        }

        public String toString() {
            String str = new String();
            for (Type type : getGenericType()) {
                str += type.toString();
            }
            return str;
        }
    }

    @Test
    public void testRetrievingGenericType() {

        Generics01<Long> l = new Generics01<Long>();
        System.out.println("l = " + l);
    }
}
