package cn.wjk.gulimall.common.validator.annotation;

import cn.wjk.gulimall.common.validator.validatorConstraint.ListValueConstraintValidatorForInteger;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @Package: cn.wjk.gulimall.common.validator.annotation
 * @ClassName: ListValue
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/6 下午2:52
 * @Description: 自定义校验---注解部分
 */
@Documented
@Constraint(validatedBy = {ListValueConstraintValidatorForInteger.class})//使用哪个校验处理器
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
public @interface ListValue {
    //下面这三个属性是JSR303规定的校验注解必须有的三个属性
    String message() default "{cn.wjk.common.validator.annotation.ListValue}";//从我们自己编写的配置文件中获取默认消息信息

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int[] value() default {};
}
