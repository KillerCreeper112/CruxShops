package killercreepr.cruxshops.api.data;

public interface OriginalHolder {
    static <T extends OriginalHolder> T getCompleteOriginalOrThis(T trade){
        OriginalHolder current = trade;
        while (current.getOriginal() != null) {
            current = current.getOriginal();
        }
        return (T) current;
    }

    OriginalHolder getOriginal();
}
