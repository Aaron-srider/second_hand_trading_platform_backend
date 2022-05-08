package fit.wenchao.second_hand_trading_platform_front.utils.dataValidate;

import fit.wenchao.second_hand_trading_platform_front.dao.po.UserPO;
import fit.wenchao.utils.function.ExceptionBiConsumer;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.tuple.Triple;

import java.lang.reflect.Field;
import java.util.*;

import static org.apache.commons.lang3.tuple.Triple.of;


public class NoneAnnoValidator {

    static <L, M, R> List<Triple<L, M, R>> trs(L l1, M m1, R r1) {
        List<Triple<L, M, R>> triples = new ArrayList<>();

        Triple<L, M, R> of = of(l1, m1, r1);

        triples.add(of);
        return triples;
    }

    static <T> Triple<String, ExceptionPredicate, ExceptionBiConsumer> rule(String pn1, ExceptionPredicate ep1, ExceptionBiConsumer ec1) {
        return of(pn1, ep1, ec1);
    }

    public static <T> boolean validate(T obj, Triple<String, ExceptionPredicate, ExceptionBiConsumer>... rules) throws Exception {

        Map<String, CallbackPair> map = new HashMap<>();
        for (Triple<String, ExceptionPredicate, ExceptionBiConsumer> rule : rules) {
            map.put(rule.getLeft(), new CallbackPair(rule.getMiddle(), rule.getRight()));
        }

        return validate(obj, map);
    }


    public static <T> boolean validate(T obj, Map<String, CallbackPair> rules) throws Exception {

        Set<String> keySet = rules.keySet();

        for (String key : keySet) {
            CallbackPair callbackPair = rules.get(key);
            Field declaredField = obj.getClass().getDeclaredField(key);
            declaredField.setAccessible(true);
            Object o = declaredField.get(obj);
            if (!callbackPair.test(o)) {
                callbackPair.failedCallback(obj, declaredField);
                return false;
            }
        }

        return true;
    }


    @Data
    @AllArgsConstructor
    static class CallbackPair {
        ExceptionPredicate tester;
        ExceptionBiConsumer failedCallback;

        public boolean test(Object o) throws Exception {
            return this.tester.test(o);
        }

        public void failedCallback(Object o, Field f) throws Exception {
            this.failedCallback.accept(o, f);
        }
    }


    @FunctionalInterface
    static interface ExceptionPredicate<T> {
        boolean test(T t) throws Exception;
    }

    public static void main(String[] args) throws Exception {
        UserPO wc = UserPO.builder().name("wc").build();
        validate(wc,
                rule("name",
                        (name) -> {
                            return name != null;
                        },
                        (obj, field) -> {
                            System.out.println(obj);
                        }),
                rule("sex",
                        (sex) -> {
                            return sex != null;
                        },
                        (obj, field) -> {
                            throw new RuntimeException("性别为空");
                            //System.out.println(obj);
                        })
        );


    }
}


