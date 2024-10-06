package cn.wjk.gulimall.common.validator.validatorConstraint;

import cn.wjk.gulimall.common.validator.annotation.ListValue;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.HashSet;
import java.util.Set;

/**
 * @Package: cn.wjk.gulimall.common.validator.validatorConstraint
 * @ClassName: ListValueConstraintValidatorForInteger
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/6 下午3:00
 * @Description: @ListValue的校验处理器 for integer
 *               第一个泛型传入对应校验注解，第二个泛型传入要处理的数据
 */
public class ListValueConstraintValidatorForInteger implements ConstraintValidator<ListValue, Integer> {
    private final Set<Integer> validIntegers = new HashSet<>();

    /**
     * 初始化
     * @param constraintAnnotation annotation instance for a given constraint declaration
     */
    @Override
    public void initialize(ListValue constraintAnnotation) {
        for (int validInteger : constraintAnnotation.value()) {
            validIntegers.add(validInteger);
        }
    }

    /**
     * 校验
     * @param value integer to validate
     * @param context context in which the constraint is evaluated
     *
     * @return has the verification passed
     */
    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {

        return validIntegers.contains(value);
    }
}
