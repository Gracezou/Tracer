package com.miracle.convert.custom;

import org.dozer.CustomConverter;

import java.time.*;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;

/**
 * Description:适配JDK-8的dozer自定义转化器
 *
 * @author guobin On date 2018/5/16.
 * @version 1.0
 * @since jdk 1.8
 */
public class Jdk8CompatibilityConverter implements CustomConverter {

    @Override
    public Object convert(Object existingDestinationFieldValue,
                          Object sourceFieldValue,
                          Class<?> destinationClass,
                          Class<?> sourceClass) {
        if (destinationClass == null || sourceClass == null) {
            return existingDestinationFieldValue;
        }

        if (sourceFieldValue == null) {
            return existingDestinationFieldValue;
        }

        /*
         * 使用责任链设计模式进行转化
         */
        ConvertActionEnum[] actions = ConvertActionEnum.values();
        for (ConvertActionEnum action : actions) {
            Result result = action.convert(sourceFieldValue, sourceClass, destinationClass);
            if (result.success) {
                existingDestinationFieldValue = result.obj;
                break;
            }
        }
        return existingDestinationFieldValue;
    }


    /**
     * 转化类型枚举
     * 枚举保证符合单例设计模式
     * 目前没有对lazy-init特别需求,故将所有的情况全部实例化
     * 如果以后对lazy-init有特别需求,可以考虑单例设计模式中的注册/登录方式来构造转换action
     */
    private enum ConvertActionEnum {

        /**
         * {@link Date}转{@link Instant}
         */
        DATE_TO_INSTANT {
            @Override
            Result doConvert(Object srcObj) {
                return new Result(Instant.ofEpochMilli(((Date) srcObj).getTime()));
            }

            @Override
            Class<?> srcClass() {
                return Date.class;
            }

            @Override
            Class<?> destClass() {
                return Instant.class;
            }
        },

        /**
         * {@link Duration}自转
         */
        DURATION_SELF {
            @Override
            Result doConvert(Object srcObj) {
                return new Result(Duration.parse(srcObj.toString()));
            }

            @Override
            Class<?> srcClass() {
                return Duration.class;
            }

            @Override
            Class<?> destClass() {
                return Duration.class;
            }
        },

        /**
         * {@link Instant}自转
         */
        INSTANT_SELF {
            @Override
            Result doConvert(Object srcObj) {
                return new Result(Instant.ofEpochMilli(((Instant)srcObj).toEpochMilli()));
            }

            @Override
            Class<?> srcClass() {
                return Instant.class;
            }

            @Override
            Class<?> destClass() {
                return Instant.class;
            }
        },

        /**
         * {@link Instant}转{@link Date}
         */
        INSTANT_TO_DATE {
            @Override
            Result doConvert(Object srcObj) {
                return new Result(new Date(((Instant) srcObj).toEpochMilli()));
            }

            @Override
            Class<?> srcClass() {
                return Instant.class;
            }

            @Override
            Class<?> destClass() {
                return Date.class;
            }
        },

        /**
         * {@link LocalDate}自转
         */
        LOCAL_DATE_SELF {
            @Override
            Result doConvert(Object srcObj) {
                LocalDate srcObject = (LocalDate) srcObj;
                LocalDate destObject = LocalDate.of(srcObject.getYear(), srcObject.getMonth(), srcObject.getDayOfMonth());
                return new Result(destObject);
            }

            @Override
            Class<?> srcClass() {
                return LocalDate.class;
            }

            @Override
            Class<?> destClass() {
                return LocalDate.class;
            }
        },

        /**
         * {@link LocalDateTime}自转
         */
        LOCAL_DATE_TIME_SELF {
            @Override
            Result doConvert(Object srcObj) {
                LocalDateTime srcObject = (LocalDateTime) srcObj;
                LocalDateTime destObject = LocalDateTime.of(
                        srcObject.getYear(),
                        srcObject.getMonth(),
                        srcObject.getDayOfMonth(),
                        srcObject.getHour(),
                        srcObject.getMinute(),
                        srcObject.getSecond(),
                        srcObject.getNano()
                );
                return new Result(destObject);
            }

            @Override
            Class<?> srcClass() {
                return LocalDateTime.class;
            }

            @Override
            Class<?> destClass() {
                return LocalDateTime.class;
            }
        },

        /**
         * {@link Locale}自转
         */
        LOCALE_SELF {
            @Override
            Result doConvert(Object srcObj) {
                Locale srcObject = (Locale) srcObj;
                String language = Optional.ofNullable(srcObject.getLanguage()).orElse("");
                String country = Optional.ofNullable(srcObject.getCountry()).orElse("");
                String variant = Optional.ofNullable(srcObject.getVariant()).orElse("");
                Locale destObject = new Locale(language, country, variant);
                return new Result(destObject);
            }

            @Override
            Class<?> srcClass() {
                return Locale.class;
            }

            @Override
            Class<?> destClass() {
                return Locale.class;
            }
        },

        /**
         * {@link LocalTime}自转
         */
        LOCAL_TIME_SELF {
            @Override
            Result doConvert(Object srcObj) {
                LocalTime srcObject = (LocalTime) srcObj;
                LocalTime destObject = LocalTime.of(srcObject.getHour(),
                        srcObject.getMinute(),
                        srcObject.getSecond(),
                        srcObject.getNano());
                return new Result(destObject);
            }

            @Override
            Class<?> srcClass() {
                return LocalTime.class;
            }

            @Override
            Class<?> destClass() {
                return LocalTime.class;
            }
        },

        /**
         * {@link Period}自转
         */
        PERIOD_SELF {
            @Override
            Result doConvert(Object srcObj) {
                Period srcObject = (Period) srcObj;
                Period destObject = Period.of(srcObject.getYears(), srcObject.getMonths(), srcObject.getDays());
                return new Result(destObject);
            }

            @Override
            Class<?> srcClass() {
                return Period.class;
            }

            @Override
            Class<?> destClass() {
                return Period.class;
            }
        },

        /**
         * {@link ZonedDateTime}自转
         */
        ZONED_DATE_TIME_SELF {
            @Override
            Result doConvert(Object srcObj) {
                ZonedDateTime srcObject = (ZonedDateTime) srcObj;
                ZonedDateTime destObject = ZonedDateTime.of(
                        srcObject.getYear(),
                        srcObject.getMonthValue(),
                        srcObject.getDayOfMonth(),
                        srcObject.getHour(),
                        srcObject.getMinute(),
                        srcObject.getSecond(),
                        srcObject.getNano(),
                        srcObject.getZone()
                );
                return new Result(destObject);
            }

            @Override
            Class<?> srcClass() {
                return ZonedDateTime.class;
            }

            @Override
            Class<?> destClass() {
                return ZonedDateTime.class;
            }
        },

        /**
         * {@link ZoneId}自转
         */
        ZONE_ID_SELF {
            @Override
            Result doConvert(Object srcObj) {
                return new Result(ZoneId.of(((ZoneId) srcObj).getId()));
            }

            @Override
            Class<?> srcClass() {
                return ZoneId.class;
            }

            @Override
            Class<?> destClass() {
                return ZoneId.class;
            }
        };

        /**
         * 数据转化,这里采用模板设计模式
         * @param srcObj 源数据对象
         * @param srcClass 源数据类型
         * @param destClass 目标数据类型
         * @return 目标数据对象
         */
        public final Result convert(Object srcObj, Class<?> srcClass, Class<?> destClass) {
            return this.validate(srcClass, destClass) ? this.doConvert(srcObj) : new Result(false);
        }

        abstract Result doConvert(Object srcObj);

        abstract Class<?> srcClass();

        abstract Class<?> destClass();

        /**
         * 验证传入的类型是否符合该枚举对应的转化类型,如果是则进行转化
         * @param srcClass 源数据类型
         * @param destClass 目标数据类型
         * @return 判断结果
         */
        private boolean validate(Class<?> srcClass, Class<?> destClass) {
            return srcClass.isAssignableFrom(this.srcClass()) && destClass.isAssignableFrom(this.destClass());
        }

    }

    /**
     * 转化结果类
     */
    private static class Result {

        private boolean success;

        private Object obj;

        public Result(Object obj) {
            this(true, obj);
        }

        Result(boolean success) {
            this(success, null);
        }

        Result(boolean success, Object obj) {
            this.success = success;
            this.obj = obj;
        }

    }
}
