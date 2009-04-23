package app.navigps.utils;

import java.lang.reflect.Method;

/**
 *
 * @author Grzegorz (wara) Warywoda
 */
public class InvokeUtils{

    /**
     *
     * @param baseClass
     * @param methodName
     * @return
     */
    public static ReturnValue invokeWithTime(Object baseClass,String methodName){
        return invokeWithTime(baseClass, methodName, null,null);
    }
   /*
    public static ReturnValue invokeWithTime(Object baseClass,String methodName,
            Object[] params){
        Class [] paramsType = new Class[params.length];

        for (int i = 0; i < params.length; i++) {
            Object object = params[i];
            paramsType [i] = object.getClass().getGenericSuperclass().getClass();
            System.out.println("Type "+object.getClass().getGenericSuperclass());
            System.out.println("isPrimitive "+object.getClass().isPrimitive());
            System.out.println("isSynthetic "+object.getClass().isSynthetic());
            System.out.println("isAnonymousClass "+object.getClass().isAnonymousClass());
            System.out.println("isArray "+object.getClass().isArray());

            System.out.println("--------------------");
        }
        return invokeWithTime(baseClass, methodName,paramsType,params);
    }
*/
    /**
     *
     * @param baseClass
     * @param methodName
     * @param paramsType
     * @param params
     * @return
     */
    public static ReturnValue invokeWithTime(Object baseClass,String methodName,
                                        Class[] paramsType,Object[] params){
        try {
            Class e = baseClass.getClass();
            Method m = e.getMethod(methodName, paramsType);

            long start = System.nanoTime();
            Object retObj = m.invoke(baseClass, params);
            long stop = System.nanoTime();

            return new ReturnValue(retObj, (stop-start));

        } catch (Exception ex) {
            return new ReturnValue(null, -1,ex);
        }
    }

    /**
     *
     */
    public static class ReturnValue{

        private Object ret;
        private double timeNano;
        private Exception message;

        /**
         *
         * @param obj
         * @param time
         * @param e
         */
        public ReturnValue(Object obj,double time,Exception e){
            ret = obj;
            timeNano = time;
            message = e;
        }
        /**
         *
         * @param obj
         * @param time
         */
        public ReturnValue(Object obj,double time){
            this(obj, time, null);
        }

        /**
         * @return the ret
         */
        public Object getRet() {
            return ret;
        }

        /**
         * @return the timeNano
         */
        public double getTimeNano() {
            return timeNano;
        }

        /**
         * @return the message
         */
        public Exception getMessage() {
            return message;
        }

        /**
         * @param message the message to set
         */
        public void setMessage(Exception message) {
            this.message = message;
        }

        public boolean isMessage(){
            return message != null;
        }
    }
}