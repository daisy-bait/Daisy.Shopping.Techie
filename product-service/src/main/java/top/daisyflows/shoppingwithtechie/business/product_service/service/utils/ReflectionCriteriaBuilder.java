package top.daisyflows.shoppingwithtechie.business.product_service.service.utils;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.query.Criteria;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public final class ReflectionCriteriaBuilder {

    private static final String ILIKE = "i";
    private static final String MIN = "min";
    private static final String MAX = "max";
    private static final String BLANK = "";
    private static final Class<?> STRING_CLASS = String.class;
    private static final Class<?> BIGDECIMAL_CLASS = BigDecimal.class;

    @SneakyThrows
    public static <D, T> void buildCriteriaFromDTO(
            List<Criteria> criteriaList, Class<D> documentClass, Class<T> dtoClass, T listRequest
    ) {

        Set<String> documentFields = Arrays.stream(documentClass.getDeclaredFields()).map(Field::getName).collect(Collectors.toSet());
        Field[] fields = dtoClass.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            Object value = field.get(listRequest);
            if (value == null) continue;

            log.info("=====[UTILITY_PREPARE_CRITERIA] Field Name: {}, Field Value: {}=====", field.getName(), value);

            Class<?> fieldType = field.getType();

            if (fieldType.equals(STRING_CLASS)) {
                logTypeField(STRING_CLASS.getSimpleName());
                String fieldAsString = String.valueOf(value);
                if (!fieldAsString.isBlank() && documentFields.contains(field.getName()))
                    criteriaList.add(Criteria.where(field.getName()).regex(fieldAsString, ILIKE));
            } else if (fieldType.equals(BIGDECIMAL_CLASS)) {
                logTypeField(BIGDECIMAL_CLASS.getSimpleName());
                BigDecimal fieldAsNumber = (BigDecimal) value;

                if (field.getName().startsWith(MIN)) criteriaList.add(Criteria.where(
                        lowerCaseFirstLetter(field.getName().replace(MIN, BLANK))
                ).gte(fieldAsNumber));
                else if (field.getName().startsWith(MAX)) criteriaList.add(Criteria.where(
                        lowerCaseFirstLetter(field.getName().replace(MAX, BLANK))
                ).lte(fieldAsNumber));
                else criteriaList.add(Criteria.where(field.getName()).is(fieldAsNumber));

                break;
            }
        }
    }

    private static String lowerCaseFirstLetter(String word) {
        return word.substring(0, 1).toLowerCase() + word.substring(1);
    }

    private static void logTypeField(String fieldType) {
        log.info("=====[UTILITY_PREPARE_CRITERIA] Field is {} Class=====", fieldType);
    }

}
