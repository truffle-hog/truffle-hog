package edu.kit.trufflehog;

import edu.kit.trufflehog.model.FileSystem;
import edu.kit.trufflehog.model.configdata.ConfigDataModel;

/**
 * <p>
 * </p>
 *
 * @author Julian Brendl
 * @version 1.0
 */
public class Debug {
    public static void main(String[] args) {
        ConfigDataModel configDataModel = new ConfigDataModel(new FileSystem());
        configDataModel.load();
//        StringProperty stringProperty1 = new SimpleStringProperty();
//        StringProperty  stringProperty2 = new SimpleStringProperty();
//        stringProperty1.bindBidirectional(stringProperty2);
//        stringProperty1.addListener((observable, oldValue, newValue) -> {
//            System.out.println("stringProperty1 changed from old value: " + oldValue + " to new value: " + newValue);
//        });
//
//        IntegerProperty integerProperty1 = new SimpleIntegerProperty();
//        IntegerProperty  integerProperty2 = new SimpleIntegerProperty();
//        integerProperty1.bindBidirectional(integerProperty2);
//        integerProperty1.addListener((observable, oldValue, newValue) -> {
//            System.out.println("integerProperty1 changed from old value: " + oldValue + " to new value: " + newValue);
//        });
//
//        Map<Class<? extends Property>, Map<String, ? extends Property>> map = new HashMap<>();
//        map.put(IntegerProperty.class, new HashMap<>());
//        map.put("key2", stringProperty2);
//        map.put("key3", integerProperty1);
//        map.put("key4", integerProperty1);
//
//        Class clazz = StringProperty.class;
//
//        StringProperty propertyTemp1 = (clazz) map.get("key1");
//        propertyTemp1.setValue("hello");
//        propertyTemp1.setValue("world");
//
//        IntegerProperty propertyTemp2 = (IntegerProperty) map.get("key3");
//        propertyTemp2.setValue(1);
//        propertyTemp2.setValue(3);
    }
}
