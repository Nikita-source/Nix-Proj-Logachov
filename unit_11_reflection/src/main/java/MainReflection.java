public class MainReflection {
    public static void main(String[] args) {
        ApplyProperties applyProperty = new ApplyProperties();
        AppProperties appProperty = applyProperty.createPropertyObject(AppProperties.class);
        System.out.println(appProperty);
    }
}
