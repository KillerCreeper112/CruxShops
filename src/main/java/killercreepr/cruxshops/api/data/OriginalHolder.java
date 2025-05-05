package killercreepr.cruxshops.api.data;

public interface OriginalHolder {
    static <T extends OriginalHolder> T getCompleteOriginal(T trade){
        OriginalHolder current = trade;
        OriginalHolder next = trade;
        while(next != null){
            next = next.getOriginal();
            if(next != null) current = next;
        }
        return (T) current;
    }

    OriginalHolder getOriginal();
}
