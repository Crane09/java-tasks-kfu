public class step_9{
    abstract class KeywordAnalyzer implements TextAnalyzer {
    protected abstract String[] getKeywords();
    protected abstract Label getLabel();

    @Override
    public Label processText(String text) {
        for (String k : getKeywords()) {
            if (text.contains(k)) {
                return getLabel();
            }
        }
        return Label.OK;
    }
}
class SpamAnalyzer extends KeywordAnalyzer {
    private final String[] keywords;

    public SpamAnalyzer(String[] keywords) {
        this.keywords = keywords;
    }

    @Override
    protected String[] getKeywords() {
        return keywords;
    }

    @Override
    protected Label getLabel() {
        return Label.SPAM;
    }
}
class NegativeTextAnalyzer extends KeywordAnalyzer {
    private final String[] keywords = {":(", "=(" , ":|"};

    public NegativeTextAnalyzer() {}

    @Override
    protected String[] getKeywords() {
        return keywords;
    }

    @Override
    protected Label getLabel() {
        return Label.NEGATIVE_TEXT;
    }
}
class TooLongTextAnalyzer implements TextAnalyzer {
    private final int maxLength;

    public TooLongTextAnalyzer(int maxLength) {
        this.maxLength = maxLength;
    }

    @Override
    public Label processText(String text) {
        return text.length() > maxLength ? Label.TOO_LONG : Label.OK;
    }
}
public Label checkLabels(TextAnalyzer[] analyzers, String text) {
    for (TextAnalyzer a : analyzers) {
        Label l = a.processText(text);
        if (l != Label.OK) {
            return l;
        }
    }
    return Label.OK;
}
}