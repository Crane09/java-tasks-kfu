public class step_14 {
    public static class MailMessage {
    private final String from;
    private final String to;
    private final String content;

    public MailMessage(String from, String to, String content) {
        this.from = from;
        this.to = to;
        this.content = content;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getContent() {
        return content;
    }
}
public static class Salary {
    private final String from;
    private final String to;
    private final Integer content;

    public Salary(String from, String to, Integer content) {
        this.from = from;
        this.to = to;
        this.content = content;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public Integer getContent() {
        return content;
    }
}
public static class MailService<T>
        implements java.util.function.Consumer<Object> {

    private final java.util.Map<String, java.util.List<T>> mailBox =
            new java.util.HashMap<>();

    @Override
    public void accept(Object o) {
        try {
            String to = (String) o.getClass().getMethod("getTo").invoke(o);
            @SuppressWarnings("unchecked")
            T content = (T) o.getClass().getMethod("getContent").invoke(o);

            mailBox.computeIfAbsent(to, k -> new java.util.ArrayList<>())
                   .add(content);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public java.util.Map<String, java.util.List<T>> getMailBox() {
        return new java.util.HashMap<String, java.util.List<T>>(mailBox) {
            @Override
            public java.util.List<T> get(Object key) {
                return super.getOrDefault(
                        key,
                        java.util.Collections.emptyList()
                );
            }
        };
    }
}
}
